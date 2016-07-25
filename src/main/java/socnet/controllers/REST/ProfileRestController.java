package socnet.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import socnet.entities.Profile;
import socnet.services.interfaces.ProfileService;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {
    @Autowired
    ProfileService profileService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Profile> getProfileList() {
        return profileService.findAll();
    }
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Profile getProfileByLogin(@PathVariable String login) {
        if (login != null && !login.isEmpty())
            return profileService.findByLogin(login);
        else
            return null;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Integer> update(@RequestBody Profile profile, @PathVariable int id) {
        if (profile == null || id != profile.getId())
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        else {
            profile = profileService.update(profile);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/profile/{id}").buildAndExpand(id).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            return new ResponseEntity<Integer>(profile.getId(), headers, HttpStatus.OK);
        }
    }
}
