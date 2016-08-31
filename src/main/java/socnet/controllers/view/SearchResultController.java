package socnet.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "search")
public class SearchResultController {
    @RequestMapping(method = RequestMethod.GET)
    public String get(@RequestParam(required = false) String query) {
        return "searchResult";
    }
}
