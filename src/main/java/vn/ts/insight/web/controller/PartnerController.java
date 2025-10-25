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
import vn.ts.insight.domain.common.PartnerType;
import vn.ts.insight.service.PartnerService;
import vn.ts.insight.web.dto.partner.PartnerListItemDto;
import vn.ts.insight.web.dto.partner.PartnerRequest;

import java.util.List;

@Controller
@RequestMapping("/cms/partners")
@PreAuthorize("hasRole('ADMIN')")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public String managePartners(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) PartnerType partnerType,
            Model model) {
        
        List<PartnerListItemDto> partners;
        if (name != null || status != null || partnerType != null) {
            partners = partnerService.findByFilters(name, status, partnerType);
        } else {
            partners = partnerService.findAll();
        }
        
        model.addAttribute("partners", partners);
        model.addAttribute("pageTitle", "Quản lý đối tác");
        model.addAttribute("pageHeader", "Danh sách đối tác");
        model.addAttribute("partnerTypes", PartnerType.values());
        
        if (!model.containsAttribute("partnerForm")) {
            model.addAttribute("partnerForm", new PartnerRequest());
        }
        
        return "partners/manage";
    }

    @PostMapping
    public String createPartner(
            @Valid @ModelAttribute("partnerForm") PartnerRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            List<PartnerListItemDto> partners = partnerService.findAll();
            model.addAttribute("partners", partners);
            model.addAttribute("partnerTypes", PartnerType.values());
            return "partners/manage";
        }

        try {
            partnerService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo đối tác thành công");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo đối tác: " + ex.getMessage());
        }
        
        return "redirect:/cms/partners";
    }

    @PostMapping("/{id}/delete")
    public String deletePartner(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = partnerService.delete(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đối tác thành công");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đối tác");
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đối tác: " + ex.getMessage());
        }
        
        return "redirect:/cms/partners";
    }
}
