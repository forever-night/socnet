package socnet.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socnet.beans.interfaces.ProfileBean;
import socnet.entities.Profile;

import java.util.List;


@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {
    @Autowired
    ProfileBean profileBean;

    @RequestMapping(method = RequestMethod.GET)
    public List<Profile> getProfileList() {
        return profileBean.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile getProfileById(@PathVariable int id) {
        if (id > 0)
            return profileBean.find(id);
        else
            return null;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Profile> update(@RequestBody Profile profile, @PathVariable int id) {
        if (id == profile.getId()) {
            profile = profileBean.update(profile);

            // TODO put profile

            return new ResponseEntity<Profile>(profile, HttpStatus.OK);
        } else
            return new ResponseEntity<Profile>(profile, HttpStatus.NOT_ACCEPTABLE);
    }
}
