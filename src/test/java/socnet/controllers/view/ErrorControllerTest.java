package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.util.Global;
import socnet.util.TestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class ErrorControllerTest {
    ErrorController controller;
    InternalResourceViewResolver viewResolver;
    MockMvc mockMvc;
    
    @Before
    public void setUp() {
        controller = new ErrorController();
        viewResolver = TestUtil.configureViewResolver();
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }
    
    @Test
    public void errorWithoutMessage() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", Global.Error.UNEXPECTED.getMessage()));
    }
    
    @Test
    public void errorWithDefaultMessage() throws Exception {
        String errorMessage = "";
        
        mockMvc.perform(get("/error").param("errorMessage", errorMessage))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", Global.Error.UNEXPECTED.getMessage()));
    }
    
    @Test
    public void errorWithCustomMessage() throws Exception {
        String errorMessage = "test";
        
        mockMvc.perform(get("/error").param("errorMessage", errorMessage))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", errorMessage));
    }
}