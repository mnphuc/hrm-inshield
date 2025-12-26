package vn.ts.insight.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ts.insight.domain.common.LeaveStatus;
import vn.ts.insight.repository.EmployeeRepository;
import vn.ts.insight.repository.EquipmentRepository;
import vn.ts.insight.repository.LeaveRequestRepository;
import vn.ts.insight.repository.ProjectRepository;

@Controller
public class HomeController {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EquipmentRepository equipmentRepository;

    public HomeController(
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository,
            LeaveRequestRepository leaveRequestRepository,
            EquipmentRepository equipmentRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Tổng quan");
        model.addAttribute("pageHeader", "Bảng điều khiển");
        
        // Statistics
        model.addAttribute("employeeCount", employeeRepository.count());
        model.addAttribute("projectCount", projectRepository.count());
        model.addAttribute("pendingLeaveCount", leaveRequestRepository.findByStatus(LeaveStatus.PENDING).size());
        model.addAttribute("equipmentCount", equipmentRepository.count());
        
        return "dashboard";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Đăng nhập");
        model.addAttribute("pageHeader", "Truy cập hệ thống");
        return "login";
    }
}
