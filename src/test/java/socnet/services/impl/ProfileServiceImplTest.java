package socnet.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;
import socnet.dao.interfaces.ProfileDao;
import socnet.dto.ProfileDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;

import javax.persistence.NoResultException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static socnet.util.TestUtil.*;


@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceImplTest {
    @Mock
    ProfileDao profileDaoMock;
    
    @Mock
    ProfileMapper profileMapperMock;
    
    @Mock
    UserService userServiceMock;
    
    private ProfileService service;
    
    
    @Before
    public void setUp() {
        service = new ProfileServiceImpl(profileDaoMock, profileMapperMock, userServiceMock);
    }
    
    @Test
    public void create() {
        int expectedId = 100500;
        Profile expected = new Profile();
        expected.setId(expectedId);
        
        when(profileDaoMock.persist(any()))
                .thenReturn(expected.getId());
        
        Profile actual = service.create(expectedId);
        
        assertEquals(expectedId, (int) actual.getId());
        assertEquals(expected, actual);
    }
    
    @Test
    public void findDtoByLoginExistsAndCurrentUserLoggedIn() {
        String current = "current";
        String follower = "follower";
        
        Profile profileCurrent = generateProfile();
        Profile profileFollower = generateProfile();
        
        ProfileDto expected = generateProfileDto(profileFollower);
        
        when(profileDaoMock.findByLogin(follower))
                .thenReturn(profileFollower);
        
        when(profileMapperMock.asProfileDto(profileFollower))
                .thenReturn(expected);
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(current);
        
        when(profileDaoMock.findByLogin(current))
                .thenReturn(profileCurrent);
        
        when(profileDaoMock.checkIfFollows(profileFollower, profileCurrent))
                .thenReturn(true);
        
        
        ProfileDto actual = service.findDtoByLogin(follower);
        expected.setFollowing(true);
        assertNotNull(actual);
        assertTrue(actual.isFollowing());
        assertEquals(expected, actual);
    }
    
    @Test(expected = AccessDeniedException.class)
    public void findDtoByLoginExistsAndCurrentUserNotLoggedIn() {
        String follower = "follower";
        Profile profileFollower = generateProfile();
        ProfileDto expected = generateProfileDto(profileFollower);
    
        when(profileDaoMock.findByLogin(follower))
                .thenReturn(profileFollower);
    
        when(profileMapperMock.asProfileDto(profileFollower))
                .thenReturn(expected);
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(null);
    
        service.findDtoByLogin(follower);
    }
    
    @Test(expected = NoResultException.class)
    public void findDtoByLoginNotExistsAndCurrentUserLoggedIn() {
        String current = "current";
        String follower = "follower";
    
        Profile profileCurrent = generateProfile();
    
        when(profileDaoMock.findByLogin(follower))
                .thenThrow(NoResultException.class);
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(current);
    
        when(profileDaoMock.findByLogin(current))
                .thenReturn(profileCurrent);
    
    
        service.findDtoByLogin(follower);
    }
    
    @Test
    public void checkIfFollowsIfExist() {
        String login1 = "login1";
        String login2 = "login2";
        
        Profile profile1 = generateProfile();
        Profile profile2 = generateProfile();
        
        when(profileDaoMock.findByLogin(login1))
                .thenReturn(profile1);
        
        when(profileDaoMock.findByLogin(login2))
                .thenReturn(profile2);
        
        when(profileDaoMock.checkIfFollows(profile1, profile2))
                .thenReturn(true);
        
        boolean actual = service.checkIfFollows(login1, login2);
        assertTrue(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void checkIfFollowsIfNotExists() {
        String login1 = "login1";
        String login2 = "login2";
        
        when(profileDaoMock.findByLogin(login1))
                .thenThrow(NoResultException.class);
        
        service.checkIfFollows(login1, login2);
    }
    
    @Test
    public void findFollowersWithLoginIfExist() {
        int iterations = 3;
        
        Account mainAccount = generateAccount("main");
        Profile mainProfile = generateProfile(mainAccount);
        mainProfile.setId(1);
        
        Set<Profile> followers = new HashSet<>();
        List<Integer> followedIds = new ArrayList<>();
        List<ProfileDto> expected = new ArrayList<>();
        
        Map<String, Profile> map = new HashMap<>();
        
        for(int i = 0; i < iterations; i++) {
            Account account = generateAccount("a" + i);
            Profile profile = generateProfile(account);
            profile.setId(i);
            
            followedIds.add(i);
            followers.add(profile);
            
            if (i == 0) {
                ProfileDto profileDto = generateProfileDto(profile);
                expected.add(profileDto);
            }
            
            map.put(account.getLogin(), profile);
        }
        
        
        when(profileDaoMock.findFollowersWithLogin(mainProfile))
                .thenReturn(map);
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(mainAccount.getLogin());
        
        when(profileDaoMock.findByLogin(mainAccount.getLogin()))
                .thenReturn(mainProfile);
        
        when(profileDaoMock.pickFollowed(mainProfile, followedIds.toArray(new Integer[]{})))
                .thenReturn(null);
        
        for (Profile profile : followers)
            when(profileMapperMock.asProfileDto(any()))
                .thenReturn(generateProfileDto(profile));
        
        
        List<ProfileDto> actual = service.findFollowersWithLogin(mainProfile);
        
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(followers.size(), actual.size());
        assertTrue(actual.contains(expected.get(0)));
    }
    
    @Test
    public void findFollowersWithLoginIfNoFollowers() {
        when(profileDaoMock.findFollowersWithLogin(any(Profile.class)))
                .thenReturn(null);
        
        Profile profile = generateProfile();
        profile.setId(1);
        
        List<ProfileDto> actual = service.findFollowersWithLogin(profile);
        
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findFollowersWithLoginIfUserNotExists() {
        when(profileDaoMock.findFollowersWithLogin(any(Profile.class)))
                .thenThrow(NoResultException.class);
        
        service.findFollowersWithLogin(generateProfile());
    }
    
    @Test
    public void findFollowingWithLoginIfExist() {
        int resultSize = 3;
        String currentLogin = "login";
        Profile currentProfile = generateProfile();
        
        Map<String, Profile> following = new HashMap<>();
        List<Integer> followedIds = new ArrayList<>();
        List<ProfileDto> expected = new ArrayList<>();
    
        for(int i = 0; i < resultSize; i++) {
            Account account = generateAccount("a" + i);
            Profile profile = generateProfile(account);
            profile.setId(i);
            following.put(account.getLogin(), profile);
        
            ProfileDto profileDto = generateProfileDto(profile);
            expected.add(profileDto);
        }
    
        
        when(profileDaoMock.findFollowingWithLogin(any(Profile.class)))
                .thenReturn(following);
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
        
        when(profileDaoMock.findByLogin(currentLogin))
                .thenReturn(currentProfile);
        
        when(profileDaoMock.pickFollowed(currentProfile, followedIds.toArray(new Integer[]{})))
                .thenReturn(null);
    
        for (Map.Entry<String, Profile> entry : following.entrySet())
            when(profileMapperMock.asProfileDto(entry.getValue()))
                    .thenReturn(generateProfileDto(entry.getValue()));
    
        List<ProfileDto> actual = service.findFollowingWithLogin(generateProfile());
    
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(expected.size(), actual.size());
    
        for (ProfileDto dto : expected)
            assertTrue(actual.contains(dto));
    }
    
    @Test
    public void findFollowingWithLoginIfNoFollowing() {
        when(profileDaoMock.findFollowingWithLogin(any(Profile.class)))
                .thenReturn(null);
    
        Profile profile = generateProfile();
        profile.setId(1);
    
        List<ProfileDto> actual = service.findFollowingWithLogin(profile);
    
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findFollowingWithLoginIfUserNotExists() {
        when(profileDaoMock.findFollowingWithLogin(any(Profile.class)))
                .thenThrow(NoResultException.class);
    
        service.findFollowingWithLogin(generateProfile());
    }
    
    @Test
    public void addFollowerIfOwnerHasNoFollowers() {
        Profile owner = generateProfile();
        Profile follower = generateProfile();
        
        when(profileDaoMock.findFollowers(owner))
                .thenReturn(null);
        
        when(profileDaoMock.updateFollowers(owner))
                .thenReturn(owner);
        
        Profile actual = service.addFollower(owner, follower);
        
        assertNotNull(actual.getFollowers());
        assertEquals(1, actual.getFollowers().size());
    }
    
    @Test
    public void addFollowerIfOwnerHas2Followers() {
        int oldFollowerCount = 2;
        
        Profile owner = generateProfile();
        Profile follower = generateProfile();
        
        Set<Profile> followers = new HashSet<>();
        
        for (int i = 0; i < oldFollowerCount; i++) {
            Profile temp = generateProfile();
            temp.setName("temp" + i);
            followers.add(temp);
        }
        
        when(profileDaoMock.findFollowers(owner))
                .thenReturn(followers);
    
        when(profileDaoMock.updateFollowers(owner))
                .thenReturn(owner);
    
        Profile actual = service.addFollower(owner, follower);
    
        assertNotNull(actual.getFollowers());
        assertEquals(oldFollowerCount + 1, actual.getFollowers().size());
    }
    
    @Test
    public void removeFollowerIfOwnerHasNoFollowers() {
        Profile owner = generateProfile();
        Profile follower = generateProfile();
    
        when(profileDaoMock.findFollowers(owner))
                .thenReturn(null);
    
        when(profileDaoMock.updateFollowers(owner))
                .thenReturn(owner);
    
        Profile actual = service.removeFollower(owner, follower);
    
        assertNull(actual.getFollowers());
    }
    
    @Test
    public void removeFollowerIfOwnerHas2Followers() {
        int oldFollowerCount = 2;
    
        Profile owner = generateProfile();
    
        List<Profile> followers = new ArrayList<>();
    
        for (int i = 0; i < oldFollowerCount; i++) {
            Profile temp = generateProfile();
            temp.setName("temp" + i);
            followers.add(temp);
        }
    
        when(profileDaoMock.findFollowers(owner))
                .thenReturn(new HashSet<>(followers));
    
        when(profileDaoMock.updateFollowers(owner))
                .thenReturn(owner);
    
        Profile actual = service.removeFollower(owner, followers.get(0));
    
        assertNotNull(actual.getFollowers());
        assertEquals(oldFollowerCount - 1, actual.getFollowers().size());
    }
}