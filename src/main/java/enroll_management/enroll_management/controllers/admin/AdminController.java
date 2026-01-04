package enroll_management.enroll_management.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.services.common.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private enroll_management.enroll_management.services.admin.CourseService courseService;

    @Autowired
    private enroll_management.enroll_management.services.admin.EnrollmentService enrollmentService;
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("title", "Admin Dashboard");

        // Count statistics
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalStudents", userService.getTotalStudents());
        model.addAttribute("totalLecturers", userService.getTotalLecturers());
        model.addAttribute("newUsersThisWeek", userService.getNewUsersThisWeek());
        model.addAttribute("pendingRequests", enrollmentService.getPendingEnrollmentsCount());
        model.addAttribute("resolvedToday", enrollmentService.getResolvedToday());

        // Course statistics
        model.addAttribute("totalCourses", courseService.getTotalCourses());
        model.addAttribute("activeCourses", courseService.getActiveCourses());
        model.addAttribute("draftCourses", courseService.getDraftCourses());
        model.addAttribute("pendingCourses", courseService.getPendingCourses());


        // Enrollment statistics
        Enrollment latestEnrollment = enrollmentService.getLatestEnrollment();

        if (latestEnrollment != null) {
            model.addAttribute("lastEnrollmentDate", latestEnrollment.getEnrollmentDate());
        } else {
            model.addAttribute("lastEnrollmentDate", null);
        }

        return "admin/dashboard";
    }
}
