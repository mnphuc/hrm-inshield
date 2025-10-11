package vn.ts.insight.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.common.LeaveStatus;
import vn.ts.insight.domain.leave.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    List<LeaveRequest> findByStatus(LeaveStatus status);
}
