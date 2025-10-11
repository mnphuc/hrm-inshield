package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ts.insight.domain.common.LeaveStatus;
import vn.ts.insight.domain.common.LeaveType;
import vn.ts.insight.service.EmployeeService;
import vn.ts.insight.service.LeaveService;
import vn.ts.insight.web.dto.employee.EmployeeResponse;
import vn.ts.insight.web.dto.leave.LeaveDecisionRequest;
import vn.ts.insight.web.dto.leave.LeaveRequestPayload;
import vn.ts.insight.web.dto.leave.LeaveResponse;

@Controller
@RequestMapping("/cms/leave")
@PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER','EMPLOYEE')")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public LeaveController(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String manageLeave(Model model) {
        model.addAttribute("pageTitle", "Nghi phep");
        model.addAttribute("pageHeader", "Quan ly nghi phep");
        populateModel(model);
        if (!model.containsAttribute("leaveForm")) {
            model.addAttribute("leaveForm", new LeaveRequestPayload());
        }
        if (!model.containsAttribute("decisionForm")) {
            model.addAttribute("decisionForm", new LeaveDecisionRequest());
        }
        return "leave/manage";
    }

    @PostMapping
    public String submitLeave(
        @Valid @ModelAttribute("leaveForm") LeaveRequestPayload payload,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Nghi phep");
            model.addAttribute("pageHeader", "Quan ly nghi phep");
            populateModel(model);
            return "leave/manage";
        }

        leaveService.submit(payload);
        redirectAttributes.addFlashAttribute("successMessage", "Da tao don xin nghi");
        return "redirect:/cms/leave";
    }

    @PostMapping("/{id}/decision")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public String decideLeave(
        @PathVariable Long id,
        @Valid @ModelAttribute("decisionForm") LeaveDecisionRequest decisionRequest,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Nghi phep");
            model.addAttribute("pageHeader", "Quan ly nghi phep");
            populateModel(model);
            model.addAttribute("leaveForm", new LeaveRequestPayload());
            model.addAttribute("decisionError", true);
            return "leave/manage";
        }

        leaveService.decide(id, decisionRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Da cap nhat trang thai don nghi");
        return "redirect:/cms/leave";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteLeave(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        leaveService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Da xoa don nghi");
        return "redirect:/cms/leave";
    }

    private void populateModel(Model model) {
        List<LeaveResponse> leaveRequests = leaveService.findAll();
        List<LeaveResponse> pendingRequests = leaveService.findPending();
        List<EmployeeResponse> employees = employeeService.findAll();
        model.addAttribute("leaveRequests", leaveRequests);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("employees", employees);
        model.addAttribute("leaveTypes", LeaveType.values());
        model.addAttribute("leaveStatuses", LeaveStatus.values());
    }
}
