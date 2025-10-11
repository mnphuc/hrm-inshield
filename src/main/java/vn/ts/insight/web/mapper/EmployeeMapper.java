package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.web.dto.employee.EmployeeRequest;
import vn.ts.insight.web.dto.employee.EmployeeResponse;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setCode(employee.getCode());
        response.setFullName(employee.getFullName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDepartment(employee.getDepartment());
        response.setPosition(employee.getPosition());
        response.setHireDate(employee.getHireDate());
        response.setTerminationDate(employee.getTerminationDate());
        response.setEmploymentStatus(employee.getEmploymentStatus());
        response.setBaseSalary(employee.getBaseSalary());
        response.setManagerId(employee.getManager() != null ? employee.getManager().getId() : null);
        response.setUserId(employee.getAccount() != null ? employee.getAccount().getId() : null);
        return response;
    }

    public void updateEntity(Employee employee, EmployeeRequest request) {
        employee.setCode(request.getCode());
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPosition());
        employee.setHireDate(request.getHireDate());
        employee.setTerminationDate(request.getTerminationDate());
        employee.setEmploymentStatus(request.getEmploymentStatus());
        employee.setBaseSalary(request.getBaseSalary());
    }
}
