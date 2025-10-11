package vn.ts.insight.web.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterUserRequest());
        }
        prepareModel(model);
        return "auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(
        @Valid @ModelAttribute("registerForm") RegisterUserRequest request,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareModel(model);
            return "auth/register";
        }

        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("successMessage", "Da tao tai khoan thanh cong");
            return "redirect:/auth/register";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("registerError", ex.getMessage());
            prepareModel(model);
            return "auth/register";
        }
    }

    private void prepareModel(Model model) {
        model.addAttribute("pageTitle", "Tao tai khoan");
        model.addAttribute("pageHeader", "Dang ky nguoi dung moi");
        model.addAttribute("roleOptions", SystemRoleName.values());
    }
}
