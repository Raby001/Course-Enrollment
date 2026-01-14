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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // LOGIN PAGE
    // =========================
    @GetMapping("/login")
    public String loginPage(Model model) {

        if (!model.containsAttribute("signupRequest")) {
            model.addAttribute("signupRequest", new SignupRequest());
        }

        return "auth/LoginForm";
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
            BindingResult bindingResult,
            Model model) {

        if (!signupRequest.isPasswordMatch()) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "password.mismatch",
                    "Passwords do not match"
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showSignup", true);
            return "auth/LoginForm";
        }

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

        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() ->
                        new RuntimeException("STUDENT role not found in DB"));

        // =========================
        // CREATE USER
        // =========================
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getUsername());
        user.setLastName("");
        user.setEmail(signupRequest.getEmail());

        user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));

        user.setDob(LocalDate.now().minusYears(18));
        user.setRole(studentRole);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        model.addAttribute("success", "Account created! Please log in.");
        return "auth/LoginForm";
    }
}
