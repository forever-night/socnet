package socnet.dao.interfaces;

import socnet.entities.Profile;

import java.util.List;

/**
 * Created by anna on 18/05/16.
 */
public interface ProfileDao {
    Profile find(int id);

    List<Profile> findAll();

    Integer persist(Profile profile);

    Profile update(Profile profile);
}
