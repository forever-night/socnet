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
import socnet.dto.ProfileDto;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;
import socnet.util.TestUtil;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class ProfileRestControllerTest {
    ProfileRestController controller;
    StringHttpMessageConverter stringHttpMessageConverter;
    MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    MockMvc mockMvc;
    
    @Mock
    ProfileService profileServiceMock;
    
    @Mock
    ProfileMapper profileMapperMock;
    
    @Mock
    UserService userService;
    
    private ProfileDto profileDto;
    private Profile profile;
    
    @Before
    public void setUp() {
        controller = new ProfileRestController(profileServiceMock, profileMapperMock, userService);
        stringHttpMessageConverter = TestUtil.stringHttpMessageConverter();
        jackson2HttpMessageConverter = TestUtil.jackson2HttpMessageConverter();
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(stringHttpMessageConverter, jackson2HttpMessageConverter)
                .build();
        
        
        profileDto = TestUtil.generateProfileDto();
        profile = TestUtil.generateProfile();
    }
    
    @Test
    public void updateNotNull() throws Exception {
        String json = TestUtil.toJson(profileDto, TestUtil.jackson2HttpMessageConverter());
        
        when(profileMapperMock.asProfile(profileDto))
                .thenReturn(profile);
        
        when(userService.getCurrentLogin())
                .thenReturn("test");
        
        when(profileServiceMock.update(profile))
                .thenReturn(profile);
        
        
        mockMvc.perform(put("/api/profile/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andExpect(status().isOk());
    }
    
    @Test(expected = NestedServletException.class)
    public void updateDifferentLoginOwner() throws Exception {
        String json = TestUtil.toJson(profileDto, TestUtil.jackson2HttpMessageConverter());
    
        when(userService.getCurrentLogin())
                .thenReturn("aaa");
    
    
        mockMvc.perform(put("/api/profile/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andReturn();
    }
}