package socnet.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import socnet.dao.interfaces.AccountDao;
import socnet.entities.Account;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.ProfileService;
import socnet.util.TestUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static socnet.util.TestUtil.*;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
    @Mock
    AccountDao accountDaoMock;
    
    @Mock
    ProfileService profileServiceMock;
    
    @Mock
    PasswordEncoder passwordEncoderMock;
    
    private AccountService service;
    
    @Before
    public void setUp() {
        service = new AccountServiceImpl(accountDaoMock, profileServiceMock, passwordEncoderMock);
    }
    
    @Test
    public void updatePasswordIfOldPasswordValid() {
        String oldPassword = "old";
        String newPassword = "new";
        
        Account old = generateAccount(oldPassword);
        Account expected = generateAccount(oldPassword);
        expected.setPassword(newPassword);
    
        when(accountDaoMock.findByLogin(old.getLogin()))
                .thenReturn(old);
        
        when(passwordEncoderMock.matches(any(), any()))
                .thenReturn(true);
        
        when(passwordEncoderMock.encode(newPassword))
                .thenReturn(newPassword);
        
        when(accountDaoMock.update(old))
                .thenReturn(expected);
        
        Account actual = service.updatePassword(expected, old.getPassword());
        
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void updatePasswordIfOldPasswordInvalid() {
        String oldPassword = "old";
        String wrongOldPassword = "wrong";
    
        Account old = generateAccount(oldPassword);
        Account newAccount = generateAccount(oldPassword);
        newAccount.setPassword("new");
    
        when(accountDaoMock.findByLogin(newAccount.getLogin()))
                .thenReturn(old);
    
        when(passwordEncoderMock.matches(any(), any()))
                .thenReturn(false);
        
        when(passwordEncoderMock.encode(wrongOldPassword))
                .thenReturn(wrongOldPassword);
    
        Account actual = service.updatePassword(newAccount, wrongOldPassword);
    
        assertNull(actual);
    }
}