package socnet.util;

import org.springframework.web.servlet.view.InternalResourceViewResolver;


public class TestUtil {
    public static InternalResourceViewResolver configureViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        viewResolver.setContentType("text/html;charset=UTF-8");

        return viewResolver;
    }
}
