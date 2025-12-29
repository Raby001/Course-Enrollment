package enroll_management.enroll_management.controllers.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class StudentController {

    @GetMapping("/student/home")
    public String studentDashboard(Model model) {
        model.addAttribute("title", "Student Dashboard");
        return "student/home";
    }

}
