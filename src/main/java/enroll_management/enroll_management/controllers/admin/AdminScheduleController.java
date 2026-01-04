package enroll_management.enroll_management.controllers.admin;

import enroll_management.enroll_management.dto.ScheduleDTO;
import enroll_management.enroll_management.services.ClassroomService;
import enroll_management.enroll_management.services.ScheduleService;
import enroll_management.enroll_management.services.admin.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/schedules")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final ScheduleService scheduleService;
    private final CourseService courseService;
    private final ClassroomService classroomService;

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("schedules", scheduleService.findAll());
        return "admin/schedule/list";
    }

    // CREATE FORM (MUST BE ABOVE {id})
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("schedule", new ScheduleDTO());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("classrooms", classroomService.findAll());
        return "admin/schedule/form";
    }

    // CREATE SUBMIT
    @PostMapping
    public String create(@Valid @ModelAttribute("schedule") ScheduleDTO dto,
                        BindingResult result,
                        Model model) {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("classrooms", classroomService.findAll());
            return "admin/schedule/form";
        }

        try {
            scheduleService.create(dto);
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("classrooms", classroomService.findAll());
            return "admin/schedule/form";
        }

        return "redirect:/admin/schedules";
    }


    // EDIT FORM
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getDTOById(id));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("classrooms", classroomService.findAll());
        return "admin/schedule/form";
    }

    // UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                        @Valid @ModelAttribute("schedule") ScheduleDTO dto,
                        BindingResult result,
                        Model model) {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("classrooms", classroomService.findAll());
            return "admin/schedule/form";
        }

        try {
            scheduleService.update(id, dto);
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("classrooms", classroomService.findAll());
            return "admin/schedule/form";
        }

        return "redirect:/admin/schedules";
    }


    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        scheduleService.delete(id);
        return "redirect:/admin/schedules";
    }
}
