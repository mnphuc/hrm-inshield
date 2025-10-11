package vn.ts.insight.web.dto.leave;

import jakarta.validation.constraints.NotNull;
import vn.ts.insight.domain.common.LeaveStatus;

public class LeaveDecisionRequest {
    @NotNull(message = "Status is required")
    private LeaveStatus status;
    @NotNull(message = "Approver is required")
    private Long approverId;

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }
}
