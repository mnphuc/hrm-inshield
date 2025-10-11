package vn.ts.insight.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.expense.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByEmployeeId(Long employeeId);
    List<Expense> findByProjectId(Long projectId);
    List<Expense> findByIncurredDateBetween(LocalDate startDate, LocalDate endDate);
}
