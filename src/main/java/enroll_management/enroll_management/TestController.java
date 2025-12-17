package enroll_management.enroll_management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/")
    public String signin(){
        return "auth/SignIn";
    }
}
