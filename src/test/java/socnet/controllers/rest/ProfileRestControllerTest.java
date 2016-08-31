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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static socnet.util.TestUtil.*;


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
        stringHttpMessageConverter = stringHttpMessageConverter();
        jackson2HttpMessageConverter = jackson2HttpMessageConverter();
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(stringHttpMessageConverter, jackson2HttpMessageConverter)
                .build();
        
        
        profileDto = generateProfileDto();
        profile = generateProfile();
    }
    
    @Test
    public void getProfilesLikeLoginNotNull() throws Exception {
        String searchPattern = "test";
        Map<String, Profile> profileMap = new HashMap<>();

        Profile tempProfile;
        
        for (int i = 0; i < 2; i++) {
            tempProfile = generateProfile();
            profileMap.put(i + searchPattern + i, tempProfile);
        }
        

        when(profileServiceMock.findAllLikeLogin(searchPattern))
                .thenReturn(profileMap);

        when(profileMapperMock.asProfileDto(any()))
                .thenReturn(generateProfileDto());


        mockMvc.perform(get("/api/profile")
                .param("search", searchPattern))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    
    @Test(expected = NestedServletException.class)
    public void getProfilesLikeLoginNull() throws Exception {
        mockMvc.perform(get("/api/profile")
                .param("search", ""))
                .andReturn();
    }
    
    @Test
    public void updateNotNull() throws Exception {
        String json = toJson(profileDto, jackson2HttpMessageConverter());
        
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
        String json = toJson(profileDto, jackson2HttpMessageConverter());
    
        when(userService.getCurrentLogin())
                .thenReturn("aaa");
    
    
        mockMvc.perform(put("/api/profile/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andReturn();
    }
}