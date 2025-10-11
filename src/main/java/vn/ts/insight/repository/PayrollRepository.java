package vn.ts.insight.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.payroll.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeId(Long employeeId);
    Optional<Payroll> findByEmployeeIdAndPayrollYearAndPayrollMonth(Long employeeId, int year, int month);
}
