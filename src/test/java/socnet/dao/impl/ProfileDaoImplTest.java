package socnet.dao.impl;

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

import javax.persistence.NoResultException;

import static org.junit.Assert.*;
import static socnet.util.TestUtil.generateAccount;
import static socnet.util.TestUtil.generateProfile;


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
    public void remove() {
        Account account = generateAccount("testtest");
        accountDao.persist(account);
        
        Profile expected = generateProfile(account);
        profileDao.persist(expected);
        
        Profile actual = profileDao.find(expected.getId());
        
        profileDao.remove(expected.getId());
        actual = profileDao.find(actual.getId());
        
        assertNull(actual);
    }
}