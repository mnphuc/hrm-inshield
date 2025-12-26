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
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "department", required = false) String department,
        @RequestParam(value = "employmentStatus", required = false) String employmentStatus,
        Model model
    ) {
        populateModel(model, page, size, name, department, employmentStatus);
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
        
        // Add employment statuses for dropdown
        List<String> employmentStatuses = List.of(
            "ĐANG LÀM VIỆC",
            "NGHỈ VIỆC",
            "TẠM NGHỈ",
            "THỬ VIỆC"
        );
        model.addAttribute("employmentStatusOptions", employmentStatuses);
        
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
        // Ensure combobox data is available even on error
        redirectAttributes.addFlashAttribute("roleOptions", SystemRoleName.values());
        redirectAttributes.addFlashAttribute("employmentStatusOptions", List.of(
            "ĐANG LÀM VIỆC",
            "NGHỈ VIỆC",
            "TẠM NGHỈ",
            "THỬ VIỆC"
        ));
        
        // Load department and position options from existing employees
        var allEmployees = employeeService.findAll();
        var departmentOptions = allEmployees.stream()
            .map(EmployeeResponse::getDepartment)
            .filter(d -> d != null && !d.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        var positionOptions = allEmployees.stream()
            .map(EmployeeResponse::getPosition)
            .filter(p -> p != null && !p.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        redirectAttributes.addFlashAttribute("departmentOptions", departmentOptions);
        redirectAttributes.addFlashAttribute("positionOptions", positionOptions);
        
        // Create mapping between department and positions
        java.util.Map<String, java.util.List<String>> departmentPositionMap = allEmployees.stream()
            .filter(e -> e.getDepartment() != null && !e.getDepartment().isEmpty() 
                      && e.getPosition() != null && !e.getPosition().isEmpty())
            .collect(java.util.stream.Collectors.groupingBy(
                EmployeeResponse::getDepartment,
                java.util.stream.Collectors.mapping(
                    EmployeeResponse::getPosition,
                    java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toSet(),
                        set -> set.stream().sorted().collect(java.util.stream.Collectors.toList())
                    )
                )
            ));
        redirectAttributes.addFlashAttribute("departmentPositionMap", departmentPositionMap);
        
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
        // Ensure combobox data is available even on error
        redirectAttributes.addFlashAttribute("roleOptions", SystemRoleName.values());
        redirectAttributes.addFlashAttribute("employmentStatusOptions", List.of(
            "ĐANG LÀM VIỆC",
            "NGHỈ VIỆC",
            "TẠM NGHỈ",
            "THỬ VIỆC"
        ));
        
        // Load department and position options from existing employees
        var allEmployees = employeeService.findAll();
        var departmentOptions = allEmployees.stream()
            .map(EmployeeResponse::getDepartment)
            .filter(d -> d != null && !d.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        var positionOptions = allEmployees.stream()
            .map(EmployeeResponse::getPosition)
            .filter(p -> p != null && !p.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        redirectAttributes.addFlashAttribute("departmentOptions", departmentOptions);
        redirectAttributes.addFlashAttribute("positionOptions", positionOptions);
        
        // Create mapping between department and positions
        java.util.Map<String, java.util.List<String>> departmentPositionMap = allEmployees.stream()
            .filter(e -> e.getDepartment() != null && !e.getDepartment().isEmpty() 
                      && e.getPosition() != null && !e.getPosition().isEmpty())
            .collect(java.util.stream.Collectors.groupingBy(
                EmployeeResponse::getDepartment,
                java.util.stream.Collectors.mapping(
                    EmployeeResponse::getPosition,
                    java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toSet(),
                        set -> set.stream().sorted().collect(java.util.stream.Collectors.toList())
                    )
                )
            ));
        redirectAttributes.addFlashAttribute("departmentPositionMap", departmentPositionMap);
        
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

    private void populateModel(Model model, int page, int size, String name, String department, String employmentStatus) {
        var employeePage = employeeService.findPageWithFilters(page, size, name, department, employmentStatus);
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("managerOptions", employeeService.findAll());
        model.addAttribute("currentPage", employeePage.getNumber());
        model.addAttribute("pageSize", employeePage.getSize());
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalElements", employeePage.getTotalElements());
        
        // Add filter values to model for form persistence
        model.addAttribute("filterName", name);
        model.addAttribute("filterDepartment", department);
        model.addAttribute("filterEmploymentStatus", employmentStatus);
        
        // Get unique departments and employment statuses for filter dropdowns
        var allEmployees = employeeService.findAll();
        var departments = allEmployees.stream()
            .map(EmployeeResponse::getDepartment)
            .filter(d -> d != null && !d.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        var employmentStatuses = allEmployees.stream()
            .map(EmployeeResponse::getEmploymentStatus)
            .filter(s -> s != null && !s.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("departments", departments);
        model.addAttribute("employmentStatuses", employmentStatuses);
        
        // Get unique departments and positions for form dropdowns
        var departmentOptions = allEmployees.stream()
            .map(EmployeeResponse::getDepartment)
            .filter(d -> d != null && !d.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        var positionOptions = allEmployees.stream()
            .map(EmployeeResponse::getPosition)
            .filter(p -> p != null && !p.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("departmentOptions", departmentOptions);
        model.addAttribute("positionOptions", positionOptions);
        
        // Create mapping between department and positions
        java.util.Map<String, java.util.List<String>> departmentPositionMap = allEmployees.stream()
            .filter(e -> e.getDepartment() != null && !e.getDepartment().isEmpty() 
                      && e.getPosition() != null && !e.getPosition().isEmpty())
            .collect(java.util.stream.Collectors.groupingBy(
                EmployeeResponse::getDepartment,
                java.util.stream.Collectors.mapping(
                    EmployeeResponse::getPosition,
                    java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.toSet(),
                        set -> set.stream().sorted().collect(java.util.stream.Collectors.toList())
                    )
                )
            ));
        model.addAttribute("departmentPositionMap", departmentPositionMap);
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
