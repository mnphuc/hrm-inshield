package vn.ts.insight.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ts.insight.domain.project.ProjectAssignment;

public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    List<ProjectAssignment> findByProjectId(Long projectId);
    List<ProjectAssignment> findByEmployeeId(Long employeeId);
}
