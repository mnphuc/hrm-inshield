package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.expense.Expense;
import vn.ts.insight.web.dto.expense.ExpenseResponse;

@Component
public class ExpenseMapper {

    public ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setEmployeeId(expense.getEmployee() != null ? expense.getEmployee().getId() : null);
        response.setProjectId(expense.getProject() != null ? expense.getProject().getId() : null);
        response.setCategory(expense.getCategory());
        response.setAmount(expense.getAmount());
        response.setIncurredDate(expense.getIncurredDate());
        response.setDescription(expense.getDescription());
        response.setStatus(expense.getStatus());
        return response;
    }
}
