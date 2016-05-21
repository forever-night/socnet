package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by anna on 23/04/16.
 */
@Controller
public class SigninController {
    @RequestMapping(value = {"/sign_in"}, method = RequestMethod.GET)
    public String signIn() {
        return "sign_in";
    }
}
