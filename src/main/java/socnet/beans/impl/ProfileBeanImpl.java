package socnet.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import socnet.beans.interfaces.AccountBean;
import socnet.beans.interfaces.ProfileBean;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Account;
import socnet.entities.Profile;

import java.util.List;

/**
 * Created by anna on 01/05/16.
 */
@Component
public class ProfileBeanImpl implements ProfileBean {
    @Autowired
    private ProfileDao profileDao;

    public Profile find(int id) {
        return profileDao.find(id);
    }

    public List<Profile> findAll() {
        return profileDao.findAll();
    }

    public Profile create(int accountId) {
        Profile profile = new Profile();
        profile.setId(accountId);

        profileDao.persist(profile);

        return profile;
    }

    public Profile update(Profile profile) {
        return profileDao.update(profile);
    }
}
