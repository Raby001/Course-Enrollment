package enroll_management.enroll_management.controllers.admin;

import enroll_management.enroll_management.dto.admin.ClassroomDTO;
import enroll_management.enroll_management.services.admin.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/classrooms")
@RequiredArgsConstructor
public class AdminClassroomController {

    private final ClassroomService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classrooms", service.findAll());
        return "admin/classroom/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("classroom", new ClassroomDTO());
        return "admin/classroom/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("classroom") ClassroomDTO dto,
                         BindingResult result) {
        if (result.hasErrors()) return "admin/classroom/form";
        service.create(dto);
        return "redirect:/admin/classrooms";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var c = service.findById(id);
        ClassroomDTO dto = new ClassroomDTO();
        dto.setId(c.getId());
        dto.setBuilding(c.getBuilding());
        dto.setRoomNumber(c.getRoomNumber());
        dto.setCapacity(c.getCapacity());
        dto.setHasProjector(c.getHasProjector());
        dto.setHasComputer(c.getHasComputer());
        dto.setStatus(c.getStatus());

        model.addAttribute("classroom", dto);
        return "admin/classroom/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("classroom") ClassroomDTO dto,
                         BindingResult result) {
        if (result.hasErrors()) return "admin/classroom/form";
        service.update(id, dto);
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/classrooms";
    }
}
