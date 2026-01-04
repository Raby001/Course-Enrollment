package enroll_management.enroll_management.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/adminDashboard")
    public String adminDashboard(Model model){
        model.addAttribute("title", "Admin Dashboard");
        return "admin/adminDashboard";
    }

    @GetMapping("/classrooms")
    public String classroomList(Model model) {
        return "admin/classroom/list";
    }

    @GetMapping("/classrooms/create")
    public String classroomForm(Model model) {
        return "admin/classroom/form";
    }

    @GetMapping("/schedules")
    public String scheduleList(Model model) {
        return "admin/schedule/list";
    }

    @GetMapping("/schedules/create")
    public String scheduleForm(Model model) {
        return "admin/schedule/form";
    }
}
