package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

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
import vn.ts.insight.domain.common.SystemRoleName;
import vn.ts.insight.service.EmployeeService;
import vn.ts.insight.web.dto.employee.EmployeeRequest;
import vn.ts.insight.web.dto.employee.EmployeeResponse;

@Controller
@RequestMapping("/cms/employees")
@PreAuthorize("hasAnyRole('ADMIN','HR')")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String manageEmployees(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        populateModel(model, page, size);
        Long totalElements = (Long) model.getAttribute("totalElements");
        int startIndex = page * size + 1;
        int endIndex = (int) Math.min((page + 1) * size, totalElements);

        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", endIndex);

        if (!model.containsAttribute("employeeForm")) {
            model.addAttribute("employeeForm", new EmployeeRequest());
        }
        if (!model.containsAttribute("modalMode")) {
            model.addAttribute("modalMode", "create");
        }
        if (!model.containsAttribute("showModal")) {
            model.addAttribute("showModal", false);
        }
        model.addAttribute("roleOptions", SystemRoleName.values());
        model.addAttribute("pageTitle", "Quản lý nhân viên");
        model.addAttribute("pageHeader", "Danh sách nhân sự");
        return "employees/manage";
    }

    @PostMapping
    public String createEmployee(
        @Valid @ModelAttribute("employeeForm") EmployeeRequest request,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeForm", bindingResult);
            redirectAttributes.addFlashAttribute("employeeForm", request);
            redirectAttributes.addFlashAttribute("modalMode", "create");
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhập");
            return "redirect:/cms/employees";
        }
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            request.setRoles(Set.of(SystemRoleName.EMPLOYEE));
        }

        try {
            employeeService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm nhân viên mới");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeForm", bindingResult);
            redirectAttributes.addFlashAttribute("employeeForm", request);
            redirectAttributes.addFlashAttribute("modalMode", "create");
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/cms/employees";
    }

    @PostMapping("/{id}/update")
    public String updateEmployee(
        @PathVariable Long id,
        @Valid @ModelAttribute("employeeForm") EmployeeRequest request,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeForm", bindingResult);
            redirectAttributes.addFlashAttribute("employeeForm", request);
            redirectAttributes.addFlashAttribute("editId", id);
            redirectAttributes.addFlashAttribute("modalMode", "edit");
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhập");
            return "redirect:/cms/employees";
        }

        try {
            employeeService.update(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật nhân viên");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeForm", bindingResult);
            redirectAttributes.addFlashAttribute("employeeForm", request);
            redirectAttributes.addFlashAttribute("editId", id);
            redirectAttributes.addFlashAttribute("modalMode", "edit");
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/cms/employees";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa nhân viên");
        return "redirect:/cms/employees";
    }

    private void populateModel(Model model, int page, int size) {
        var employeePage = employeeService.findPage(page, size);
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("managerOptions", employeeService.findAll());
        model.addAttribute("currentPage", employeePage.getNumber());
        model.addAttribute("pageSize", employeePage.getSize());
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalElements", employeePage.getTotalElements());
    }

    private EmployeeRequest toRequest(EmployeeResponse response) {
        EmployeeRequest request = new EmployeeRequest();
        request.setCode(response.getCode());
        request.setFullName(response.getFullName());
        request.setEmail(response.getEmail());
        request.setPhone(response.getPhone());
        request.setDepartment(response.getDepartment());
        request.setPosition(response.getPosition());
        request.setHireDate(response.getHireDate());
        request.setTerminationDate(response.getTerminationDate());
        request.setEmploymentStatus(response.getEmploymentStatus());
        request.setBaseSalary(response.getBaseSalary());
        request.setManagerId(response.getManagerId());
        request.setUserId(response.getUserId());
        return request;
    }
}
