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
import socnet.entities.Account;
import socnet.entities.Role;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@Transactional
public class AccountDaoImplTest {
    @Autowired
    AccountDao accountDao;
        
    @Test
    public void persistNotNull() {
        Account account = createAccount("aaa");
        
        Integer actualId = accountDao.persist(account);
        
        Assert.assertNotNull(actualId);
        Assert.assertTrue(actualId > 0);
        Assert.assertTrue(account.getEnabled());
        Assert.assertEquals(Role.USER, account.getRole());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void persistNull() {
        Integer actualId = accountDao.persist(null);
        
        Assert.assertNull(actualId);
    }
    
    @Test
    public void findIfExists() {
        Account expected = createAccount("testtest");
        accountDao.persist(expected);
        
        Account actual = accountDao.find(expected.getId());
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void findIfNotExists() {
        Account actual = accountDao.find(100500);
        Assert.assertNull(actual);
    }
    
    @Test
    public void findByLoginIfExists() {
        String expectedLogin = "testtest";
        Account expected = createAccount(expectedLogin);
        accountDao.persist(expected);
        
        Account actual = accountDao.findByLogin(expectedLogin);
        
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void findByLoginIfNotExists() {
        Account actual = accountDao.findByLogin("testtest");
        Assert.assertNull(actual);
    }
    
    @Test
    public void update() {
        Account expected = createAccount("testtest");
        accountDao.persist(expected);
        
        expected.setPassword("asdf1");
        accountDao.update(expected);
        
        Account actual = accountDao.find(expected.getId());
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void remove() {
        Account expected = createAccount("testtest");
        accountDao.persist(expected);
        
        Account actual = accountDao.find(expected.getId());
        
        accountDao.remove(expected);
        actual = accountDao.find(actual.getId());
        Assert.assertNull(actual);
    }
    
    private Account createAccount(String login) {
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(login);
        account.setEmail(login + "@" + login + "." + login);
        
        return account;
    }
}