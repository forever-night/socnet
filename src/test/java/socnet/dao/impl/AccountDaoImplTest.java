package socnet.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import socnet.config.TestConfig;
import socnet.dao.interfaces.AccountDao;
import socnet.entities.Account;
import socnet.entities.Role;

import javax.persistence.NoResultException;

import static org.junit.Assert.*;
import static socnet.util.TestUtil.generateAccount;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@Transactional
public class AccountDaoImplTest {
    @Autowired
    AccountDao accountDao;
        
    @Test
    public void persistNotNull() {
        Account account = generateAccount("aaa");
        
        Integer actualId = accountDao.persist(account);
        
        assertNotNull(actualId);
        assertTrue(actualId > 0);
        assertTrue(account.getEnabled());
        assertEquals(Role.USER, account.getRole());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void persistNull() {
        Integer actualId = accountDao.persist(null);
        
        assertNull(actualId);
    }
    
    @Test
    public void findIfExists() {
        Account expected = generateAccount("testtest");
        accountDao.persist(expected);
        
        Account actual = accountDao.find(expected.getId());
        assertEquals(expected, actual);
    }
    
    @Test
    public void findIfNotExists() {
        Account actual = accountDao.find(100500);
        assertNull(actual);
    }
    
    @Test
    public void findByLoginIfExists() {
        String expectedLogin = "testtest";
        Account expected = generateAccount(expectedLogin);
        accountDao.persist(expected);
        
        Account actual = accountDao.findByLogin(expectedLogin);
        
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    @Test(expected = NoResultException.class)
    public void findByLoginIfNotExists() {
        Account actual = accountDao.findByLogin("testtest");
    }
    
    @Test
    public void updateIfExists() {
        Account expected = generateAccount("testtest");
        accountDao.persist(expected);
        
        expected.setPassword("asdf1");
        accountDao.update(expected);
        
        Account actual = accountDao.find(expected.getId());
        assertEquals(expected, actual);
    }
    
    @Test(expected = NoResultException.class)
    public void updateIfNotExists() {
        Account expected = generateAccount();
        
        accountDao.update(expected);
    }
    
    @Test
    public void remove() {
        Account expected = generateAccount("testtest");
        accountDao.persist(expected);
        
        Account actual = accountDao.find(expected.getId());
        
        accountDao.remove(expected);
        actual = accountDao.find(actual.getId());
        assertNull(actual);
    }
}