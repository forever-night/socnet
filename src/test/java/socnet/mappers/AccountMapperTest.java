package socnet.mappers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import socnet.config.TestConfig;
import socnet.dto.AccountDto;
import socnet.entities.Account;
import socnet.util.TestUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class AccountMapperTest {
    @Autowired
    AccountMapper accountMapper;
    
    private Account account;
    private AccountDto accountDto;
    
    @Before
    public void setUp() {
        account = TestUtil.generateAccount();
        accountDto = TestUtil.generateAccountDto();
    }
    
    @Test
    public void accountMapperNotNull() {
        Assert.assertNotNull(accountMapper);
    }
    
    @Test
    public void asAccountDto() {
        AccountDto actual = accountMapper.asAccountDto(account);
        
        Assert.assertNotNull(actual);
        Assert.assertEquals(accountDto, actual);
    }
    
    @Test
    public void asAccount() {
        Account actual = accountMapper.asAccount(accountDto);
        
        Assert.assertNotNull(actual);
        Assert.assertEquals(account, actual);
    }
}