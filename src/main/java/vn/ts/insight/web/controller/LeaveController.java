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
        model.addAttribute("pageTitle", "Nghỉ phép");
        model.addAttribute("pageHeader", "Quản lý nghỉ phép");
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
            model.addAttribute("pageTitle", "Nghỉ phép");
            model.addAttribute("pageHeader", "Quản lý nghỉ phép");
            populateModel(model);
            model.addAttribute("decisionForm", new LeaveDecisionRequest());
            return "leave/manage";
        }

        leaveService.submit(payload);
        redirectAttributes.addFlashAttribute("successMessage", "Đã tạo đơn xin nghỉ");
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
            model.addAttribute("pageTitle", "Nghỉ phép");
            model.addAttribute("pageHeader", "Quản lý nghỉ phép");
            populateModel(model);
            model.addAttribute("leaveForm", new LeaveRequestPayload());
            model.addAttribute("decisionError", true);
            return "leave/manage";
        }

        leaveService.decide(id, decisionRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái đơn nghỉ");
        return "redirect:/cms/leave";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteLeave(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        leaveService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đơn nghỉ");
        return "redirect:/cms/leave";
    }

    private void populateModel(Model model) {
        List<LeaveResponse> leaveRequests = leaveService.findAll();
        List<LeaveResponse> pendingRequests = leaveService.findPending();
        List<EmployeeResponse> employees = employeeService.findAll();
        Map<Long, String> employeeNames = employees.stream()
                .collect(Collectors.toMap(EmployeeResponse::getId, EmployeeResponse::getFullName));
        model.addAttribute("employeeNames", employeeNames);
        model.addAttribute("leaveRequests", leaveRequests);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("employees", employees);
        model.addAttribute("leaveTypes", LeaveType.values());
        model.addAttribute("leaveStatuses", LeaveStatus.values());
    }
}
