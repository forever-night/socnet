package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.services.interfaces.UserService;
import socnet.util.Global;


@Controller
@RequestMapping(path = "/followers")
public class FollowersController {
    @Autowired
    UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String followers() {
        return "followers";
    }
    
    @RequestMapping(value = "/{profileLogin}", method = RequestMethod.GET)
    public String followers(@PathVariable String profileLogin, Model model) {
        String currentLogin = userService.getCurrentLogin();
        
        if (profileLogin == null || profileLogin.isEmpty())
            profileLogin = currentLogin;
        
        if (currentLogin == null)
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        model.addAttribute("profileLogin", profileLogin);
        return "followers";
    }
}
