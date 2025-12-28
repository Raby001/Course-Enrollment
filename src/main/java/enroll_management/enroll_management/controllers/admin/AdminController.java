package enroll_management.enroll_management.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("title", "Admin Dashboard");
        return "admin/dashboard";
    }
}
