package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by anna on 02/05/16.
 */
@Controller
public class AccountController {
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account() {
        return "account";
    }
}
