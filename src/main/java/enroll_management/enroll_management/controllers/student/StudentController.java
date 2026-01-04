package enroll_management.enroll_management.controllers.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import enroll_management.enroll_management.services.admin.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @Autowired
    private CourseService courseService;

    @GetMapping("student/home") // ← No /student needed here!
    public String studentHome(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "student/home";
    }

    @GetMapping("profile") // ← Now maps to /student/profile
    public String profile() {
        return "student/profile";
    }

}