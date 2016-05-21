package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import socnet.beans.interfaces.ProfileBean;
import socnet.entities.Profile;

/**
 * Created by anna on 02/05/16.
 */
@Controller
public class ProfileController {
    @Autowired
    ProfileBean profileBean;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile() {
        return "profile";
    }
}
