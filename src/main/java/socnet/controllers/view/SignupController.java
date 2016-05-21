package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by anna on 01/05/16.
 */
@Controller
public class SignupController {
    @RequestMapping(value = {"/sign_up"}, method = RequestMethod.GET)
    public String signUp() {
        return "sign_up";
    }
}
