package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.services.interfaces.UserService;


@Controller
public class IndexController {
    @Autowired
    UserService userService;
    
    public IndexController() {}
    
    /**
     * Used in tests for mocks.
     * */
    public IndexController(UserService userService) {
        this.userService = userService;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        String login = userService.getCurrentLogin();
        
        if (login == null)
            return "redirect:login";
        else
            return "redirect:profile";
    }
}
