package socnet.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import socnet.config.TestConfig;
import socnet.dao.interfaces.AccountDao;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Account;
import socnet.entities.Profile;

import javax.persistence.NoResultException;
import java.util.*;

import static org.junit.Assert.*;
import static socnet.util.TestUtil.generateAccount;
import static socnet.util.TestUtil.generateProfile;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@Transactional
public class ProfileDaoImplTest {
    @Autowired
    ProfileDao profileDao;
    
    @Autowired
    AccountDao accountDao;
    
    @Test
    public void persistNotNull() {
        Account account = generateAccount("testtest");
        accountDao.persist(account);
        
        Profile profile = generateProfile(account);
        Integer actualId = profileDao.persist(profile);
        
        assertNotNull(actualId);
        assertTrue(actualId > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void persistNull() {
        profileDao.persist(null);
    }
    
    @Test
    public void findIfExists() {
        Account account = generateAccount("testtest");
        accountDao.persist(account);
        
        Profile expected = generateProfile(account);
        profileDao.persist(expected);
        
        Profile actual = profileDao.find(expected.getId());
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void findIfNotExists() {
        Profile actual = profileDao.find(100500);
        assertNull(actual);
    }
    
    @Test
    public void findByLoginIfExists() {
        String expectedLogin = "testtest";
        
        Account expectedAccount = generateAccount(expectedLogin);
        accountDao.persist(expectedAccount);
        
        Profile expectedProfile = generateProfile(expectedAccount);
        profileDao.persist(expectedProfile);
        
        Profile actualProfile = profileDao.findByLogin(expectedLogin);
        assertEquals(expectedProfile, actualProfile);
    }
    
    @Test(expected = NoResultException.class)
    public void findByLoginIfNotExists() {
        profileDao.findByLogin("testtest");
    }
    
    @Test
    public void findAllLikeIfExists() {
        String searchPattern = "testTEST";
        int iterations = 3;
        
        List<Account> expectedAccounts = new ArrayList<>();
        List<Profile> expectedProfiles = new ArrayList<>();
        
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount(i + searchPattern + i);
            accountDao.persist(a);
            expectedAccounts.add(a);
            
            Profile p = generateProfile(a);
            p.setId(a.getId());
            profileDao.persist(p);
            expectedProfiles.add(p);
        }
        
        Account a = generateAccount("bbb");
        accountDao.persist(a);
    
        Profile p = generateProfile(a);
        p.setId(a.getId());
        profileDao.persist(p);
        
        
        Map<String, Profile> actual = profileDao.findAllLike(searchPattern.toUpperCase());
            
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
        assertFalse(actual.containsKey(a.getLogin()));
        assertFalse(actual.containsValue(p));
        
        for (int i = 0; i < iterations; i++) {
            assertTrue(actual.containsKey(expectedAccounts.get(i).getLogin()));
            assertTrue(actual.containsValue(expectedProfiles.get(i)));
            assertEquals(expectedProfiles.get(i), actual.get(expectedAccounts.get(i).getLogin()));
        }
    }
    
    @Test
    public void checkIfFollowsTrue() {
        Account accountOwner = generateAccount("owner");
        Account accountFollower = generateAccount("follower");
        accountDao.persist(accountOwner);
        accountDao.persist(accountFollower);
        
        Profile profileFollower = generateProfile(accountFollower);
        profileDao.persist(profileFollower);
        
        Profile profileOwner = generateProfile(accountOwner);
        
        Set<Profile> followers = new HashSet<>();
        followers.add(profileFollower);
        
        profileOwner.setFollowers(followers);
        profileDao.persist(profileOwner);
        
        boolean actual = profileDao.checkIfFollows(profileOwner, profileFollower);
        
        assertTrue(actual);
    }
    
    @Test
    public void checkIfFollowsFalse() {
        Account account1 = generateAccount("a1");
        Account account2 = generateAccount("a2");
        accountDao.persist(account1);
        accountDao.persist(account2);
        
        Profile profile1 = generateProfile(account1);
        Profile profile2 = generateProfile(account2);
        profileDao.persist(profile1);
        profileDao.persist(profile2);
        
        boolean actual = profileDao.checkIfFollows(profile1, profile2);
        
        assertFalse(actual);
    }
    
    @Test
    public void checkWhoIsFollowedResult2Rows() {
        int iterations = 5;
        int expectedNumberOfRows = 2;
        
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        
        Profile mainProfile = generateProfile(mainAccount);
        profileDao.persist(mainProfile);
        
        
        List<Profile> otherProfiles = new ArrayList<>();
        List<Integer> expectedIds = new ArrayList<>();
        
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            
            Profile p = generateProfile(a);
            
            if (i < expectedNumberOfRows) {
                Set<Profile> followers = new HashSet<>();
                followers.add(mainProfile);
                p.setFollowers(followers);
                
                expectedIds.add(p.getId());
            }
            
            profileDao.persist(p);
            otherProfiles.add(p);
        }
        
        
        List<Integer> actual = profileDao.pickFollowed(mainProfile, expectedIds.toArray(new Integer[]{}));
        
        assertNotNull(actual);
        assertEquals(expectedIds.size(), actual.size());
        assertTrue(actual.containsAll(expectedIds));
    }
    
    @Test
    public void checkWhoIsFollowedNoResult() {
        int iterations = 5;
    
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
    
        Profile mainProfile = generateProfile(mainAccount);
        profileDao.persist(mainProfile);
    
    
        List<Profile> otherProfiles = new ArrayList<>();
        List<Integer> expectedIds = new ArrayList<>();
    
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
        
            Profile p = generateProfile(a);
        
            profileDao.persist(p);
            otherProfiles.add(p);
        }
    
    
        List<Integer> actual = profileDao.pickFollowed(mainProfile, expectedIds.toArray(new Integer[]{}));
    
        assertNull(actual);
    }
    
