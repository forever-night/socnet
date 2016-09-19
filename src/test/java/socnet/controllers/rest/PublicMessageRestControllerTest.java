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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import socnet.dto.PublicMessageDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;
import socnet.mappers.PublicMessageMapper;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.PublicMessageService;
import socnet.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static socnet.util.TestUtil.*;


@RunWith(MockitoJUnitRunner.class)
public class PublicMessageRestControllerTest {
    PublicMessageRestController controller;
    StringHttpMessageConverter stringHttpMessageConverter;
    MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    MockMvc mockMvc;
    
    @Mock
    PublicMessageService messageServiceMock;
    
    @Mock
    UserService userServiceMock;
    
    @Mock
    PublicMessageMapper messageMapperMock;
    
    @Mock
    AccountService accountServiceMock;
    
    @Before
    public void setUp() {
        controller = new PublicMessageRestController(messageServiceMock, userServiceMock,
                messageMapperMock, accountServiceMock);
        
        stringHttpMessageConverter = stringHttpMessageConverter();
        jackson2HttpMessageConverter = jackson2HttpMessageConverter();
    
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(stringHttpMessageConverter, jackson2HttpMessageConverter)
                .build();
    }
    
    @Test
    public void getMessageByIdNotNullExists() throws Exception {
        int messageId = 2;
        
        Profile sender = generateProfile();
        sender.setId(1);
        PublicMessage pm = generatePublicMessage(sender);
        pm.setId(messageId);
        PublicMessageDto dto = generatePublicMessageDto(pm);
        dto.setId(messageId);
        
        String json = toJson(dto, jackson2HttpMessageConverter);
        
        when(messageServiceMock.find(messageId))
                .thenReturn(pm);
        
        when(messageMapperMock.asPublicMessageDto(pm))
                .thenReturn(dto);
        
        mockMvc.perform(get("/api/public/" + messageId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }
    
    @Test
    public void getMessagesByLogin() throws Exception {
        String senderLogin = "sender";
        
        Profile sender = generateProfile();
        PublicMessage pm = generatePublicMessage(sender);
        PublicMessageDto dto = generatePublicMessageDto(pm);
        
        List<PublicMessageDto> dtoList = new ArrayList<>();
        dtoList.add(dto);
        
        String json = toJson(dtoList, jackson2HttpMessageConverter);
        
        when(messageServiceMock.findDtoBySenderLogin(senderLogin))
                .thenReturn(dtoList);
        
        mockMvc.perform(get("/api/public/").param("sender", senderLogin))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }
    
    @Test
    public void send() throws Exception {
        String currentLogin = "login";
        int currentId = 1;
        
        Profile profile = generateProfile();
        profile.setId(currentId);
        PublicMessage pm = generatePublicMessage(profile);
        PublicMessageDto dto = generatePublicMessageDto(pm);
        dto.setSenderLogin(currentLogin);
        
        String json = toJson(dto, jackson2HttpMessageConverter);
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
        
        when(messageServiceMock.create(dto))
                .thenReturn(1);
        
        mockMvc.perform(post("/api/public/send")
                .content(json.getBytes())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
    @Test
    public void update() throws Exception {
        int messageId = 2;
        String currentLogin = "login";
        
        Profile profile = generateProfile();
        profile.setId(1);
        PublicMessage pm = generatePublicMessage(profile);
        PublicMessageDto dto = generatePublicMessageDto(pm);
        dto.setSenderLogin(currentLogin);
        
        String json = toJson(dto, jackson2HttpMessageConverter);
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
        
        when(messageServiceMock.update(dto))
                .thenReturn(dto);
        
        mockMvc.perform(put("/api/public/" + messageId)
                .content(json.getBytes())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
    @Test
    public void delete() throws Exception {
        int profileId = 1;
        int messageId = 2;
        String currentLogin = "login";
        
        Account account = generateAccount(currentLogin);
        account.setId(profileId);
        Profile profile = generateProfile(account);
        profile.setId(profileId);
        PublicMessage pm = generatePublicMessage(profile);
        
        when(messageServiceMock.find(messageId))
                .thenReturn(pm);
        
        when(accountServiceMock.find(profileId))
                .thenReturn(account);
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/public/" + messageId))
                .andExpect(status().isOk());
    }
}