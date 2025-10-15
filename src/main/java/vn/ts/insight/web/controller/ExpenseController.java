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
import vn.ts.insight.domain.common.ExpenseStatus;
import vn.ts.insight.service.EmployeeService;
import vn.ts.insight.service.ExpenseService;
import vn.ts.insight.service.ProjectService;
import vn.ts.insight.web.dto.employee.EmployeeResponse;
import vn.ts.insight.web.dto.expense.ExpenseRequest;
import vn.ts.insight.web.dto.expense.ExpenseResponse;
import vn.ts.insight.web.dto.expense.ExpenseStatusUpdateRequest;
import vn.ts.insight.web.dto.project.ProjectResponse;

@Controller
@RequestMapping("/cms/expenses")
@PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER','EMPLOYEE')")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public ExpenseController(
        ExpenseService expenseService,
        EmployeeService employeeService,
        ProjectService projectService
    ) {
        this.expenseService = expenseService;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @GetMapping
    public String manageExpenses(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        model.addAttribute("pageTitle", "Chi phí");
        model.addAttribute("pageHeader", "Quản lý chi phí");
        populateModel(model, page, size);
        if (!model.containsAttribute("expenseForm")) {
            model.addAttribute("expenseForm", new ExpenseRequest());
        }
        if (!model.containsAttribute("statusForm")) {
            model.addAttribute("statusForm", new ExpenseStatusUpdateRequest());
        }
        if (!model.containsAttribute("expenseModalMode")) {
            model.addAttribute("expenseModalMode", "create");
        }
        if (!model.containsAttribute("showExpenseModal")) {
            model.addAttribute("showExpenseModal", false);
        }
        if (!model.containsAttribute("showStatusModal")) {
            model.addAttribute("showStatusModal", false);
        }
        return "expenses/manage";
    }

    @PostMapping
    public String submitExpense(
        @Valid @ModelAttribute("expenseForm") ExpenseRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expenseForm", bindingResult);
            redirectAttributes.addFlashAttribute("expenseForm", request);
            redirectAttributes.addFlashAttribute("expenseModalMode", "create");
            redirectAttributes.addFlashAttribute("showExpenseModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra thông tin chi phí");
            return "redirect:/cms/expenses";
        }

        expenseService.submit(request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo chi phí mới");
        return "redirect:/cms/expenses";
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public String updateExpense(
        @PathVariable Long id,
        @Valid @ModelAttribute("expenseForm") ExpenseRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expenseForm", bindingResult);
            redirectAttributes.addFlashAttribute("expenseForm", request);
            redirectAttributes.addFlashAttribute("expenseModalMode", "edit");
            redirectAttributes.addFlashAttribute("showExpenseModal", true);
            redirectAttributes.addFlashAttribute("expenseEditId", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra thông tin chi phí");
            return "redirect:/cms/expenses";
        }

        expenseService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật chi phí");
        return "redirect:/cms/expenses";
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public String updateStatus(
        @PathVariable Long id,
        @Valid @ModelAttribute("statusForm") ExpenseStatusUpdateRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.statusForm", bindingResult);
            redirectAttributes.addFlashAttribute("statusForm", request);
            redirectAttributes.addFlashAttribute("showStatusModal", true);
            redirectAttributes.addFlashAttribute("statusExpenseId", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn trạng thái hợp lệ");
            return "redirect:/cms/expenses";
        }

        expenseService.updateStatus(id, request.getStatus());
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái chi phí");
        return "redirect:/cms/expenses";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public String deleteExpense(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        expenseService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa chi phí");
        return "redirect:/cms/expenses";
    }

    private void populateModel(Model model, int page, int size) {
        var expensePage = expenseService.findPage(page, size);
        int start = page * size + 1;
        int end = Math.min((page + 1) * size, (int) expensePage.getTotalElements());
        model.addAttribute("startRecord", start);
        model.addAttribute("endRecord", end);
        List<EmployeeResponse> employees = employeeService.findAll();
        List<ProjectResponse> projects = projectService.findAll();
        Map<Long, String> employeeNames = employees.stream()
                .collect(Collectors.toMap(EmployeeResponse::getId, EmployeeResponse::getFullName));
        Map<Long, String> projectNames = projects.stream()
                .collect(Collectors.toMap(ProjectResponse::getId, ProjectResponse::getName));
        model.addAttribute("expenses", expensePage.getContent());
        model.addAttribute("employees", employees);
        model.addAttribute("projects", projects);
        model.addAttribute("employeeNames", employeeNames);
        model.addAttribute("projectNames", projectNames);
        model.addAttribute("expenseStatuses", ExpenseStatus.values());
        model.addAttribute("currentPage", expensePage.getNumber());
        model.addAttribute("pageSize", expensePage.getSize());
        model.addAttribute("totalPages", expensePage.getTotalPages());
        model.addAttribute("totalElements", expensePage.getTotalElements());
    }
}
