package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;


@Controller
@RequestMapping(value = "/settings")
public class SettingsController {
    @Autowired
    ProfileService profileService;

    @Autowired
    UserService userService;
    
    public SettingsController() {}
    
    /**
     * Used in tests for mocks.
     * */
    public SettingsController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String settings(Model model) {
        String login = userService.getCurrentLogin();
    
        if (login == null)
            return "403";
        else {
            model.addAttribute("login", login);
            return "settings";
        }
    }
}
