package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.services.interfaces.UserService;
import socnet.util.TestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


public class IndexControllerTest {
    IndexController controller;
    InternalResourceViewResolver viewResolver;
    MockMvc mockMvc;
    UserService mockUserService;
    
    @Before
    public void setUp() {
        mockUserService = mock(UserService.class);
        
        controller = new IndexController(mockUserService);
        viewResolver = TestUtil.configureViewResolver();
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }
    
    @Test
    public void redirectToLogin() throws Exception {
        when(mockUserService.getCurrentLogin())
                .thenReturn(null);
        
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("login"));
    }
        
    @Test
    public void redirectToProfile() throws Exception {
        String expectedLogin = "aaa";
        
        when(mockUserService.getCurrentLogin())
                .thenReturn(expectedLogin);
        
        mockMvc.perform(get("/"))
                .andExpect(redirectedUrl("profile"));
    }
}