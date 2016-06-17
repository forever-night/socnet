package socnet.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socnet.beans.interfaces.ProfileBean;
import socnet.entities.Profile;


@Controller
@RequestMapping(value = "/settings")
public class SettingsController {
    @Autowired
    ProfileBean profileBean;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String settings(@PathVariable int id, Model model) {
        Profile profile = profileBean.find(id);

        if (profile != null) {
            model.addAttribute("profileId", id);
            return "settings";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }
}
