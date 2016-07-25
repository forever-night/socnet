package socnet.dao.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import socnet.config.TestConfig;
import socnet.dao.interfaces.AccountDao;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Account;
import socnet.entities.Profile;


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
        Account account = createAccount("testtest");
        accountDao.persist(account);
        
        Profile profile = createProfile(account);
        Integer actualId = profileDao.persist(profile);
        
        Assert.assertNotNull(actualId);
        Assert.assertTrue(actualId > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void persistNull() {
        Integer actualId = profileDao.persist(null);
    }
    
    @Test
    public void findIfExists() {
        Account account = createAccount("testtest");
        accountDao.persist(account);
        
        Profile expected = createProfile(account);
        profileDao.persist(expected);
        
        Profile actual = profileDao.find(expected.getId());
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void findIfNotExists() {
        Profile actual = profileDao.find(100500);
        Assert.assertNull(actual);
    }
    
    @Test
    public void findByLoginIfExists() {
        String expectedLogin = "testtest";
        
        Account expectedAccount = createAccount(expectedLogin);
        accountDao.persist(expectedAccount);
        
        Profile expectedProfile = createProfile(expectedAccount);
        profileDao.persist(expectedProfile);
        
        Profile actualProfile = profileDao.findByLogin(expectedLogin);
        Assert.assertEquals(expectedProfile, actualProfile);
    }
    
    @Test
    public void findByLoginIfNotExists() {
        Profile actual = profileDao.findByLogin("testtest");
        Assert.assertNull(actual);
    }
    
    @Test
    public void update() {
        Account account = createAccount("testtest");
        accountDao.persist(account);
        
        Profile expectedProfile = createProfile(account);
        profileDao.persist(expectedProfile);
        
        expectedProfile.setName("blablabla");
        expectedProfile.setCountry("country");
        profileDao.update(expectedProfile);
        
        Profile actualProfile = profileDao.find(expectedProfile.getId());
        Assert.assertEquals(expectedProfile, actualProfile);
    }
    
    
    @Test
    public void remove() {
        Account account = createAccount("testtest");
        accountDao.persist(account);
        
        Profile expected = createProfile(account);
        profileDao.persist(expected);
        
        Profile actual = profileDao.find(expected.getId());
        
        profileDao.remove(expected.getId());
        actual = profileDao.find(actual.getId());
        
        Assert.assertNull(actual);
    }
    
    private Profile createProfile(Account account) {
        Profile profile = new Profile();
        profile.setId(account.getId());
        profile.setName(account.getLogin());
        profile.setPhone("1234");
        
        return profile;
    }
    
    private Account createAccount(String login) {
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(login);
        account.setEmail(login + "@" + login + "." + login);
        
        return account;
    }
}