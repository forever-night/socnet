package socnet.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import socnet.dao.interfaces.ProfileDao;
import socnet.dto.ProfileDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;

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
    
    private ProfileService service;
    
    
    @Before
    public void setUp() {
        service = new ProfileServiceImpl(profileDaoMock, profileMapperMock);
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
    public void findFollowersWithLoginIfExist() {
        int iterations = 3;
        Set<Profile> followers = new HashSet<>();
        List<ProfileDto> expected = new ArrayList<>();
        
        Map<String, Profile> map = new HashMap<>();
        
        for(int i = 0; i < iterations; i++) {
            Account account = generateAccount("a" + i);
            Profile profile = generateProfile(account);
            followers.add(profile);
            
            if (i == 0) {
                ProfileDto profileDto = generateProfileDto(profile);
                expected.add(profileDto);
            }
            
            map.put(account.getLogin(), profile);
        }
        
        when(profileDaoMock.findFollowersWithLogin(any(Profile.class)))
                .thenReturn(map);
        
        for (Profile profile : followers)
            when(profileMapperMock.asProfileDto(any()))
                .thenReturn(generateProfileDto(profile));
        
        List<ProfileDto> actual = service.findFollowersWithLogin(generateProfile());
        
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
        
        Map<String, Profile> following = new HashMap<>();
        List<ProfileDto> expected = new ArrayList<>();
    
        for(int i = 0; i < resultSize; i++) {
            Account account = generateAccount("a" + i);
            Profile profile = generateProfile(account);
            following.put(account.getLogin(), profile);
        
            ProfileDto profileDto = generateProfileDto(profile);
            expected.add(profileDto);
        }
    
        when(profileDaoMock.findFollowingWithLogin(any(Profile.class)))
                .thenReturn(following);
    
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
    
        List<ProfileDto> actual = service.findFollowingWithLogin(generateProfile());
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
    
//    @Test
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
    
//    @Test
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