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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ts.insight.service.AttendanceService;
import vn.ts.insight.service.EmployeeService;
import vn.ts.insight.web.dto.attendance.AttendanceRequest;
import vn.ts.insight.web.dto.attendance.AttendanceResponse;
import vn.ts.insight.web.dto.employee.EmployeeResponse;

@Controller
@RequestMapping("/cms/attendance")
@PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String manageAttendance(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        Model model
    ) {
        model.addAttribute("pageTitle", "Chấm công");
        model.addAttribute("pageHeader", "Quản lý chấm công");
        populateModel(model, page, size);
        if (!model.containsAttribute("attendanceForm")) {
            model.addAttribute("attendanceForm", new AttendanceRequest());
        }
        if (!model.containsAttribute("modalMode")) {
            model.addAttribute("modalMode", "create");
        }
        if (!model.containsAttribute("showModal")) {
            model.addAttribute("showModal", false);
        }
        return "attendance/manage";
    }

    @PostMapping
    public String createAttendance(
        @Valid @ModelAttribute("attendanceForm") AttendanceRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.attendanceForm", bindingResult);
            redirectAttributes.addFlashAttribute("attendanceForm", request);
            redirectAttributes.addFlashAttribute("modalMode", "create");
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin");
            return "redirect:/cms/attendance";
        }

        attendanceService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã ghi nhận chấm công");
        return "redirect:/cms/attendance";
    }

    @PostMapping("/{id}/update")
    public String updateAttendance(
        @PathVariable Long id,
        @Valid @ModelAttribute("attendanceForm") AttendanceRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.attendanceForm", bindingResult);
            redirectAttributes.addFlashAttribute("attendanceForm", request);
            redirectAttributes.addFlashAttribute("modalMode", "edit");
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("editId", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin");
            return "redirect:/cms/attendance";
        }

        attendanceService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật bản ghi chấm công");
        return "redirect:/cms/attendance";
    }

    @PostMapping("/{id}/delete")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        attendanceService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa bản ghi chấm công");
        return "redirect:/cms/attendance";
    }

    private void populateModel(Model model, int page, int size) {
        var recordPage = attendanceService.findPage(page, size);
        List<EmployeeResponse> employees = employeeService.findAll();
        model.addAttribute("records", recordPage.getContent());
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", recordPage.getNumber());
        model.addAttribute("pageSize", recordPage.getSize());
        model.addAttribute("totalPages", recordPage.getTotalPages());
        model.addAttribute("totalElements", recordPage.getTotalElements());
    }
}
