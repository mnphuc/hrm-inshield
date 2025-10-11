package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.leave.LeaveRequest;
import vn.ts.insight.web.dto.leave.LeaveResponse;

@Component
public class LeaveMapper {

    public LeaveResponse toResponse(LeaveRequest entity) {
        LeaveResponse response = new LeaveResponse();
        response.setId(entity.getId());
        response.setEmployeeId(entity.getEmployee() != null ? entity.getEmployee().getId() : null);
        response.setType(entity.getType());
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());
        response.setTotalDays(entity.getTotalDays());
        response.setReason(entity.getReason());
        response.setStatus(entity.getStatus());
        response.setApproverId(entity.getApprover() != null ? entity.getApprover().getId() : null);
        response.setDecidedAt(entity.getDecidedAt());
        return response;
    }
}
