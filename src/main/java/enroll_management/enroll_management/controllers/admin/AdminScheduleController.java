package enroll_management.enroll_management.controllers.admin;

import enroll_management.enroll_management.dto.admin.ScheduleDTO;
import enroll_management.enroll_management.services.common.ScheduleService;
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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("schedules", scheduleService.findAll());
        return "admin/schedule/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("schedule", new ScheduleDTO());
        return "admin/schedule/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("schedule") ScheduleDTO dto,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "admin/schedule/form";
        }
        scheduleService.create(dto);
        return "redirect:/admin/schedules";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getDTOById(id));
        return "admin/schedule/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("schedule") ScheduleDTO dto,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "admin/schedule/form";
        }
        scheduleService.update(id, dto);
        return "redirect:/admin/schedules";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return "redirect:/admin/schedules";
    }
}
