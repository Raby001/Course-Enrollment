package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.admin.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    // Constructor Injection (best practice)
    public StudentController(
            CourseService courseService,
            UserRepository userRepository,
            EnrollmentRepository enrollmentRepository) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/student/home")
    public String studentHome(Authentication auth, Model model) {
        String username = auth.getName();
        
        // Get current student
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found: " + username));
        
        // Get all courses
        List<CourseDto> courses = courseService.getAllCourses();
        
        // Add enrollment status to each course
        for (CourseDto course : courses) {
            boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(
                student.getId(), 
                course.getId()
            );
            course.setEnrolled(isEnrolled);
        }

        model.addAttribute("courses", courses);
        return "student/home";
    }
}

