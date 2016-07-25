package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.services.interfaces.ProfileService;
import socnet.services.interfaces.UserService;
import socnet.util.TestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class SettingsControllerTest {
    SettingsController controller;
    MockMvc mockMvc;
    InternalResourceViewResolver viewResolver;
    ProfileService mockProfileService;
    UserService mockUserService;


    @Before
    public void setUp() {
        mockProfileService = mock(ProfileService.class);
        mockUserService = mock(UserService.class);

        controller = new SettingsController(mockProfileService, mockUserService);
        viewResolver = TestUtil.configureViewResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void returnSettingsView() throws Exception {
        String expectedLogin = "aaa";
        
        when(mockUserService.getCurrentLogin())
                .thenReturn(expectedLogin);

        mockMvc.perform(get("/settings"))
                .andExpect(view().name("settings"))
                .andExpect(model().attributeExists("login"))
                .andExpect(model().attribute("login", expectedLogin));
    }

    @Test
    public void return403View() throws Exception {
        when(mockUserService.getCurrentLogin())
                .thenReturn(null);

        mockMvc.perform(get("/settings"))
                .andExpect(view().name("403"));
    }
}