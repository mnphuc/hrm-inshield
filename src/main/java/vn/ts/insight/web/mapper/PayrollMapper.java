package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.payroll.Payroll;
import vn.ts.insight.web.dto.payroll.PayrollResponse;

@Component
public class PayrollMapper {

    public PayrollResponse toResponse(Payroll payroll) {
        PayrollResponse response = new PayrollResponse();
        response.setId(payroll.getId());
        response.setEmployeeId(payroll.getEmployee() != null ? payroll.getEmployee().getId() : null);
        response.setPayrollYear(payroll.getPayrollYear());
        response.setPayrollMonth(payroll.getPayrollMonth());
        response.setBaseSalary(payroll.getBaseSalary());
        response.setOvertimeHours(payroll.getOvertimeHours());
        response.setOvertimeAmount(payroll.getOvertimeAmount());
        response.setBonus(payroll.getBonus());
        response.setDeductions(payroll.getDeductions());
        response.setNetPay(payroll.getNetPay());
        response.setGeneratedAt(payroll.getGeneratedAt());
        return response;
    }
}
