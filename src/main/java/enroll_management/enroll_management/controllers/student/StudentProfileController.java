package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.EnrollmentDto;
import enroll_management.enroll_management.dto.student.StudentProfileDto;
import enroll_management.enroll_management.services.student.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/student/profile")
public class StudentProfileController {

    private final StudentProfileService profileService;

    public StudentProfileController(StudentProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public String showProfile(Model model) {
        User user = profileService.getCurrentUser();
        StudentProfileDto dto = new StudentProfileDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDob(user.getDob());
        List<EnrollmentDto> enrolledCourses = profileService.getMyEnrollments();

        model.addAttribute("profile", dto);
        model.addAttribute("user", user); 
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "student/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("profile") StudentProfileDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "student/profile";
        }

        profileService.updateProfile(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getDob());
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/student/profile";
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile image,
                              RedirectAttributes redirectAttributes) {
        if (image.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select an image");
            return "redirect:/student/profile";
        }

        try {
            profileService.uploadProfileImage(image);
            redirectAttributes.addFlashAttribute("success", "Profile image updated!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
        }
        return "redirect:/student/profile";
    }

    @PostMapping("/drop-enrollment")
    public String dropEnrollment(@RequestParam("enrollmentId") Long enrollmentId,
                                 RedirectAttributes redirectAttributes) {
        try {
            profileService.dropEnrollment(enrollmentId);
            redirectAttributes.addFlashAttribute("success", "Course dropped successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/profile";
    }
}

