package vn.ts.insight.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Tổng quan");
        model.addAttribute("pageHeader", "Bảng điều khiển");
        return "dashboard";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Đăng nhập");
        model.addAttribute("pageHeader", "Truy cập hệ thống");
        return "login";
    }
}
