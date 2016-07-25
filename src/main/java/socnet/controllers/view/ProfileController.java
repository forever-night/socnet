package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;


@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    
    @Autowired
    UserService userService;

    public ProfileController() {}

    /**
     * Used in tests for mocks.
     * */
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String profile(Model model) {
        String login = userService.getCurrentLogin();
        
        if (login == null)
            return "403";
        else {
            model.addAttribute("login", login);
            return "profile";
        }
    }
    
    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public String profile(@PathVariable String login, Model model) {
        String currentLogin = userService.getCurrentLogin();
        
        if (login == null || login.isEmpty())
            login = currentLogin;
            
        if (currentLogin == null)
            return "403";
        else {
            model.addAttribute("login", login);
            return "profile";
        }
    }
}