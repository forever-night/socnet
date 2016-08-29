package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import socnet.util.Global;


@Controller
@RequestMapping(value = "/error")
public class ErrorController {
    @RequestMapping(method = RequestMethod.GET)
    public String errorWithMessage(@RequestParam(required = false) String errorMessage, Model model) {
        if (errorMessage == null || errorMessage.isEmpty())
            errorMessage = Global.Error.UNEXPECTED.getMessage();
        
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
