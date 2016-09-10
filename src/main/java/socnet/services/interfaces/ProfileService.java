package socnet.services.interfaces;

import socnet.dto.ProfileDto;
import socnet.entities.Profile;

import java.util.List;
import java.util.Map;


public interface ProfileService {
    Profile find(int id);
    
    Profile findByLogin(String login);

    List<Profile> findAll();
    
    Map<String, Profile> findAllLikeLogin(String login);
    
    List<ProfileDto> findFollowersWithLogin(Profile profile);
    
    List<ProfileDto> findFollowersWithLogin(String login);
    
    List<ProfileDto> findFollowingWithLogin(Profile profile);
    
    List<ProfileDto> findFollowingWithLogin(String login);

    Profile create(int accountId);

    /**
     * Does not update collections.
     * */
    Profile update(Profile profile);
    
    /**
     * Does not update collections.
     * */
    Profile update(Profile profile, String login);
    
    Profile addFollower(Profile owner, Profile follower);
    
    Profile removeFollower(Profile owner, Profile follower);

    void remove(Integer id);

    void remove(Profile profile);
}
