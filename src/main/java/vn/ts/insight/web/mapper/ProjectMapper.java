package vn.ts.insight.web.mapper;

import org.springframework.stereotype.Component;
import vn.ts.insight.domain.project.Project;
import vn.ts.insight.domain.project.ProjectAssignment;
import vn.ts.insight.web.dto.project.ProjectAssignmentResponse;
import vn.ts.insight.web.dto.project.ProjectResponse;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setCode(project.getCode());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setStatus(project.getStatus());
        response.setManagerId(project.getManager() != null ? project.getManager().getId() : null);
        return response;
    }

    public ProjectAssignmentResponse toResponse(ProjectAssignment assignment) {
        ProjectAssignmentResponse response = new ProjectAssignmentResponse();
        response.setId(assignment.getId());
        response.setProjectId(assignment.getProject() != null ? assignment.getProject().getId() : null);
        response.setEmployeeId(assignment.getEmployee() != null ? assignment.getEmployee().getId() : null);
        response.setRoleName(assignment.getRoleName());
        response.setAllocationPercent(assignment.getAllocationPercent());
        response.setStartDate(assignment.getStartDate());
        response.setEndDate(assignment.getEndDate());
        return response;
    }
}
