package socnet.beans.interfaces;

import socnet.entities.Profile;

import java.util.List;


public interface ProfileBean {
    Profile find(int id);

    List<Profile> findAll();

    Profile create(int accountId);

    Profile update(Profile profile);

    void remove(Integer id);

    void remove(Profile profile);
}
