package socnet.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import socnet.dao.interfaces.AccountDao;
import socnet.dao.interfaces.ProfileDao;
import socnet.dao.interfaces.PublicMessageDao;
import socnet.dto.PublicMessageDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;
import socnet.mappers.PublicMessageMapper;
import socnet.services.interfaces.PublicMessageService;
import socnet.util.TestUtil;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static socnet.util.TestUtil.*;


@RunWith(MockitoJUnitRunner.class)
public class PublicMessageServiceImplTest {
    @Mock
    ProfileDao profileDaoMock;
    
    @Mock
    AccountDao accountDaoMock;
    
    @Mock
    PublicMessageDao messageDaoMock;
    
    @Mock
    PublicMessageMapper messageMapperMock;
    
    PublicMessageService service;
    
    @Before
    public void setUp() {
        service = new PublicMessageServiceImpl(profileDaoMock, accountDaoMock, messageDaoMock, messageMapperMock);
    }
    
    @Test
    public void findDtoBySenderExists() {
        int messageCount = 3;
        int senderId = 1;
        
        Account account = generateAccount();
        account.setId(senderId);
        Profile sender = generateProfile(account);
        sender.setId(senderId);
        List<PublicMessage> messageList = new ArrayList<>();
        List<PublicMessageDto> expected = new ArrayList<>();
    
        for (int i = 0; i < messageCount; i++) {
            PublicMessage pm = generatePublicMessage("pm" + i, sender);
            messageList.add(pm);
            
            PublicMessageDto dto = generatePublicMessageDto(pm);
            dto.setSenderLogin(account.getLogin());
            expected.add(dto);
        }
        
        when(messageDaoMock.findByProfile(sender))
                .thenReturn(messageList);
        
        for (PublicMessage pm : messageList)
            when(messageMapperMock.asPublicMessageDto(pm))
                    .thenReturn(generatePublicMessageDto(pm));
        
        when(accountDaoMock.find(senderId))
                .thenReturn(account);
        
        List<PublicMessageDto> actual = service.findDtoBySender(sender);
        
        assertNotNull(actual);
        assertTrue(actual.size() == expected.size());
        assertTrue(actual.containsAll(expected));
    }
    
    @Test(expected = NoResultException.class)
    public void findDtoBySenderNotExists() {
        Profile sender = generateProfile();
        sender.setId(1);
        
        when(messageDaoMock.findByProfile(sender))
                .thenThrow(NoResultException.class);
        
        service.findDtoBySender(sender);
    }
    
    @Test
    public void findDtoBySenderLoginExists() {
        String senderLogin = "sender";
        int messageCount = 3;
        
        Profile sender = generateProfile();
        sender.setId(1);
        
        List<PublicMessage> messageList = new ArrayList<>();
        List<PublicMessageDto> expected = new ArrayList<>();
    
        for (int i = 0; i < messageCount; i++) {
            PublicMessage pm = generatePublicMessage("pm" + i, sender);
            messageList.add(pm);
        
            PublicMessageDto dto = generatePublicMessageDto(pm);
            dto.setSenderLogin(senderLogin);
            expected.add(dto);
        }
        
        when(service.findBySenderLogin(senderLogin))
                .thenReturn(messageList);
        
        for (PublicMessage pm : messageList)
            when(messageMapperMock.asPublicMessageDto(pm))
                    .thenReturn(generatePublicMessageDto(pm));
        
        List<PublicMessageDto> actual = service.findDtoBySenderLogin(senderLogin);
        
        assertNotNull(actual);
        assertTrue(actual.size() == expected.size());
        assertTrue(actual.containsAll(expected));
    }
    
    @Test(expected = NoResultException.class)
    public void findDtoBySenderLoginNotExists() {
        String senderLogin = "sender";
        
        when(profileDaoMock.findByLogin(senderLogin))
                .thenThrow(NoResultException.class);
        
        service.findBySenderLogin(senderLogin);
    }
    
    @Test
    public void createDtoNotNull() {
        String senderLogin = "sender";
        int expectedId = 1;
        Profile sender = generateProfile();
        sender.setId(1);
        
        PublicMessage pm = generatePublicMessage(sender);
        PublicMessageDto dto = generatePublicMessageDto(pm);
        dto.setSenderLogin(senderLogin);
        
        when(profileDaoMock.findByLogin(senderLogin))
                .thenReturn(sender);
        
        when(messageMapperMock.asPublicMessage(dto))
                .thenReturn(pm);
        
        when(messageDaoMock.persist(pm))
                .thenReturn(expectedId);
        
        Integer actualId = service.create(dto);
        
        assertNotNull(actualId);
        assertEquals(expectedId, (int) actualId);
    }
    
    @Test
    public void update() {
        int senderId = 1;
        int messageId = 1;
        Profile sender = generateProfile();
        sender.setId(senderId);
        
        PublicMessage pm = generatePublicMessage(sender);
        pm.setId(messageId);
        PublicMessageDto dto = generatePublicMessageDto(pm);
        dto.setId(messageId);
        
        when(messageMapperMock.asPublicMessage(dto))
                .thenReturn(pm);
        
        when(messageDaoMock.update(pm))
                .thenReturn(pm);
        
        when(messageMapperMock.asPublicMessageDto(pm))
                .thenReturn(dto);
        
        PublicMessageDto actual = service.update(dto);
        
        assertNotNull(actual);
        assertEquals(dto, actual);
    }
}