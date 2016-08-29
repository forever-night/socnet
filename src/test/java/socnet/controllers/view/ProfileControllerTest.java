package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.NestedServletException;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;
import socnet.util.TestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class ProfileControllerTest {
    ProfileController controller;
    InternalResourceViewResolver viewResolver;
    MockMvc mockMvc;
    ProfileService mockProfileService;
    UserService mockUserService;

    @Before
    public void setUp() {
        mockProfileService = mock(ProfileService.class);
        mockUserService = mock(UserService.class);

        controller = new ProfileController(mockProfileService, mockUserService);
        viewResolver = TestUtil.configureViewResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void returnProfileViewForCurrentUser() throws Exception {
        String expectedUsername = "aaa";
        
        when(mockUserService.getCurrentLogin())
                .thenReturn(expectedUsername);
        
        mockMvc.perform(get("/profile"))
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("login"))
                .andExpect(model().attribute("login", expectedUsername));
    }

    @Test(expected = NestedServletException.class)
    public void returnAccessDeniedForProfile() throws Exception {
        when(mockUserService.getCurrentLogin())
                .thenReturn(null);
        
        mockMvc.perform(get("/profile"))
                .andReturn();
    }
    
    @Test
    public void returnProfileViewForLogin() throws Exception {
        String currentLogin = "aaa";
        String expectedLogin = "bbb";
        
        when(mockUserService.getCurrentLogin())
                .thenReturn(currentLogin);
        
        mockMvc.perform(get("/profile/" + expectedLogin))
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("login"))
                .andExpect(model().attribute("login", expectedLogin));
    }
    
    @Test(expected = NestedServletException.class)
    public void returnAccessDeniedForProfileLogin() throws Exception {
        String expectedLogin = "bbb";
        
        when(mockUserService.getCurrentLogin())
                .thenReturn(null);
        
        mockMvc.perform(get("/profile/" + expectedLogin))
                .andReturn();
    }
}