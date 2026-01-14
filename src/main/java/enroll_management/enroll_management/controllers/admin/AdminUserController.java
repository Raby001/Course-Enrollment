package enroll_management.enroll_management.controllers.admin;

import enroll_management.enroll_management.dto.admin.UserCreateDto;
import enroll_management.enroll_management.services.admin.UserService;
import enroll_management.enroll_management.repositories.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminUserController(UserService userService,
                                RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("activePage", "users");
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new UserCreateDto());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("statuses", enroll_management.enroll_management.enums.UserStatus.values());
        return "admin/user-create";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute UserCreateDto dto) {
        userService.createUser(dto);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/status")
    public String toggleStatus(@PathVariable("id") Long id) {
        userService.toggleUserStatus(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
