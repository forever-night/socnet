package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by anna on 23/04/16.
 */
@Controller
public class IndexController {
    @RequestMapping({"/", "index"})
    public String index() {
        return "index";
    }
}
