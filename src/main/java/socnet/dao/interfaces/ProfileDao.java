package socnet.dao.interfaces;

import socnet.entities.Profile;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ProfileDao {
    Profile find(int id);
    
    Profile findByLogin(String login);

    List<Profile> findAll();
    
    Map<String, Profile> findAllLikeLogin(String login);
    
    Set<Profile> findFollowers(Profile profile);

    Set<Profile> findFollowers(String login);
    
    Map<String, Profile> findFollowersWithLogin(Profile profile);
    
    Map<String, Profile> findFollowersWithLogin(String login);
    
    Set<Profile> findFollowing(Profile profile);
    
    Set<Profile> findFollowing(String login);
    
    Map<String, Profile> findFollowingWithLogin(Profile profile);
    
    Map<String, Profile> findFollowingWithLogin(String login);

    Integer persist(Profile profile);

    Profile update(Profile profile);
    
    Profile update(Profile profile, String login);
    
    Profile updateFollowers(Profile profile);
    
    Profile updateFollowers(Profile profile, String login);

    void remove(Profile profile);
}
