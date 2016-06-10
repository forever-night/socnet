package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/account")
public class AccountController {
    @RequestMapping(method = RequestMethod.GET)
    public String account() {
        return "account";
    }
}
