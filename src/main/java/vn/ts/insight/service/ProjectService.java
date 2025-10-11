package vn.ts.insight.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.domain.project.Project;
import vn.ts.insight.domain.project.ProjectAssignment;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.ProjectAssignmentRepository;
import vn.ts.insight.repository.ProjectRepository;
import vn.ts.insight.web.dto.project.ProjectAssignmentRequest;
import vn.ts.insight.web.dto.project.ProjectAssignmentResponse;
import vn.ts.insight.web.dto.project.ProjectRequest;
import vn.ts.insight.web.dto.project.ProjectResponse;
import vn.ts.insight.web.mapper.ProjectMapper;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectAssignmentRepository projectAssignmentRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(
        ProjectRepository projectRepository,
        EmployeeRepository employeeRepository,
        ProjectAssignmentRepository projectAssignmentRepository,
        ProjectMapper projectMapper
    ) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.projectAssignmentRepository = projectAssignmentRepository;
        this.projectMapper = projectMapper;
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
            .map(projectMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Page<ProjectResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> result = projectRepository.findAll(pageable);
        List<ProjectResponse> content = result.getContent().stream()
            .map(projectMapper::toResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    public ProjectResponse getById(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return projectMapper.toResponse(project);
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project();
        mapProject(project, request);
        projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        mapProject(project, request);
        return projectMapper.toResponse(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public ProjectAssignmentResponse assign(ProjectAssignmentRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Employee employee = employeeRepository.findById(request.getEmployeeId())
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        ProjectAssignment assignment = new ProjectAssignment();
        assignment.setProject(project);
        assignment.setEmployee(employee);
        assignment.setRoleName(request.getRoleName());
        assignment.setAllocationPercent(request.getAllocationPercent());
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());

        projectAssignmentRepository.save(assignment);
        return projectMapper.toResponse(assignment);
    }

    public List<ProjectAssignmentResponse> findAssignmentsByProject(Long projectId) {
        return projectAssignmentRepository.findByProjectId(projectId).stream()
            .map(projectMapper::toResponse)
            .collect(Collectors.toList());
    }

    public void removeAssignment(Long assignmentId) {
        projectAssignmentRepository.deleteById(assignmentId);
    }

    private void mapProject(Project project, ProjectRequest request) {
        project.setCode(request.getCode());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
            project.setManager(manager);
        } else {
            project.setManager(null);
        }
    }
}
