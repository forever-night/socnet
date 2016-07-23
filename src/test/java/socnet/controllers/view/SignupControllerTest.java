package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.util.TestUtil;


public class SignupControllerTest {
    SignupController controller;
    MockMvc mockMvc;
    InternalResourceViewResolver viewResolver;

    @Before
    public void setUp() {
        controller = new SignupController();
        viewResolver = TestUtil.configureViewResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void signUp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/signup"))
                .andExpect(MockMvcResultMatchers.view().name("signup"));
    }
}