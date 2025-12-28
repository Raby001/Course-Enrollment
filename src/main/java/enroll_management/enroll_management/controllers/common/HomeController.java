package enroll_management.enroll_management.controllers.common;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String dashboardRedirect(Authentication auth) {
        String role = auth.getAuthorities().iterator().next().getAuthority(); // e.g., "ROLE_ADMIN"

        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/student/home";
        }
    }
}
