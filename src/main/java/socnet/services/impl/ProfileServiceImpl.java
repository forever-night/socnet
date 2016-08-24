package socnet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Profile;
import socnet.services.interfaces.ProfileService;

import java.util.List;


@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileDao profileDao;

    @Override
    public Profile find(int id) {
        return profileDao.find(id);
    }
    
    @Override
    public Profile findByLogin(String login) {
        return profileDao.findByLogin(login);
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
    public Profile update(Profile profile, String login) {
        return profileDao.update(profile, login);
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
