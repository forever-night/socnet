package socnet.mappers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import socnet.config.TestConfig;
import socnet.dto.PublicMessageDto;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;
import socnet.util.TestUtil;

import java.util.Date;

import static org.junit.Assert.*;
import static socnet.util.TestUtil.*;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
public class PublicMessageMapperTest {
    @Autowired
    PublicMessageMapper messageMapper;
    
    private Profile sender;
    private PublicMessage message;
    private PublicMessageDto messageDto;
    
    @Before
    public void setUp() {
        sender = generateProfile();
        message = generatePublicMessage(sender);
        message.setCreatedAt(new Date());
        
        messageDto = generatePublicMessageDto(message);
    }
    
    @Test
    public void asPublicMessageDto() {
        int expectedId = 1;
        message.setId(expectedId);
        PublicMessageDto actual = messageMapper.asPublicMessageDto(message);
        
        assertNotNull(actual);
        assertEquals(messageDto, actual);
        assertEquals(message.getId(), actual.getId());
    }
    
    @Test
    public void asPublicMessage() {
        PublicMessage actual = messageMapper.asPublicMessage(messageDto);
        message.setSender(null);
        
        assertNotNull(actual);
        assertEquals(message, actual);
    }
}