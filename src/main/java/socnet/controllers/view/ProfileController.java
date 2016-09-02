package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;
import socnet.util.Global;


@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    
    @Autowired
    UserService userService;

    public ProfileController() {}

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String profile() {
        String currentLogin = userService.getCurrentLogin();
        
        if (currentLogin == null)
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        return "profile";
    }
    
    @RequestMapping(value = "/{profileLogin}", method = RequestMethod.GET)
    public String profile(@PathVariable String profileLogin, Model model) {
        String currentLogin = userService.getCurrentLogin();
        
        if (profileLogin == null || profileLogin.isEmpty())
            profileLogin = currentLogin;
            
        if (currentLogin == null)
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        model.addAttribute("profileLogin", profileLogin);
        return "profile";
    }
}