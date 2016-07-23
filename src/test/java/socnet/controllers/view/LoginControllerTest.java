package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.util.TestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class LoginControllerTest {
    LoginController controller;
    MockMvc mockMvc;
    InternalResourceViewResolver viewResolver;


    @Before
    public void setUp() {
        controller = new LoginController();
        viewResolver = TestUtil.configureViewResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void getLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }
}