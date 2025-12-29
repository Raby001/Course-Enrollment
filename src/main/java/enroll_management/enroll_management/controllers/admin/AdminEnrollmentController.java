package enroll_management.enroll_management.controllers.admin;

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
@RequestMapping("/admin/enrollments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // =========================
    // VIEW ALL ENROLLMENTS
    // =========================
    @GetMapping
    public String listAllEnrollments(Model model) {
        model.addAttribute(
                "enrollments",
                enrollmentService.getAllEnrollments()
        );
        return "admin/enrollments"; // Thymeleaf page
    }

    // =========================
    // VIEW ENROLLMENTS BY STUDENT
    // =========================
    @GetMapping("/student/{studentId}")
    public String viewByStudent(
            @PathVariable Long studentId,
            Model model) {

        model.addAttribute(
                "enrollments",
                enrollmentService.getEnrollmentsByStudentId(studentId)
        );
        return "admin/enrollments";
    }

    // =========================
    // SHOW CREATE FORM
    // =========================
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("enrollment", new EnrollmentCreateDto());
        return "admin/enrollment-create";
    }

    // =========================
    // HANDLE CREATE
    // =========================
    @PostMapping("/create")
    public String createEnrollment(
            @Valid @ModelAttribute("enrollment") EnrollmentCreateDto dto,
            Authentication authentication) {

        enrollmentService.createEnrollment(dto, authentication);
        return "redirect:/admin/enrollments";
    }

    // =========================
    // DELETE (HTML FORM)
    // =========================
    @PostMapping("/delete/{id}")
    public String deleteEnrollment(
            @PathVariable Long id,
            Authentication authentication) {

        enrollmentService.deleteEnrollment(id, authentication);
        return "redirect:/admin/enrollments";
    }
}
