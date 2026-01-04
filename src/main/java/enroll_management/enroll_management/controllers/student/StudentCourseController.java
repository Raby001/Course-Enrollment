package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.services.admin.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/courses")
@PreAuthorize("hasRole('ROLE_STUDENT')") 
public class StudentCourseController {

    @Autowired
    private CourseService courseService;

    // =========================
    // VIEW ALL COURSES
    // =========================
    @GetMapping
    public String viewAllCourses(Model model) {

        model.addAttribute(
                "courses",
                courseService.getAllCourses()
        );

        return "student/course/courses"; // Thymeleaf page
    }

    // =========================
    // VIEW COURSE DETAILS
    // =========================
    @GetMapping("/{id}")
    public String viewCourseDetail(
            @PathVariable("id") Long id,
            Model model) {

        model.addAttribute(
                "course",
                courseService.getCourseById(id)
        );

        return "student/course/course-details";
    }
}
