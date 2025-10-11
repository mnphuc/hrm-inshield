package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.user.UserAccount;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.UserAccountRepository;
import vn.ts.insight.web.dto.employee.EmployeeRequest;
import vn.ts.insight.web.dto.employee.EmployeeResponse;
import vn.ts.insight.web.mapper.EmployeeMapper;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(
        EmployeeRepository employeeRepository,
        UserAccountRepository userAccountRepository,
        EmployeeMapper employeeMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
            .map(employeeMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Page<EmployeeResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> result = employeeRepository.findAll(pageable);
        List<EmployeeResponse> content = result.getContent().stream()
            .map(employeeMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public EmployeeResponse getById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = new Employee();
        employeeMapper.updateEntity(employee, request);
        applyRelations(employee, request);
        employeeRepository.save(employee);
        return employeeMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        employeeMapper.updateEntity(employee, request);
        applyRelations(employee, request);
        return employeeMapper.toResponse(employee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private void applyRelations(Employee employee, EmployeeRequest request) {
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            employee.setManager(manager);
        } else {
            employee.setManager(null);
        }

        if (request.getUserId() != null) {
            UserAccount account = userAccountRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User account not found"));
            employee.setAccount(account);
        } else {
            employee.setAccount(null);
        }
    }
}
