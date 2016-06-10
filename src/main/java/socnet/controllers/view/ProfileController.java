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
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileBean profileBean;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String profile(@PathVariable int id, Model model) {
        Profile profile = profileBean.find(id);

        if (profile != null)
            model.addAttribute("profileId", id);

        return "profile";
    }
}