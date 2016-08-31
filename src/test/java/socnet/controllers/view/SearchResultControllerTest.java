package socnet.controllers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.util.TestUtil;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static socnet.util.TestUtil.*;


public class SearchResultControllerTest {
    SearchResultController controller;
    InternalResourceViewResolver viewResolver;
    MockMvc mockMvc;
    
    @Before
    public void setUp() {
        controller = new SearchResultController();
        viewResolver = configureViewResolver();
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }
    
    @Test
    public void returnSearchViewParamsNull() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(view().name("searchResult"));
    }
    
    @Test
    public void returnSearchViewParamsNotNull() throws Exception {
        String param = "test";
        
        mockMvc.perform(get("/search")
                .param("query", param))
                .andExpect(view().name("searchResult"));
    }
}