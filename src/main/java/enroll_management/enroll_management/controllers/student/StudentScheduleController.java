package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.Entities.Schedule;
import enroll_management.enroll_management.services.student.StudentScheduleService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentScheduleController {

    @Autowired
    private StudentScheduleService scheduleService;

    @GetMapping("/student/schedules")
    public String viewSchedule(Model model) {
        List<Schedule> schedules = scheduleService.getStudentSchedule();
        
        // Group by day
        Map<String, List<Schedule>> scheduleMap = schedules.stream()
            .collect(Collectors.groupingBy(s -> s.getDayOfWeek().name()));
        
        model.addAttribute("scheduleMap", scheduleMap);
        model.addAttribute("schedules", schedules);
        return "student/schedule/schedule";
    }
}