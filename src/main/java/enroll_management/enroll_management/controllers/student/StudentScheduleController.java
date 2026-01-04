package enroll_management.enroll_management.controllers.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import enroll_management.enroll_management.services.common.ScheduleService;

@Controller
@RequestMapping("/student/schedules")
@RequiredArgsConstructor
public class StudentScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("schedules", scheduleService.findAll());
        return "student/schedule/list";
    }
}
    
