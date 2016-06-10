package socnet.beans.interfaces;

import socnet.entities.Profile;

import java.util.List;

/**
 * Created by anna on 01/05/16.
 */
public interface ProfileBean {
    Profile find(int id);

    List<Profile> findAll();

    Profile create(int accountId);

    Profile update(Profile profile);
}
