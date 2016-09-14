package socnet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.ProfileDao;
import socnet.dto.ProfileDto;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;
import socnet.util.Global;

import java.util.*;


@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileDao profileDao;
    
    @Autowired
    private ProfileMapper profileMapper;
    
    @Autowired
    private UserService userService;
    
    public ProfileServiceImpl() {}
    
    public ProfileServiceImpl(ProfileDao profileDao, ProfileMapper profileMapper, UserService userService) {
        this.profileDao = profileDao;
        this.profileMapper = profileMapper;
        this.userService = userService;
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
    @Transactional
    public ProfileDto findDtoByLogin(String login) {
        Profile profile = profileDao.findByLogin(login);
        ProfileDto profileDto = profileMapper.asProfileDto(profile);
        
        String currentLogin = userService.getCurrentLogin();
        Profile currentProfile;
        
        if (currentLogin != null && !currentLogin.equals("anonymousUser")) {
            currentProfile = profileDao.findByLogin(currentLogin);
    
            boolean isFollowing = profileDao.checkIfFollows(profile, currentProfile);
            profileDto.setFollowing(isFollowing);
        } else
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        return profileDto;
    }
    
    @Override
    public List<Profile> findAll() {
        return profileDao.findAll();
    }
    
    @Override
    public List<ProfileDto> findAllLike(String searchPattern) {
        Map<String, Profile> loginProfileMap = profileDao.findAllLike(searchPattern);
        
        if (loginProfileMap == null)
            return null;
    
        
        List<Integer> idList = getIdListFromLoginProfileMap(loginProfileMap);
        
        if (!idList.isEmpty()) {
            List<Integer> followedIdList = pickFollowedByCurrentProfile(idList);
    
            if (followedIdList != null && !followedIdList.isEmpty())
                return stringProfileMapToDtoList(loginProfileMap, followedIdList);
        }
        
        return stringProfileMapToDtoList(loginProfileMap);
    }
    
    @Override
    @Transactional
    public boolean checkIfFollows(String owner, String follower) {
        Profile profileOwner = findByLogin(owner);
        Profile profileFollower = findByLogin(follower);
        
        return profileDao.checkIfFollows(profileOwner, profileFollower);
    }
    
    @Override
    public List<Integer> pickFollowedByCurrentProfile(List<Integer> profileIdList) {
        String currentLogin = userService.getCurrentLogin();
    
        if (currentLogin == null || currentLogin.isEmpty())
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
    
        Profile currentProfile = profileDao.findByLogin(currentLogin);
    
        return profileDao.pickFollowed(currentProfile, profileIdList.toArray(new Integer[]{}));
    }
    
    @Override
    public List<ProfileDto> findFollowersWithLogin(Profile profile) {
        Map<String, Profile> loginProfileMap = profileDao.findFollowersWithLogin(profile);
    
        if (loginProfileMap == null)
            return null;
    
        
        List<Integer> idList = getIdListFromLoginProfileMap(loginProfileMap);
    
        if (!idList.isEmpty()) {
            List<Integer> followedIdList = pickFollowedByCurrentProfile(idList);
        
            if (followedIdList != null && !followedIdList.isEmpty())
                return stringProfileMapToDtoList(loginProfileMap, followedIdList);
        }
    
        return stringProfileMapToDtoList(loginProfileMap);
    }
    
    @Override
    public List<ProfileDto> findFollowersWithLogin(String login) {
        Profile profile = profileDao.findByLogin(login);
        
        return findFollowersWithLogin(profile);
    }
    
    @Override
    public List<ProfileDto> findFollowingWithLogin(Profile profile) {
        Map<String, Profile> loginProfileMap = profileDao.findFollowingWithLogin(profile);
        
        if (loginProfileMap == null)
            return null;
    
        
        List<Integer> idList = getIdListFromLoginProfileMap(loginProfileMap);
    
        if (!idList.isEmpty()) {
            List<Integer> followedIdList = pickFollowedByCurrentProfile(idList);
        
            if (followedIdList != null && !followedIdList.isEmpty())
                return stringProfileMapToDtoList(loginProfileMap, followedIdList);
        }
    
        return stringProfileMapToDtoList(loginProfileMap);
    }
    
    @Override
    public List<ProfileDto> findFollowingWithLogin(String login) {
        Profile profile = profileDao.findByLogin(login);
    
        return findFollowingWithLogin(profile);
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
    
    private List<Integer> getIdListFromLoginProfileMap(Map<String, Profile> loginProfileMap) {
        List<Integer> idList = new ArrayList<>();
    
        for (Map.Entry<String, Profile> entry : loginProfileMap.entrySet()) {
            int id = entry.getValue().getId();
            idList.add(id);
        }
        
        return idList;
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
    
    private List<ProfileDto> stringProfileMapToDtoList(Map<String, Profile> map, List<Integer> followedIds) {
        List<ProfileDto> dtoList = new ArrayList<>();
    
        for (Map.Entry<String, Profile> entry : map.entrySet()) {
            ProfileDto profileDto = profileMapper.asProfileDto(entry.getValue());
            profileDto.setLogin(entry.getKey());
            
            boolean isFollowing = followedIds.contains(entry.getValue().getId());
            profileDto.setFollowing(isFollowing);
            
            dtoList.add(profileDto);
        }
    
        return dtoList;
    }
}
