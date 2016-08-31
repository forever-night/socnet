package socnet.dao.interfaces;

import socnet.entities.Profile;

import java.util.List;
import java.util.Map;


public interface ProfileDao {
    Profile find(int id);
    
    Profile findByLogin(String login);

    List<Profile> findAll();
    
    Map<String, Profile> findAllLikeLogin(String login);

    Integer persist(Profile profile);

    Profile update(Profile profile);
    
    Profile update(Profile profile, String login);

    void remove(Integer id);

    void remove(Profile profile);
}
