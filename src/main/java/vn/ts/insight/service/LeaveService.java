package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.common.LeaveStatus;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.leave.LeaveRequest;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.LeaveRequestRepository;
import vn.ts.insight.web.dto.leave.LeaveDecisionRequest;
import vn.ts.insight.web.dto.leave.LeaveRequestPayload;
import vn.ts.insight.web.dto.leave.LeaveResponse;
import vn.ts.insight.web.mapper.LeaveMapper;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;

    public LeaveService(
        LeaveRequestRepository leaveRequestRepository,
        EmployeeRepository employeeRepository,
        LeaveMapper leaveMapper
    ) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveMapper = leaveMapper;
    }

    @Transactional
    public LeaveResponse submit(LeaveRequestPayload payload) {
        LeaveRequest entity = new LeaveRequest();
        mapPayload(entity, payload);
        leaveRequestRepository.save(entity);
        return leaveMapper.toResponse(entity);
    }

    public List<LeaveResponse> findAll() {
        return leaveRequestRepository.findAll().stream()
            .map(leaveMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Page<LeaveResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> result = leaveRequestRepository.findAll(pageable);
        List<LeaveResponse> content = result.getContent().stream()
            .map(leaveMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public List<LeaveResponse> findPending() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING).stream()
            .map(leaveMapper::toResponse)
            .collect(Collectors.toList());
    }

    public List<LeaveResponse> findByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
            .map(leaveMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public LeaveResponse decide(Long id, LeaveDecisionRequest decision) {
        LeaveRequest entity = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        if (decision.getStatus() == null || decision.getApproverId() == null) {
            throw new IllegalArgumentException("Status and approver must be provided");
        }
        Employee approver = employeeRepository.findById(decision.getApproverId())
            .orElseThrow(() -> new IllegalArgumentException("Approver not found"));
        entity.setStatus(decision.getStatus());
        entity.setApprover(approver);
        entity.setDecidedAt(LocalDateTime.now());
        return leaveMapper.toResponse(entity);
    }

    public void delete(Long id) {
        leaveRequestRepository.deleteById(id);
    }

    @Transactional
    public LeaveResponse update(Long id, LeaveRequestPayload payload) {
        LeaveRequest entity = leaveRequestRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));
        if (entity.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Chi duoc phep sua don dang cho duyet");
        }
        mapPayload(entity, payload);
        return leaveMapper.toResponse(entity);
    }

    private void mapPayload(LeaveRequest entity, LeaveRequestPayload payload) {
        Employee employee = employeeRepository.findById(payload.getEmployeeId())
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        entity.setEmployee(employee);
        entity.setType(payload.getType());
        entity.setStartDate(payload.getStartDate());
        entity.setEndDate(payload.getEndDate());
        entity.setTotalDays(payload.getTotalDays());
        entity.setReason(payload.getReason());
        entity.setStatus(LeaveStatus.PENDING);
        entity.setApprover(null);
        entity.setDecidedAt(null);
    }
}
