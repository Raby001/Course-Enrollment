package enroll_management.enroll_management.controllers.student;

import enroll_management.enroll_management.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student/classrooms")
@RequiredArgsConstructor
public class StudentClassroomController {

    private final ClassroomService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classrooms", service.findAll());
        return "student/classroom/list";
    }
}
