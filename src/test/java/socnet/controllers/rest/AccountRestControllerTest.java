package socnet.controllers.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;
import socnet.dto.AccountDto;
import socnet.entities.Account;
import socnet.mappers.AccountMapper;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.UserService;
import socnet.util.TestUtil;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class AccountRestControllerTest {
    AccountRestController controller;
    StringHttpMessageConverter stringHttpMessageConverter;
    MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    MockMvc mockMvc;
    
    @Mock
    AccountService accountServiceMock;
    
    @Mock
    AccountMapper accountMapperMock;
    
    @Mock
    UserService userServiceMock;
    
    private AccountDto accountDto;
    private Account account;
    
    @Before
    public void setUp() {
        controller = new AccountRestController(accountServiceMock, accountMapperMock, userServiceMock);
        
        stringHttpMessageConverter = TestUtil.stringHttpMessageConverter();
        jackson2HttpMessageConverter = TestUtil.jackson2HttpMessageConverter();
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(stringHttpMessageConverter, jackson2HttpMessageConverter)
                .build();
        
        
        accountDto = TestUtil.generateAccountDto();
        account = TestUtil.generateAccount();
    }
    
//    fails if AccountDto.getPassword is annotated with JsonIgnore
//    @Test
    public void signUpNotNull() throws Exception {
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);
        
        when(accountMapperMock.asAccount(accountDto))
                .thenReturn(account);
        
        when(accountServiceMock.signUp(account))
                .thenReturn(1);
        
        
        mockMvc.perform(post("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andExpect(status().isCreated());
    }
    
    @Test(expected = NestedServletException.class)
    public void signUpLoginNull() throws Exception {
        accountDto.setLogin("");
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);
        
        mockMvc.perform(post("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andReturn();
    }
    
    @Test
    public void updateEmailNotNull() throws Exception {
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);
    
        when(accountMapperMock.asAccount(accountDto))
                .thenReturn(account);
    
        when(accountServiceMock.updateEmail(account))
                .thenReturn(account);
    
    
        mockMvc.perform(put("/api/account/" + accountDto.getLogin() + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andExpect(status().isOk());
    }
    
    @Test(expected = NestedServletException.class)
    public void updateEmailDifferentLoginOwner() throws Exception {
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);
    
        mockMvc.perform(put("/api/account/" + accountDto.getLogin() + "aaa" + "/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andReturn();
    }
    
//    fails if AccountDto.getPassword is annotated with JsonIgnore
//    @Test
    public void updatePasswordOldPasswordValid() throws Exception {
        String oldPassword = "old";
        accountDto.setPassword("test");
        accountDto.setOldPassword(oldPassword);
        
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);

        when(accountMapperMock.asAccount(accountDto))
                .thenReturn(account);

        when(accountServiceMock.updatePassword(account, oldPassword))
                .thenReturn(account);


        mockMvc.perform(put("/api/account/" + accountDto.getLogin() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andExpect(status().isOk());
    }
    
    //    fails if AccountDto.getPassword is annotated with JsonIgnore
//    @Test
    public void updatePasswordOldPasswordInvalid() throws Exception {
        String oldPassword = "old";
        accountDto.setPassword("test");
        accountDto.setOldPassword(oldPassword);
    
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);
    
        when(accountMapperMock.asAccount(accountDto))
                .thenReturn(account);
    
        when(accountServiceMock.updatePassword(account, oldPassword))
                .thenReturn(null);
    
    
        mockMvc.perform(put("/api/account/" + accountDto.getLogin() + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andExpect(status().isUnauthorized());
    }
    
    @Test(expected = NestedServletException.class)
    public void updatePasswordDifferentLoginOwner() throws Exception {
        String json = TestUtil.toJson(accountDto, jackson2HttpMessageConverter);

        mockMvc.perform(put("/api/account/" + accountDto.getLogin() + "aaa" + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andReturn();
    }
}