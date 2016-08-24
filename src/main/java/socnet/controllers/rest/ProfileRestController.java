package socnet.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/profile")
public class ProfileRestController {
    @Autowired
    ProfileService profileService;
    
    @Autowired
    ProfileMapper profileMapper;
    
    @Autowired
    UserService userService;
    
    public ProfileRestController() {}
    
//    for testing
    public ProfileRestController(ProfileService profileService, ProfileMapper profileMapper, UserService userService) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Profile> getProfileList() {
        return profileService.findAll();
    }
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET)
    public Profile getProfileByLogin(@PathVariable String login) {
        if (login != null && !login.isEmpty())
            return profileService.findByLogin(login);
        else
            return null;
    }

    @RequestMapping(path = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody ProfileDto profileDto, @PathVariable String login) {
        if (profileDto == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else {
            String currentLogin = userService.getCurrentLogin();
            
            if (!login.equals(currentLogin))
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            
            Profile profile = profileMapper.asProfile(profileDto);
            profileService.update(profile, login);
            
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
