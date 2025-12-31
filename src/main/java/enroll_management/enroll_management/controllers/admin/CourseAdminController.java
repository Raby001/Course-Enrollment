package enroll_management.enroll_management.controllers.admin;

import enroll_management.enroll_management.dto.admin.CourseCreateUpdateDto;
import enroll_management.enroll_management.services.admin.CourseService;
import jakarta.validation.Valid;
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

    // =========================
    // VIEW ALL COURSES
    // =========================
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses";
    }

    // =========================
    // SHOW CREATE FORM
    // =========================
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new CourseCreateUpdateDto());
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
