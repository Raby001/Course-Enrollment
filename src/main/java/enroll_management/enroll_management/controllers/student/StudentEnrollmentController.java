package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.EnrollmentCreateDto;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.admin.EnrollmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student/enrollments")
@PreAuthorize("hasRole('STUDENT')")
public class StudentEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // =========================
    // VIEW MY ENROLLMENTS
    // =========================
    @GetMapping
    public String myEnrollments(Authentication authentication, Model model) {

        // Get current student
        String username = authentication.getName();
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get enrollments for this student
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());

        model.addAttribute("enrollments", enrollments);
        return "student/enrollments"; // ← Template path
    }

    // =========================
    // ENROLL INTO A COURSE
    // =========================
    @PostMapping("/enroll")
    public String enrollCourse(
        @RequestParam("courseId") Long courseId,
            Authentication authentication,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            EnrollmentCreateDto dto = new EnrollmentCreateDto();
            dto.setCourseId(courseId);

            enrollmentService.createEnrollment(dto, authentication);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Successfully enrolled in course");

        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage());
        }
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        return "redirect:/student/home"; 
    }

    // =========================
    // DROP (DELETE) ENROLLMENT
    // =========================
    @PostMapping("/drop/{id}")
    public String dropEnrollment(
            @PathVariable("id") Long id,
            Authentication authentication) {

        enrollmentService.deleteEnrollment(id, authentication);
        return "redirect:/student/enrollments";
    }
}
