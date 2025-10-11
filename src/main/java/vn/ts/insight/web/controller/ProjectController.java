package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ts.insight.domain.common.ProjectStatus;
import vn.ts.insight.service.EmployeeService;
import vn.ts.insight.service.ProjectService;
import vn.ts.insight.web.dto.employee.EmployeeResponse;
import vn.ts.insight.web.dto.project.ProjectAssignmentRequest;
import vn.ts.insight.web.dto.project.ProjectAssignmentResponse;
import vn.ts.insight.web.dto.project.ProjectRequest;
import vn.ts.insight.web.dto.project.ProjectResponse;

@Controller
@RequestMapping("/cms/projects")
@PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String manageProjects(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        model.addAttribute("pageTitle", "Dự án");
        model.addAttribute("pageHeader", "Quản lý dự án");
        populateModel(model, page, size);
        if (!model.containsAttribute("projectForm")) {
            model.addAttribute("projectForm", new ProjectRequest());
        }
        if (!model.containsAttribute("assignmentForm")) {
            model.addAttribute("assignmentForm", new ProjectAssignmentRequest());
        }
        if (!model.containsAttribute("projectModalMode")) {
            model.addAttribute("projectModalMode", "create");
        }
        if (!model.containsAttribute("showProjectModal")) {
            model.addAttribute("showProjectModal", false);
        }
        if (!model.containsAttribute("showAssignmentModal")) {
            model.addAttribute("showAssignmentModal", false);
        }
        return "projects/manage";
    }

    @PostMapping
    public String createProject(
        @Valid @ModelAttribute("projectForm") ProjectRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectForm", bindingResult);
            redirectAttributes.addFlashAttribute("projectForm", request);
            redirectAttributes.addFlashAttribute("projectModalMode", "create");
            redirectAttributes.addFlashAttribute("showProjectModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra thông tin dự án");
            return "redirect:/cms/projects";
        }

        projectService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo dự án mới");
        return "redirect:/cms/projects";
    }

    @PostMapping("/{id}/update")
    public String updateProject(
        @PathVariable Long id,
        @Valid @ModelAttribute("projectForm") ProjectRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectForm", bindingResult);
            redirectAttributes.addFlashAttribute("projectForm", request);
            redirectAttributes.addFlashAttribute("projectModalMode", "edit");
            redirectAttributes.addFlashAttribute("showProjectModal", true);
            redirectAttributes.addFlashAttribute("projectEditId", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra thông tin dự án");
            return "redirect:/cms/projects";
        }

        projectService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật dự án");
        return "redirect:/cms/projects";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa dự án");
        return "redirect:/cms/projects";
    }

    @PostMapping("/assign")
    public String assignMember(
        @Valid @ModelAttribute("assignmentForm") ProjectAssignmentRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.assignmentForm", bindingResult);
            redirectAttributes.addFlashAttribute("assignmentForm", request);
            redirectAttributes.addFlashAttribute("showAssignmentModal", true);
            redirectAttributes.addFlashAttribute("assignmentProjectId", request.getProjectId());
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra thông tin phân công");
            return "redirect:/cms/projects";
        }

        projectService.assign(request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã gán nhân sự vào dự án");
        return "redirect:/cms/projects";
    }

    @PostMapping("/assignments/{assignmentId}/delete")
    public String removeAssignment(@PathVariable Long assignmentId, RedirectAttributes redirectAttributes) {
        projectService.removeAssignment(assignmentId);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa phân công dự án");
        return "redirect:/cms/projects";
    }

    private void populateModel(Model model, int page, int size) {
        var projectPage = projectService.findPage(page, size);
        List<ProjectResponse> projects = projectPage.getContent();
        Map<Long, List<ProjectAssignmentResponse>> assignmentMap = projects.stream()
            .collect(Collectors.toMap(
                ProjectResponse::getId,
                project -> projectService.findAssignmentsByProject(project.getId())
            ));
        List<EmployeeResponse> employees = employeeService.findAll();

        model.addAttribute("projects", projects);
        model.addAttribute("assignmentMap", assignmentMap);
        model.addAttribute("employees", employees);
        model.addAttribute("projectStatuses", ProjectStatus.values());
        model.addAttribute("currentPage", projectPage.getNumber());
        model.addAttribute("pageSize", projectPage.getSize());
        model.addAttribute("totalPages", projectPage.getTotalPages());
        model.addAttribute("totalElements", projectPage.getTotalElements());
    }
}
