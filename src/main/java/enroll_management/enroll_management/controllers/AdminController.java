package enroll_management.enroll_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admindashboard")
    public String showDashboard() {
        return "admin/adminDashboard";  // Points to templates/admin/adminDashboard.html
    }
}
