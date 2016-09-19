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
import socnet.dao.interfaces.PublicMessageDao;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static socnet.util.TestUtil.generateAccount;
import static socnet.util.TestUtil.generateProfile;
import static socnet.util.TestUtil.generatePublicMessage;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@Transactional
public class PublicMessageDaoImplTest {
    @Autowired
    ProfileDao profileDao;
    
    @Autowired
    AccountDao accountDao;
    
    @Autowired
    PublicMessageDao messageDao;
    
    @Test
    public void persistNotNull() {
        Account account = generateAccount("account");
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
        
        String text = "testmsg";
        PublicMessage msg = generatePublicMessage(text, profile);
        Integer id = messageDao.persist(msg);
        
        assertNotNull(id);
        assertTrue(id > 0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void persistProfileNotPersisted() {
        Profile profile = generateProfile();
        profile.setId(1);
        PublicMessage pm = generatePublicMessage(profile);
        messageDao.persist(pm);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void persistNull() {
        messageDao.persist(null);
    }
    
    @Test
    public void findByProfileExists() {
        int messageCount = 3;
        
        Account account = generateAccount("account");
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
    
        List<PublicMessage> pmList = new ArrayList<>();
    
        for (int i = 0; i < messageCount; i++) {
            PublicMessage pm = generatePublicMessage("pm" + i, profile);
            pmList.add(pm);
            messageDao.persist(pm);
        }
        
        List<PublicMessage> actual = messageDao.findByProfile(profile);
        
        assertNotNull(actual);
        assertTrue(actual.size() == pmList.size());
        assertTrue(actual.containsAll(pmList));
    }
    
    @Test(expected = NoResultException.class)
    public void findByProfileNotFound() {
        Profile profile = generateProfile();
        profile.setId(1);
        messageDao.findByProfile(profile);
    }
    
    @Test
    public void removeIfExists() {
        Account account = generateAccount("account");
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
        
        PublicMessage pm = generatePublicMessage(profile);
        messageDao.persist(pm);
        
        List<PublicMessage> beforeRemove = messageDao.findByProfile(profile);
        assertNotNull(beforeRemove);
        assertTrue(beforeRemove.size() == 1);
        assertTrue(beforeRemove.contains(pm));
        
        messageDao.remove(pm);
        
        Profile actual = profileDao.find(profile.getId());
        assertNotNull(actual);
        
        List<PublicMessage> afterRemove = messageDao.findByProfile(profile);
        assertNull(afterRemove);
    }
    
    @Test(expected = NoResultException.class)
    public void removeIfNotExists() {
        Account account = generateAccount("account");
        accountDao.persist(account);
        Profile profile = generateProfile(account);
        profileDao.persist(profile);
        
        PublicMessage pm = generatePublicMessage(profile);
        pm.setId(1);
        
        messageDao.remove(pm);
    }
}