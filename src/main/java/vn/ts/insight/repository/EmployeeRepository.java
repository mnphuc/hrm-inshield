package vn.ts.insight.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCode(String code);
    Optional<Employee> findByEmail(String email);
}
