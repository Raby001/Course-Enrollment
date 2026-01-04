package enroll_management.enroll_management.controllers.admin;

import enroll_management.enroll_management.dto.admin.CourseCreateUpdateDto;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.enums.CourseStatus;
import enroll_management.enroll_management.enums.RoleName;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.admin.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/courses")
@PreAuthorize("hasRole('ADMIN')")
public class CourseAdminController {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private UserRepository userRepository;

    // =========================
    // VIEW ALL COURSES
    // =========================
   @GetMapping
public String listCourses(
        @RequestParam(name = "keyword", defaultValue = "") String keyword,
        @RequestParam(name = "page" ,defaultValue = "0") int page,
        Model model) {

    Page<CourseDto> courses = courseService.searchCourses(keyword, page);

    model.addAttribute("courses", courses);
    model.addAttribute("keyword", keyword);

    return "admin/courses";
}


    // =========================
    // SHOW CREATE FORM
    // =========================
  @GetMapping("/create")
public String showCreateForm(Model model) {
    model.addAttribute("course", new CourseCreateUpdateDto());
    // fetch lecturers directly
    model.addAttribute(
        "lecturers",
        userRepository.findByRole_Name(RoleName.LECTURER)
    );
    return "admin/course-create";
}

    // =========================
    // HANDLE CREATE
    // =========================
    @PostMapping("/create")
    public String createCourse(
            @Valid @ModelAttribute("course") CourseCreateUpdateDto courseDto) {

        courseService.createCourse(courseDto);
        return "redirect:/admin/courses";
    }

    // =========================
    // SHOW EDIT FORM
    // =========================
   @GetMapping("/edit/{id}")
public String showEditForm(
        @PathVariable("id") Long id,
        Model model) {

    model.addAttribute("course", courseService.getCourseById(id));
    model.addAttribute("statuses", CourseStatus.values());
    return "admin/course-edit";
}


    // =========================
    // HANDLE UPDATE
    // =========================
    @PostMapping("/edit/{id}")
    public String updateCourse(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("course") CourseCreateUpdateDto courseDto) {

        courseService.updateCourse(id, courseDto);
        return "redirect:/admin/courses";
    }

    // =========================
    // DELETE COURSE
    // =========================
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {

        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }
}
