package enroll_management.enroll_management.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.services.common.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AdminController {

    @Autowired
    private UserDetailService userService;

    @Autowired
    private enroll_management.enroll_management.services.admin.CourseService courseService;

    @Autowired
    private enroll_management.enroll_management.services.admin.EnrollmentService enrollmentService;
    
        @GetMapping("/admin/adminDashboard")
        public String adminDashboard(Model model){
            model.addAttribute("activePage", "adminDashboard");
            model.addAttribute("title", "Admin Dashboard");

            // Count statistics
            model.addAttribute("totalUsers", userService.getTotalUsers());
            model.addAttribute("totalStudents", userService.getTotalStudents());
            model.addAttribute("totalLecturers", userService.getTotalLecturers());
            model.addAttribute("newUsersThisWeek", userService.getNewUsersThisWeek());
            model.addAttribute("pendingRequests", enrollmentService.getPendingEnrollmentsCount());
            model.addAttribute("resolvedToday", enrollmentService.getResolvedToday());

            // Course statistics (all are now numbers)
            model.addAttribute("totalCourses", courseService.getTotalCourses());
            model.addAttribute("activeCourses", courseService.getActiveCoursesCount());
            model.addAttribute("draftCourses", courseService.getDraftCourses());
            model.addAttribute("pendingCourses", courseService.getPendingCourses());


            // Enrollment statistics
            Enrollment latestEnrollment = enrollmentService.getLatestEnrollment();
            model.addAttribute("lastEnrollmentDate", 
                latestEnrollment != null ? latestEnrollment.getEnrollmentDate() : null);

            return "admin/adminDashboard";
        }


        @GetMapping("/classrooms")
        public String classroomList(Model model) {
            model.addAttribute("activePage", "classes");
            return "admin/classroom/list";
        }

        @GetMapping("/classrooms/create")
        public String classroomForm(Model model) {
            return "admin/classroom/form";
        }

        @GetMapping("/schedules")
        public String scheduleList(Model model) {
            return "admin/schedule/list";
        }

        @GetMapping("/schedules/create")
        public String scheduleForm(Model model) {
            return "admin/schedule/form";
        }
}
