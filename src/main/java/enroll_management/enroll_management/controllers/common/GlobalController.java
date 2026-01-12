package enroll_management.enroll_management.controllers.common;

import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.services.student.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private StudentProfileService profileService;

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            try {
                return profileService.getCurrentUser();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}