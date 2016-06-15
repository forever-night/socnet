package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.entities.Account;


@Controller
@RequestMapping("/account")
public class AccountController {
    @RequestMapping(method = RequestMethod.GET)
    public String account() {
        return "account";
    }
}
