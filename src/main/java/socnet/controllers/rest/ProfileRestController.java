package socnet.controllers.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import socnet.controllers.EmptyRequestException;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;
import socnet.util.Global;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    
    public ProfileRestController(ProfileService profileService, ProfileMapper profileMapper, UserService userService) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProfileDto> getProfileList() {
        List<ProfileDto> resultDto = new ArrayList<>();
        List<Profile> resultEntity = profileService.findAll();
        
        for (Profile p : resultEntity)
            resultDto.add(profileMapper.asProfileDto(p));
        
        return resultDto;
    }
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET)
    public ProfileDto getProfileByLogin(@PathVariable String login) {
        return profileMapper.asProfileDto(profileService.findByLogin(login));
    }
    
    @RequestMapping(params = "search", method = RequestMethod.GET)
    public List<ProfileDto> getProfilesLikeLogin(@RequestParam(name = "search") String login)
            throws EmptyRequestException {
        if (login.isEmpty())
            throw new EmptyRequestException();
        
        Map<String, Profile> entityMap = profileService.findAllLikeLogin(login);
        
        if (entityMap == null || entityMap.isEmpty())
            throw new NoResultException();
        
        
        List<ProfileDto> dtoList = new ArrayList<>();
        ProfileDto temp;
        
        for (Map.Entry<String, Profile> entry : entityMap.entrySet()) {
            temp = profileMapper.asProfileDto(entry.getValue());
            temp.setLogin(entry.getKey());
            dtoList.add(temp);
        }
        
        return dtoList;
    }
    
    @RequestMapping(path = "/{login}/followers", method = RequestMethod.GET)
    public List<ProfileDto> getFollowersByLogin(@PathVariable String login) throws EmptyRequestException {
        if (login == null || login.isEmpty())
            throw new EmptyRequestException();
        
        
        String currentLogin = userService.getCurrentLogin();
    
        if (currentLogin == null)
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        
        return profileService.findFollowersWithLogin(login);
    }

    @RequestMapping(path = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody ProfileDto profileDto, @PathVariable String login)
            throws EmptyRequestException {
        if (profileDto == null)
            throw new EmptyRequestException();
        
        
        String currentLogin = userService.getCurrentLogin();
        
        if (currentLogin == null || !login.equals(currentLogin))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        
        Profile profile = profileMapper.asProfile(profileDto);
        profileService.update(profile, login);
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(path = "/{loginToFollow}", params = "follow", method = RequestMethod.PUT)
    public ResponseEntity follow(@PathVariable() String loginToFollow) {
        String currentLogin = userService.getCurrentLogin();
        
        if (currentLogin == null)
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        Profile toFollow = profileService.findByLogin(loginToFollow);
        Profile follower = profileService.findByLogin(currentLogin);
        
        profileService.addFollower(toFollow, follower);
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
