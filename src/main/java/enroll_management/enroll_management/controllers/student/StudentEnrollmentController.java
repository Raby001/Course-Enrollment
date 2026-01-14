package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.EnrollmentCreateDto;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.admin.EnrollmentService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping
    public String myEnrollments(Authentication authentication, Model model) {

        User student = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Enrollment> enrollments =
                enrollmentRepository.findByStudentId(student.getId());

        model.addAttribute("enrollments", enrollments);
        return "student/enrollment/enrollments";
    }

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
                    "successMessage",
                    "Successfully enrolled in course");

        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    ex.getMessage());
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/student/courses");
    }

    @PostMapping("/drop/{id}")
    public String dropEnrollment(
            @PathVariable("id") Long id,
            Authentication authentication,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            enrollmentService.deleteEnrollment(id, authentication);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Enrollment dropped");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    ex.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/student/enrollments");
    }
}
