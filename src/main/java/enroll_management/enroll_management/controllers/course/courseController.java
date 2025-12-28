// package enroll_management.enroll_management.controllers.course;

// import enroll_management.enroll_management.services.admin.CourseService;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;


// @Controller
// public class courseController {

//     private final CourseService courseService;

//     courseController(CourseService courseService) {
//         this.courseService = courseService;
//     }

//     @GetMapping("/courses/{id}")
//         public String course(@PathVariable Long id, Model model) {
//             model.addAttribute("course", courseService.getCourseById(id));
//             return "course/course-details";
// }
// }