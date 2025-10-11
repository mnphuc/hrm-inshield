package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.payroll.Payroll;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.PayrollRepository;
import vn.ts.insight.web.dto.payroll.PayrollRequest;
import vn.ts.insight.web.dto.payroll.PayrollResponse;
import vn.ts.insight.web.mapper.PayrollMapper;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollMapper payrollMapper;

    public PayrollService(
        PayrollRepository payrollRepository,
        EmployeeRepository employeeRepository,
        PayrollMapper payrollMapper
    ) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.payrollMapper = payrollMapper;
    }

    @Transactional
    public PayrollResponse generate(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Payroll payroll = payrollRepository
            .findByEmployeeIdAndPayrollYearAndPayrollMonth(employee.getId(), request.getPayrollYear(), request.getPayrollMonth())
            .orElseGet(Payroll::new);

        payroll.setEmployee(employee);
        payroll.setPayrollYear(request.getPayrollYear());
        payroll.setPayrollMonth(request.getPayrollMonth());

        payroll.setBaseSalary(defaultIfNull(request.getBaseSalary(), employee.getBaseSalary()));
        payroll.setOvertimeHours(defaultIfNull(request.getOvertimeHours(), BigDecimal.ZERO));
        payroll.setOvertimeAmount(defaultIfNull(request.getOvertimeAmount(), BigDecimal.ZERO));
        payroll.setBonus(defaultIfNull(request.getBonus(), BigDecimal.ZERO));
        payroll.setDeductions(defaultIfNull(request.getDeductions(), BigDecimal.ZERO));

        BigDecimal netPay = payroll.getBaseSalary()
            .add(payroll.getOvertimeAmount())
            .add(payroll.getBonus())
            .subtract(payroll.getDeductions());
        payroll.setNetPay(netPay);

        payrollRepository.save(payroll);
        return payrollMapper.toResponse(payroll);
    }

    public List<PayrollResponse> findAll() {
        return payrollRepository.findAll().stream()
            .map(payrollMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<PayrollResponse> findByEmployee(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
            .map(payrollMapper::toResponse)
            .collect(Collectors.toList());
    }

    private BigDecimal defaultIfNull(BigDecimal value, BigDecimal fallback) {
        if (value != null) {
            return value;
        }
        return fallback != null ? fallback : BigDecimal.ZERO;
    }
}
