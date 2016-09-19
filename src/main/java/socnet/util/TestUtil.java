package socnet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import socnet.dto.AccountDto;
import socnet.dto.ProfileDto;
import socnet.dto.PublicMessageDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;


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
    
    public static String toJson(Object object, MappingJackson2HttpMessageConverter jackson2HttpMessageConverter)
            throws JsonProcessingException {
        return jackson2HttpMessageConverter.getObjectMapper().writeValueAsString(object);
    }
    
    public static Account generateAccount() {
        Account account = new Account();
        
        account.setLogin("test");
        account.setPassword("test");
        account.setEmail("test@test.test");
        
        return account;
    }
    
    public static Account generateAccount(String login) {
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(login);
        account.setEmail(login + "@" + login + "." + login);
        
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
    
    public static Profile generateProfile(Account account) {
        Profile profile = new Profile();
        profile.setId(account.getId());
        profile.setName(account.getLogin());
        profile.setPhone("1234");
        
        return profile;
    }
    
    public static ProfileDto generateProfileDto() {
        ProfileDto profileDto = new ProfileDto();
        
        profileDto.setName("test");
        profileDto.setCountry("test");
        profileDto.setPhone("007");
        
        return profileDto;
    }
    
    public static ProfileDto generateProfileDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        
        profileDto.setName(profile.getName());
        profileDto.setCountry(profile.getCountry());
        profileDto.setCity(profile.getCurrentCity());
        profileDto.setPhone(profile.getPhone());
        profileDto.setDateOfBirth(profile.getDateOfBirth());
        profileDto.setInfo(profile.getInfo());
        
        return profileDto;
    }
    
    public static PublicMessage generatePublicMessage(Profile sender) {
        PublicMessage msg = new PublicMessage();
        msg.setTextContent("test message");
        msg.setSender(sender);
        return msg;
    }
    
    public static PublicMessage generatePublicMessage(String text, Profile sender) {
        PublicMessage msg = new PublicMessage();
        msg.setTextContent(text);
        msg.setSender(sender);
        return msg;
    }
    
    public static PublicMessageDto generatePublicMessageDto(PublicMessage message) {
        PublicMessageDto messageDto = new PublicMessageDto();
        messageDto.setTextContent(message.getTextContent());
        messageDto.setCreatedAt(message.getCreatedAt());
        return messageDto;
    }
}