    @Test
    public void findFollowersIfExists() {
        int iterations = 5;
        
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        
        Profile mainProfile = generateProfile(mainAccount);
        Set<Profile> expected = new HashSet<>();
        
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            Profile p = generateProfile(a);
            profileDao.persist(p);
            
            expected.add(p);
        }
        
        mainProfile.setFollowers(expected);
        profileDao.persist(mainProfile);
        
        Set<Profile> actual = profileDao.findFollowers(mainProfile);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        
        for (Profile expectedProfile : expected)
            assertTrue(actual.contains(expectedProfile));
    }
    
    @Test
    public void findFollowersIfHasNoFollowers() {
        Account account = generateAccount();
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
        
        Set<Profile> actual = profileDao.findFollowers(profile);
        
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findFollowersIfOwnerNotExists() {
        Profile profile = generateProfile();
        profile.setId(1);
        
        profileDao.findFollowers(profile);
    }
    
    @Test
    public void findFollowersWithLoginIfExists() {
        int iterations = 3;
    
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
    
        Profile mainProfile = generateProfile(mainAccount);
        Set<Profile> followers = new HashSet<>();
        Map<String, Profile> expected = new HashMap<>();
    
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            Profile p = generateProfile(a);
            profileDao.persist(p);
        
            followers.add(p);
            expected.put(a.getLogin(), p);
        }
    
        mainProfile.setFollowers(followers);
        profileDao.persist(mainProfile);
        
        
        Map<String, Profile> actual = profileDao.findFollowersWithLogin(mainProfile);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        
        for (Map.Entry<String, Profile> entry : expected.entrySet()) {
            assertTrue(actual.containsKey(entry.getKey()));
            assertTrue(actual.containsValue(entry.getValue()));
        }
    }
    
    @Test
    public void findFollowersWithLoginIfNoFollowers() {
        Account account = generateAccount();
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
    
        Map<String, Profile> actual = profileDao.findFollowersWithLogin(profile);
    
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findFollowersWithLoginIfOwnerNotExists() {
        Profile profile = generateProfile();
        profile.setId(1);
    
        profileDao.findFollowersWithLogin(profile);
    }
    
    @Test
    public void findFollowingIfExists() {
        int iterations = 5;
        
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        Profile mainProfile = generateProfile(mainAccount);
        profileDao.persist(mainProfile);
        
        Set<Profile> expected = new HashSet<>();
        
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            Profile p = generateProfile(a);
            
            Set<Profile> followers = new HashSet<>();
            followers.add(mainProfile);
            p.setFollowers(followers);
            profileDao.persist(p);
            
            expected.add(p);
        }
        
        
        Set<Profile> actual = profileDao.findFollowing(mainProfile);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        
        for (Profile expectedProfile : expected)
            assertTrue(actual.contains(expectedProfile));
    }
    
    @Test
    public void findFollowingIfHasNoFollowing() {
        Account account = generateAccount();
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
        
        Set<Profile> actual = profileDao.findFollowing(profile);
        
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findFollowingIfOwnerNotExists() {
        Profile profile = generateProfile();
        profile.setId(1);
        
        profileDao.findFollowing(profile);
    }
    
    @Test
    public void findFollowingWithLoginIfExists() {
        int iterations = 3;
    
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        Profile mainProfile = generateProfile(mainAccount);
        profileDao.persist(mainProfile);
    
        Map<String, Profile> expected = new HashMap<>();
    
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            Profile p = generateProfile(a);
        
            Set<Profile> followers = new HashSet<>();
            followers.add(mainProfile);
            p.setFollowers(followers);
            profileDao.persist(p);
        
            expected.put(a.getLogin(), p);
        }
    
        
        Map<String, Profile> actual = profileDao.findFollowingWithLogin(mainProfile);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        
        for (Map.Entry<String, Profile> entry : expected.entrySet()) {
            assertTrue(actual.containsKey(entry.getKey()));
            assertTrue(actual.containsValue(entry.getValue()));
        }
    }
    
    @Test
    public void findFollowingWithLoginIfHasNoFollowing() {
        Account account = generateAccount();
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
    
        Map<String, Profile> actual = profileDao.findFollowingWithLogin(profile);
    
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findFollowingWithLoginIfOwnerNotExists() {
        Profile profile = generateProfile();
        profile.setId(1);
    
        profileDao.findFollowingWithLogin(profile);
    }
    
    @Test
    public void updateIfExists() {
        Account account = generateAccount("testtest");
        accountDao.persist(account);
        
        Profile expectedProfile = generateProfile(account);
        profileDao.persist(expectedProfile);
        
        expectedProfile.setName("blablabla");
        expectedProfile.setCountry("country");
        profileDao.update(expectedProfile);
        
        Profile actualProfile = profileDao.find(expectedProfile.getId());
        assertEquals(expectedProfile, actualProfile);
    }
    
    @Test(expected = NoResultException.class)
    public void updateIfNotExists() {
        Account account = generateAccount();
        Profile profile = generateProfile(account);
        
        profileDao.update(profile, account.getLogin());
    }
    
    @Test
    public void updateFollowersIfExists() {
        Account account1 = generateAccount("account1");
        Account account2 = generateAccount("account2");
        accountDao.persist(account1);
        accountDao.persist(account2);
        
        Profile profile1 = generateProfile(account1);
        Profile profile2 = generateProfile(account2);
        profileDao.persist(profile1);
        profileDao.persist(profile2);
        
        Set<Profile> followers1 = new HashSet<>();
        followers1.add(profile2);
        
        profile1.setFollowers(followers1);
        
        Profile actual = profileDao.updateFollowers(profile1);
        assertNotNull(actual);
        assertFalse(actual.getFollowers().isEmpty());
        assertTrue(actual.getFollowers().contains(profile2));
    }
    
    @Test
    public void removeIfHasNoFollowersOrFollowing() {
        Account account = generateAccount("testtest");
        accountDao.persist(account);
        
        Profile expected = generateProfile(account);
        profileDao.persist(expected);
        
        Profile actual = profileDao.find(expected.getId());
        
        profileDao.remove(expected);
        actual = profileDao.find(actual.getId());
        
        assertNull(actual);
    }
    
    @Test(expected = NoResultException.class)
    public void removeIfHasFollowers() {
        int followerCount = 3;
        
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        
        Profile mainProfile = generateProfile(mainAccount);
        Set<Profile> followers = new HashSet<>();
    
        for (int i = 0; i < followerCount; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            Profile p = generateProfile(a);
            profileDao.persist(p);
            
            followers.add(p);
        }
        
        mainProfile.setFollowers(followers);
        profileDao.persist(mainProfile);
        
        Set<Profile> beforeDelete = profileDao.findFollowers(mainProfile);
        
        assertNotNull(beforeDelete);
        assertTrue(beforeDelete.size() == followers.size());
        assertTrue(beforeDelete.containsAll(followers));
        
        profileDao.remove(mainProfile);
        
        Profile actual = profileDao.find(mainProfile.getId());
        assertNull(actual);
    
        for (Profile follower : followers) {
            Set<Profile> following = profileDao.findFollowing(follower);
            assertNull(following);
        }
        
        profileDao.findFollowers(mainProfile);
    }
    
    @Test(expected = NoResultException.class)
    public void removeIfIsFollower() {
        int followingCount = 3;
    
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        Profile mainProfile = generateProfile(mainAccount);
        profileDao.persist(mainProfile);
        
        Set<Profile> following = new HashSet<>();
        Set<Profile> followers = new HashSet<>();
        followers.add(mainProfile);
    
        for (int i = 0; i < followingCount; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
            
            Profile p = generateProfile(a);
            p.setFollowers(followers);
            profileDao.persist(p);
            
            following.add(p);
        }
        
        Set<Profile> beforeDelete = profileDao.findFollowing(mainProfile);
        
        assertNotNull(beforeDelete);
        assertTrue(beforeDelete.size() == following.size());
        assertTrue(beforeDelete.containsAll(following));
        
        profileDao.remove(mainProfile);
        
        Profile actual = profileDao.find(mainProfile.getId());
        assertNull(actual);
        
        for (Profile profile : following) {
            Set<Profile> profileFollowers = profileDao.findFollowers(profile);
            assertNull(profileFollowers);
        }
        
        profileDao.findFollowing(mainProfile);
    }
    
    @Test(expected = NoResultException.class)
    public void removeIfHasFollowerAndIsFollower() {
        int iterations = 3;
    
        Account mainAccount = generateAccount("main");
        accountDao.persist(mainAccount);
        Profile mainProfile = generateProfile(mainAccount);
        profileDao.persist(mainProfile);
        
        Set<Profile> otherProfiles = new HashSet<>();
        Set<Profile> otherFollowers = new HashSet<>();
        otherFollowers.add(mainProfile);
    
        for (int i = 0; i < iterations; i++) {
            Account a = generateAccount("a" + i);
            accountDao.persist(a);
        
            Profile p = generateProfile(a);
            p.setFollowers(otherFollowers);
            profileDao.persist(p);
            
            otherProfiles.add(p);
        }
        
        mainProfile.setFollowers(otherProfiles);
        profileDao.updateFollowers(mainProfile);
        
        profileDao.remove(mainProfile);
        
        Profile actual = profileDao.find(mainProfile.getId());
        assertNull(actual);
        
        profileDao.findFollowers(mainProfile);
        profileDao.findFollowing(mainProfile);
    }
}