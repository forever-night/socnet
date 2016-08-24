package socnet.services.interfaces;

import socnet.entities.Profile;

import java.util.List;


public interface ProfileService {
    Profile find(int id);
    
    Profile findByLogin(String login);

    List<Profile> findAll();

    Profile create(int accountId);

    Profile update(Profile profile);
    
    Profile update(Profile profile, String login);

    void remove(Integer id);

    void remove(Profile profile);
}
