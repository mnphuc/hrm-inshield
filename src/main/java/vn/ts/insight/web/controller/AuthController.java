package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.ts.insight.domain.common.SystemRoleName;
import vn.ts.insight.service.AuthService;
import vn.ts.insight.web.dto.auth.RegisterUserRequest;

@Controller
@RequestMapping("/auth")
@PreAuthorize("hasRole('ADMIN')")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String manageAccounts(
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

        if (!model.containsAttribute("accountForm")) {
            model.addAttribute("accountForm", new RegisterUserRequest());
        }
        if (!model.containsAttribute("showModal")) {
            model.addAttribute("showModal", false);
        }
        model.addAttribute("allRoles", SystemRoleName.values());
        model.addAttribute("pageTitle", "Quản lí tài khoản");
        model.addAttribute("pageHeader", "Danh sách tài khoản");
        return "auth/manage";
    }

    @PostMapping("/{id}/update")
    public String updateAccount(
            @PathVariable Long id,
            @Valid @ModelAttribute("accountForm") RegisterUserRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.accountForm", bindingResult);
            redirectAttributes.addFlashAttribute("accountForm", request);
            redirectAttributes.addFlashAttribute("showModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhập");
            return "redirect:/auth";
        }
        authService.update(id, request);
        System.out.println("Roles received: " + request.getRoles());
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật tài khoản");
        return "redirect:/auth";
    }
    @PostMapping("/{id}/delete")
    public String deleteAccount(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        authService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa nhân viên");
        return "redirect:/auth";
    }
    private void populateModel(Model model, int page, int size) {
        var accountPage = authService.findPage(page, size);
        model.addAttribute("accounts", accountPage.getContent());
        model.addAttribute("currentPage", accountPage.getNumber());
        model.addAttribute("pageSize", accountPage.getSize());
        model.addAttribute("totalPages", accountPage.getTotalPages());
        model.addAttribute("totalElements", accountPage.getTotalElements());    }

}
