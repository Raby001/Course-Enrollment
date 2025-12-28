package enroll_management.enroll_management.controllers.auth;

import enroll_management.enroll_management.Entities.Role;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.auth.SignupRequest;
import enroll_management.enroll_management.enums.RoleName;
import enroll_management.enroll_management.enums.UserStatus;
import enroll_management.enroll_management.repositories.RoleRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Show login page (with empty signup form)
    @GetMapping("/login")
    public String loginPage(Model model) {
        // ALWAYS add signupRequest to avoid Thymeleaf errors
        if (!model.containsAttribute("signupRequest")) {
            model.addAttribute("signupRequest", new SignupRequest());
        }

        return "auth/LoginForm";
    }

    // Handle signup
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // 1. Validate password match
        if (!signupRequest.isPasswordMatch()) {
            bindingResult.rejectValue("confirmPassword", "match", "Passwords do not match");
        }

        // 2. If validation fails, stay on the same page
        if (bindingResult.hasErrors()) {
            model.addAttribute("showSignup", true);
            return "auth/LoginForm";
        }

        // 3. Check if username or email already exists
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            model.addAttribute("usernameExists", true);
            model.addAttribute("showSignup", true);
            return "auth/LoginForm";
        }
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            model.addAttribute("emailExists", true);
            model.addAttribute("showSignup", true);
            return "auth/LoginForm";
        }

        // 4. Get STUDENT role
        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new RuntimeException("STUDENT role not found in DB!"));

        // 5. Hash password and create user
        String hashedPassword = passwordEncoder.encode(signupRequest.getPassword());
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getUsername());
        user.setLastName("");    
        user.setEmail(signupRequest.getEmail());
        user.setPasswordHash(hashedPassword);
        user.setDob(LocalDate.now().minusYears(18));
        user.setRole(studentRole);
        user.setStatus(UserStatus.ACTIVE);

        // 6. Save user
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Account created! Please log in.");
        return "redirect:/login";
    }
}