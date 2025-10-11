package vn.ts.insight.web.dto.expense;

import jakarta.validation.constraints.NotNull;
import vn.ts.insight.domain.common.ExpenseStatus;

public class ExpenseStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private ExpenseStatus status;

    public ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }
}
