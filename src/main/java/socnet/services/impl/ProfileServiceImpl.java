package socnet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.ProfileDao;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;

import java.util.*;


@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileDao profileDao;
    
    @Autowired
    private ProfileMapper profileMapper;
    
    public ProfileServiceImpl() {}
    
    public ProfileServiceImpl(ProfileDao profileDao, ProfileMapper profileMapper) {
        this.profileDao = profileDao;
        this.profileMapper = profileMapper;
    }
    
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
    public Map<String, Profile> findAllLikeLogin(String login) {
        return profileDao.findAllLikeLogin(login);
    }
    
    @Override
    public List<ProfileDto> findFollowersWithLogin(Profile profile) {
        Map<String, Profile> followers = profileDao.findFollowersWithLogin(profile);
        
        if (followers == null)
            return null;
        
        return stringProfileMapToDtoList(followers);
    }
    
    @Override
    public List<ProfileDto> findFollowersWithLogin(String login) {
        Map<String, Profile> followers = profileDao.findFollowersWithLogin(login);
    
        if (followers == null)
            return null;
    
        return stringProfileMapToDtoList(followers);
    }
    
    @Override
    public List<ProfileDto> findFollowingWithLogin(Profile profile) {
        Map<String, Profile> following = profileDao.findFollowingWithLogin(profile);
        
        if (following == null)
            return null;
        
        return stringProfileMapToDtoList(following);
    }
    
    @Override
    public List<ProfileDto> findFollowingWithLogin(String login) {
        Map<String, Profile> following = profileDao.findFollowingWithLogin(login);
    
        if (following == null)
            return null;
    
        return stringProfileMapToDtoList(following);
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
    @Transactional
    public Profile addFollower(Profile owner, Profile follower) {
        Set<Profile> oldFollowers = profileDao.findFollowers(owner);
        
        if (oldFollowers == null)
            oldFollowers = new HashSet<>();
        
        oldFollowers.add(follower);
        owner.setFollowers(oldFollowers);
        
        owner = profileDao.updateFollowers(owner);
        return owner;
    }
    
    @Override
    @Transactional
    public Profile removeFollower(Profile owner, Profile follower) {
        Set<Profile> oldFollowers = profileDao.findFollowers(owner);
        
        if (oldFollowers == null)
            return owner;
        
        oldFollowers.remove(follower);
        owner.setFollowers(oldFollowers);
    
        owner = profileDao.updateFollowers(owner);
        return owner;
    }
    
    @Override
    public void remove(Integer id) {
        Profile profile = new Profile();
        profile.setId(id);
        
        profileDao.remove(profile);
    }

    @Override
    public void remove(Profile profile) {
        profileDao.remove(profile);
    }
    
    private List<ProfileDto> stringProfileMapToDtoList(Map<String, Profile> map) {
        List<ProfileDto> dtoList = new ArrayList<>();
        
        for (Map.Entry<String, Profile> entry : map.entrySet()) {
            ProfileDto profileDto = profileMapper.asProfileDto(entry.getValue());
            profileDto.setLogin(entry.getKey());
            dtoList.add(profileDto);
        }
        
        return dtoList;
    }
}
