package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.admin.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/courses")
@PreAuthorize("hasRole('ROLE_STUDENT')") 
public class StudentCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // =========================
    // VIEW ALL COURSES WITH SEARCH
    // =========================
    @GetMapping
    public String viewAllCourses(
           @RequestParam(name = "q", required = false) String keyword,
            Authentication auth, 
            Model model) {

        String username = auth.getName();
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<CourseDto> courses = courseService.getAllCourses().stream()
                .peek(course -> {
                    boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(
                        student.getId(), course.getId()
                    );
                    course.setEnrolled(isEnrolled);
                })
                .collect(Collectors.toList());

        // Apply search filter if query exists
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchTerm = keyword.toLowerCase().trim();
            courses = courses.stream()
                    .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
            model.addAttribute("query", keyword); 
        }

        model.addAttribute("courses", courses);
        return "student/course/courses";
    }

    // =========================
    // VIEW COURSE DETAILS
    // =========================
    @GetMapping("/{id}")
    public String viewCourseDetail(
            @PathVariable("id") Long id,
            Authentication auth,
            Model model) {

        String username = auth.getName();
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CourseDto course = courseService.getCourseById(id);
        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(
            student.getId(), id
        );
        course.setEnrolled(isEnrolled);

        // Get other courses (exclude current one)
        List<CourseDto> otherCourses = courseService.getAllCourses().stream()
                .filter(c -> !c.getId().equals(id))
                .limit(3)
                .peek(c -> {
                    boolean otherEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(
                        student.getId(), c.getId()
                    );
                    c.setEnrolled(otherEnrolled);
                })
                .collect(Collectors.toList());

        model.addAttribute("course", course);
        model.addAttribute("otherCourses", otherCourses);
        return "student/course/course-details";
    }
}