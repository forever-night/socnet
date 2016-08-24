package socnet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.dto.AccountDto;
import socnet.dto.ProfileDto;
import socnet.entities.Account;
import socnet.entities.Profile;


public class TestUtil {
    public static InternalResourceViewResolver configureViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        viewResolver.setContentType("text/html;charset=UTF-8");

        return viewResolver;
    }
    
    public static MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new HibernateAwareObjectMapper());
    
        return converter;
    }
    
    public static StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter();
    }
    
    public static Account generateAccount() {
        Account account = new Account();
        
        account.setLogin("test");
        account.setPassword("test");
        account.setEmail("test@test.test");
        
        return account;
    }
    
    public static AccountDto generateAccountDto() {
        AccountDto accountDto = new AccountDto();
        
        accountDto.setLogin("test");
        accountDto.setEmail("test@test.test");
        accountDto.setPassword("test");
        
        return accountDto;
    }
    
    public static Profile generateProfile() {
        Profile profile = new Profile();
        
        profile.setName("test");
        profile.setCountry("test");
        profile.setPhone("007");
        
        return profile;
    }
    
    public static ProfileDto generateProfileDto() {
        ProfileDto profileDto = new ProfileDto();
        
        profileDto.setName("test");
        profileDto.setCountry("test");
        profileDto.setPhone("007");
        
        return profileDto;
    }
    
    public static String toJson(Object object, MappingJackson2HttpMessageConverter jackson2HttpMessageConverter)
            throws JsonProcessingException {
        return jackson2HttpMessageConverter.getObjectMapper().writeValueAsString(object);
    }
}
