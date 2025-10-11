package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ts.insight.service.EmployeeService;
import vn.ts.insight.service.PayrollService;
import vn.ts.insight.web.dto.employee.EmployeeResponse;
import vn.ts.insight.web.dto.payroll.PayrollRequest;
import vn.ts.insight.web.dto.payroll.PayrollResponse;

@Controller
@RequestMapping("/cms/payrolls")
public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeService employeeService;

    public PayrollController(PayrollService payrollService, EmployeeService employeeService) {
        this.payrollService = payrollService;
        this.employeeService = employeeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER','EMPLOYEE')")
    public String viewPayrolls(
        @RequestParam(value = "employeeId", required = false) Long employeeId,
        Model model
    ) {
        model.addAttribute("pageTitle", "Bang luong");
        model.addAttribute("pageHeader", "Quan ly bang luong");
        populateModel(model, employeeId);
        if (!model.containsAttribute("payrollForm")) {
            model.addAttribute("payrollForm", new PayrollRequest());
        }
        model.addAttribute("selectedEmployeeId", employeeId);
        return "payroll/manage";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public String generatePayroll(
        @Valid @ModelAttribute("payrollForm") PayrollRequest request,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Bang luong");
            model.addAttribute("pageHeader", "Quan ly bang luong");
            populateModel(model, null);
            return "payroll/manage";
        }

        payrollService.generate(request);
        redirectAttributes.addFlashAttribute("successMessage", "Da tao/cap nhat bang luong");
        return "redirect:/cms/payrolls";
    }

    private void populateModel(Model model, Long employeeId) {
        List<EmployeeResponse> employees = employeeService.findAll();
        List<PayrollResponse> payrolls = (employeeId != null)
            ? payrollService.findByEmployee(employeeId)
            : payrollService.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("payrolls", payrolls);
    }
}
