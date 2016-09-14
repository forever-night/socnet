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
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.mappers.ProfileMapper;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

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
    UserService userServiceMock;
    
    private ProfileDto profileDto;
    private Profile profile;
    
    @Before
    public void setUp() {
        controller = new ProfileRestController(profileServiceMock, profileMapperMock, userServiceMock);
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
        List<ProfileDto> dtoList = new ArrayList<>();

        Profile tempProfile;
        
        for (int i = 0; i < 2; i++) {
            tempProfile = generateProfile();
            ProfileDto dto = generateProfileDto(tempProfile);
            dto.setLogin(i + searchPattern + i);
            
            dtoList.add(dto);
        }
        

        when(profileServiceMock.findAllLike(searchPattern))
                .thenReturn(dtoList);

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
    public void getFollowersByLoginExists() throws Exception {
        int resultSize = 3;
        List<ProfileDto> result = new ArrayList<>();
        
        for (int i = 0; i < resultSize; i++) {
            Account account = generateAccount("a" + i);
            Profile profile = generateProfile(account);
            ProfileDto profileDto = generateProfileDto(profile);
            
            result.add(profileDto);
        }
        
        String json = toJson(result, jackson2HttpMessageConverter());
                
        when(userServiceMock.getCurrentLogin())
                .thenReturn("aaa");
        
        when(profileServiceMock.findFollowersWithLogin(any(String.class)))
                .thenReturn(result);
        
        when(profileMapperMock.asProfileDto(any()))
                .thenReturn(generateProfileDto());
        
        
        mockMvc.perform(get("/api/profile/test/followers"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(json));
    }
    
    @Test
    public void getFollowingByLoginExists() throws Exception {
        int resultSize = 3;
        List<ProfileDto> result = new ArrayList<>();
    
        for (int i = 0; i < resultSize; i++) {
            Account account = generateAccount("a" + i);
            Profile profile = generateProfile(account);
            ProfileDto profileDto = generateProfileDto(profile);
        
            result.add(profileDto);
        }
    
        String json = toJson(result, jackson2HttpMessageConverter());
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn("aaa");
    
        when(profileServiceMock.findFollowingWithLogin(any(String.class)))
                .thenReturn(result);
    
        when(profileMapperMock.asProfileDto(any()))
                .thenReturn(generateProfileDto());
    
    
        mockMvc.perform(get("/api/profile/test/following"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(json));
    }
    
    @Test
    public void updateNotNull() throws Exception {
        String json = toJson(profileDto, jackson2HttpMessageConverter());
        
        when(profileMapperMock.asProfile(profileDto))
                .thenReturn(profile);
        
        when(userServiceMock.getCurrentLogin())
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
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn("aaa");
    
    
        mockMvc.perform(put("/api/profile/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.getBytes()))
                .andReturn();
    }
    
    @Test
    public void followNotNull() throws Exception {
        String currentLogin = "aaa";
        String toFollow = "bbb";
        
        Profile profile = generateProfile();
        
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
        
        when(profileServiceMock.findByLogin(any()))
                .thenReturn(profile);
        
        when(profileServiceMock.addFollower(any(), any()))
                .thenReturn(profile);
        
        mockMvc.perform(put("/api/profile/" + toFollow)
                .param("follow", ""))
                .andExpect(status().isOk());
    }
    
    @Test(expected = NestedServletException.class)
    public void followNotLoggedIn() throws Exception {
        String toFollow = "bbb";
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(null);
        
        mockMvc.perform(put("/api/profile/" + toFollow)
                .param("follow", ""))
                .andReturn();
    }
    
    @Test(expected = NestedServletException.class)
    public void followLoginNotFound() throws Exception {
        String currentLogin = "aaa";
        String toFollow = "bbb";
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
    
        when(profileServiceMock.findByLogin(toFollow))
                .thenThrow(NoResultException.class);
    
        mockMvc.perform(put("/api/profile/" + toFollow)
                .param("follow", ""))
                .andReturn();
    }
    
    @Test
    public void unfollowNotNull() throws Exception {
        String currentLogin = "aaa";
        String toUnfollow = "bbb";
    
        Profile profile = generateProfile();
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
    
        when(profileServiceMock.findByLogin(any()))
                .thenReturn(profile);
    
        when(profileServiceMock.removeFollower(any(), any()))
                .thenReturn(profile);
    
        mockMvc.perform(put("/api/profile/" + toUnfollow)
                .param("unfollow", ""))
                .andExpect(status().isOk());
    }
    
    @Test(expected = NestedServletException.class)
    public void unfollowNotLoggedIn() throws Exception {
        String toUnfollow = "bbb";
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(null);
    
        mockMvc.perform(put("/api/profile/" + toUnfollow)
                .param("unfollow", ""))
                .andReturn();
    }
    
    @Test(expected = NestedServletException.class)
    public void unfollowLoginNotFound() throws Exception {
        String currentLogin = "aaa";
        String toUnfollow = "bbb";
    
        when(userServiceMock.getCurrentLogin())
                .thenReturn(currentLogin);
    
        when(profileServiceMock.findByLogin(toUnfollow))
                .thenThrow(NoResultException.class);
    
        mockMvc.perform(put("/api/profile/" + toUnfollow)
                .param("unfollow", ""))
                .andReturn();
    }
}