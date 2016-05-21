package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by anna on 03/05/16.
 */
@Controller
public class SettingsController {
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings() {
        return "settings";
    }
}
