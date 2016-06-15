package socnet.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import socnet.beans.interfaces.ProfileBean;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Profile;

import java.util.List;


@Component
public class ProfileBeanImpl implements ProfileBean {
    @Autowired
    private ProfileDao profileDao;

    @Override
    public Profile find(int id) {
        return profileDao.find(id);
    }

    @Override
    public List<Profile> findAll() {
        return profileDao.findAll();
    }

    @Override
    public Profile create(int accountId) {
        Profile profile = new Profile();
        profile.setId(accountId);

        profileDao.persist(profile);

        return profile;
    }

    @Override
    public Profile update(Profile profile) {
        return profileDao.update(profile);
    }

    @Override
    public void remove(Integer id) {
        profileDao.remove(id);
    }

    @Override
    public void remove(Profile profile) {
        profileDao.remove(profile);
    }
}
