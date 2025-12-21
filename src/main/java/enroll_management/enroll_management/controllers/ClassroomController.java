package enroll_management.enroll_management.controllers;

import enroll_management.enroll_management.dto.ClassroomDTO;
import enroll_management.enroll_management.services.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classrooms", classroomService.getAll());
        return "classroom/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("classroom", new ClassroomDTO());
        return "classroom/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("classroom") ClassroomDTO dto,BindingResult result) {
        if (result.hasErrors()) {
            return "classroom/form";
        }
        classroomService.create(dto);
        return "redirect:/classrooms";
    }
}
