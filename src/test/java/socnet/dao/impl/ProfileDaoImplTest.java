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
        Integer actualId = profileDao.persist(null);
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
        Profile actual = profileDao.findByLogin("testtest");
    }
    
    @Test
    public void findAllLikeLoginIfExists() {
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
        
        
        Map<String, Profile> actual = profileDao.findAllLikeLogin(searchPattern.toUpperCase());
            
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
    
//    @Test
    public void remove() {
        Account account = generateAccount("testtest");
        accountDao.persist(account);
        
        Profile expected = generateProfile(account);
        profileDao.persist(expected);
        
        Profile actual = profileDao.find(expected.getId());
        
        profileDao.remove(expected);
        actual = profileDao.find(actual.getId());
        
        assertNull(actual);
    }
    
//    @Test
    public void removeIfProfileIsFollower() {
        Account a1 = generateAccount("a1");
        Account a2 = generateAccount("a2");
        Account a3 = generateAccount("a3");
        accountDao.persist(a1);
        accountDao.persist(a2);
        accountDao.persist(a3);
        
        Profile p1 = generateProfile(a1);
        Profile p2 = generateProfile(a2);
        Profile p3 = generateProfile(a3);
        profileDao.persist(p1);
        profileDao.persist(p2);
        
        p3.setFollowers(new HashSet<>(Arrays.asList(p1, p2)));
        profileDao.persist(p3);
        
        Set<Profile> beforeDelete = profileDao.findFollowers(p3);
        
        assertTrue(beforeDelete.contains(p1));
        
        
        profileDao.remove(p1);
        Set<Profile> afterDelete = profileDao.findFollowers(p3);
        
        assertNotNull(afterDelete);
        assertFalse(afterDelete.contains(p1));
        assertTrue(afterDelete.contains(p2));
    }
    
//    @Test
    public void removeIfProfileHasFollower() {
        Account a1 = generateAccount("a1");
        Account a2 = generateAccount("a2");
        Account a3 = generateAccount("a3");
        accountDao.persist(a1);
        accountDao.persist(a2);
        accountDao.persist(a3);
    
        Profile p1 = generateProfile(a1);
        Profile p2 = generateProfile(a2);
        Profile p3 = generateProfile(a3);
        profileDao.persist(p1);
        profileDao.persist(p2);
        
        p3.setFollowers(new HashSet<>(Arrays.asList(p1, p2)));
        profileDao.persist(p3);
        
        Set<Profile> beforeDelete = profileDao.findFollowing(p2);
        
        assertNotNull(beforeDelete);
        assertTrue(beforeDelete.contains(p3));
        
        
        profileDao.remove(p3);
        Profile profileAfterDelete = profileDao.find(p2.getId());
        Set<Profile> afterDelete = profileDao.findFollowing(p2);
        
        assertNotNull(profileAfterDelete);
        assertEquals(p2, profileAfterDelete);
        assertNull(afterDelete);
    }
}