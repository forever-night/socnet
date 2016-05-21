package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by anna on 03/05/16.
 */
@Controller
public class CommunityController {
    @RequestMapping(value = "/communities", method = RequestMethod.GET)
    public String communities() {
        return "communities";
    }
}
