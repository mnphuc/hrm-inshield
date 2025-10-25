package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
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
import vn.ts.insight.domain.common.EquipmentStatus;
import vn.ts.insight.domain.employee.Employee;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.service.EquipmentService;
import vn.ts.insight.web.dto.equipment.AssignEquipmentRequest;
import vn.ts.insight.web.dto.equipment.EquipmentListItemDto;
import vn.ts.insight.web.dto.equipment.EquipmentRequest;

import java.util.List;

@Controller
@RequestMapping("/cms/equipment")
@PreAuthorize("hasRole('ADMIN')")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EmployeeRepository employeeRepository;

    public EquipmentController(EquipmentService equipmentService, EmployeeRepository employeeRepository) {
        this.equipmentService = equipmentService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String manageEquipment(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) EquipmentStatus status,
            @RequestParam(required = false) String type,
            Model model) {
        
        List<EquipmentListItemDto> equipment;
        if (name != null || status != null || type != null) {
            equipment = equipmentService.findByFilters(name, status, type);
        } else {
            equipment = equipmentService.findAll();
        }
        
        List<Employee> employees = employeeRepository.findAll();
        
        model.addAttribute("equipment", equipment);
        model.addAttribute("employees", employees);
        model.addAttribute("pageTitle", "Quản lý thiết bị");
        model.addAttribute("pageHeader", "Danh sách thiết bị");
        model.addAttribute("equipmentStatuses", EquipmentStatus.values());
        
        if (!model.containsAttribute("equipmentForm")) {
            model.addAttribute("equipmentForm", new EquipmentRequest());
        }
        
        if (!model.containsAttribute("assignForm")) {
            model.addAttribute("assignForm", new AssignEquipmentRequest());
        }
        
        return "equipment/manage";
    }

    @PostMapping
    public String createEquipment(
            @Valid @ModelAttribute("equipmentForm") EquipmentRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            List<EquipmentListItemDto> equipment = equipmentService.findAll();
            List<Employee> employees = employeeRepository.findAll();
            model.addAttribute("equipment", equipment);
            model.addAttribute("employees", employees);
            model.addAttribute("equipmentStatuses", EquipmentStatus.values());
            return "equipment/manage";
        }

        try {
            equipmentService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo thiết bị thành công");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo thiết bị: " + ex.getMessage());
        }
        
        return "redirect:/cms/equipment";
    }

    @PostMapping("/{id}/assign")
    public String assignEquipment(
            @PathVariable Long id,
            @Valid @ModelAttribute("assignForm") AssignEquipmentRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dữ liệu không hợp lệ");
            return "redirect:/cms/equipment";
        }

        try {
            boolean assigned = equipmentService.assignToEmployee(id, request);
            if (assigned) {
                redirectAttributes.addFlashAttribute("successMessage", "Đã phân bổ thiết bị thành công");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không thể phân bổ thiết bị");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi phân bổ thiết bị: " + ex.getMessage());
        }
        
        return "redirect:/cms/equipment";
    }

    @PostMapping("/{id}/unassign")
    public String unassignEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean unassigned = equipmentService.unassign(id);
            if (unassigned) {
                redirectAttributes.addFlashAttribute("successMessage", "Đã hủy phân bổ thiết bị thành công");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy phân bổ thiết bị");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi hủy phân bổ thiết bị: " + ex.getMessage());
        }
        
        return "redirect:/cms/equipment";
    }

    @PostMapping("/{id}/delete")
    public String deleteEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = equipmentService.delete(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thiết bị thành công");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thiết bị");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa thiết bị: " + ex.getMessage());
        }
        
        return "redirect:/cms/equipment";
    }
}
