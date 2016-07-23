package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/signup")
public class SignupController {
    @RequestMapping(method = RequestMethod.GET)
    public String signUp() {
        return "signup";
    }
}
