package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.dto.admin.EnrollmentCreateDto;
import enroll_management.enroll_management.services.admin.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/enrollments")
@PreAuthorize("hasRole('STUDENT')")
public class StudentEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // =========================
    // VIEW MY ENROLLMENTS
    // =========================
    @GetMapping
    public String myEnrollments(Authentication authentication, Model model) {

        model.addAttribute(
                "enrollments",
                enrollmentService.getMyEnrollments(authentication)
        );

        return "student/enrollments"; // Thymeleaf page
    }

    // =========================
    // ENROLL INTO A COURSE
    // =========================
    @PostMapping("/enroll")
    public String enrollCourse(
            @Valid @ModelAttribute EnrollmentCreateDto enrollmentDto,
            Authentication authentication) {

        enrollmentService.createEnrollment(enrollmentDto, authentication);
        return "redirect:/student/enrollments";
    }

    // =========================
    // DROP (DELETE) ENROLLMENT
    // =========================
    @PostMapping("/drop/{id}")
    public String dropEnrollment(
            @PathVariable Long id,
            Authentication authentication) {

        enrollmentService.deleteEnrollment(id, authentication);
        return "redirect:/student/enrollments";
    }
}
