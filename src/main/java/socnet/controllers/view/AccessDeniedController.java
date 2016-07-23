package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/403")
public class AccessDeniedController {
    @RequestMapping(method = RequestMethod.GET)
    public String accessDenied() {
        return "403";
    }
}
