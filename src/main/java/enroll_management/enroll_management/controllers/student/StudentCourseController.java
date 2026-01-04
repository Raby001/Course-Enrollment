package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.enums.CourseStatus;
import enroll_management.enroll_management.exception.ResourceNotFoundException;
import enroll_management.enroll_management.repositories.CourseRepository;
import enroll_management.enroll_management.services.admin.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student/courses")
@PreAuthorize("hasRole('STUDENT')")
public class StudentCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    // =========================
    // VIEW ALL ACTIVE COURSES
    // =========================
   @GetMapping
public String listCourses(
        @RequestParam(name = "keyword" ,defaultValue = "") String keyword,
        @RequestParam(name = "page" ,defaultValue = "0") int page,
        Model model) {

    Page<Course> courses = courseRepository
            .findByStatusAndCourseNameContainingIgnoreCase(
                    CourseStatus.ACTIVE,
                    keyword,
                    PageRequest.of(page, 6)
            );

    model.addAttribute("courses", courses);
    model.addAttribute("keyword", keyword);

    return "student/courses";
}

    // =========================
    // VIEW COURSE DETAILS (ACTIVE ONLY)
    // =========================
    @GetMapping("/{id}")
    public String viewCourseDetail(
            @PathVariable("id") Long id,
            Model model) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id));

        // 🚫 Block non-active courses
        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new AccessDeniedException("Course not available");
        }

        model.addAttribute(
                "course",
                courseService.getCourseById(id)
        );

        return "student/course-detail";
    }
}
