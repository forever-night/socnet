package socnet.dao.interfaces;

import socnet.entities.Profile;

import java.util.List;


public interface ProfileDao {
    Profile find(int id);
    
    Profile findByLogin(String login);

    List<Profile> findAll();

    Integer persist(Profile profile);

    Profile update(Profile profile);
    
    Profile update(Profile profile, String login);

    void remove(Integer id);

    void remove(Profile profile);
}
