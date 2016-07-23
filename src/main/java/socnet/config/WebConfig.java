package socnet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.util.HibernateAwareObjectMapper;

import java.util.List;


@Configuration
@EnableWebMvc
@ComponentScan("socnet.controllers")
public class WebConfig extends WebMvcConfigurerAdapter{
    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        viewResolver.setContentType("text/html;charset=UTF-8");

        return viewResolver;
    }

    @Bean
    public HibernateAwareObjectMapper hibernateAwareObjectMapper() {
        return new HibernateAwareObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(hibernateAwareObjectMapper());

        return converter;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/WEB-INF/views/**").addResourceLocations("/views/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
