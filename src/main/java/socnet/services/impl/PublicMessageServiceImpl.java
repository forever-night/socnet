package socnet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.AccountDao;
import socnet.dao.interfaces.ProfileDao;
import socnet.dao.interfaces.PublicMessageDao;
import socnet.dto.PublicMessageDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;
import socnet.mappers.PublicMessageMapper;
import socnet.services.interfaces.PublicMessageService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PublicMessageServiceImpl implements PublicMessageService{
    @Autowired
    ProfileDao profileDao;
    
    @Autowired
    AccountDao accountDao;
    
    @Autowired
    PublicMessageDao messageDao;
    
    @Autowired
    PublicMessageMapper messageMapper;
    
    public PublicMessageServiceImpl() {}
    
    public PublicMessageServiceImpl(ProfileDao profileDao, AccountDao accountDao,
            PublicMessageDao messageDao, PublicMessageMapper messageMapper) {
        this.profileDao = profileDao;
        this.accountDao = accountDao;
        this.messageDao = messageDao;
        this.messageMapper = messageMapper;
    }
    
    @Override
    public PublicMessage find(int id) {
        return messageDao.find(id);
    }
    
    @Override
    public List<PublicMessage> findBySender(Profile sender) {
        return messageDao.findByProfile(sender);
    }
    
    @Override
    public List<PublicMessageDto> findDtoBySender(Profile sender) {
        List<PublicMessage> messageList = messageDao.findByProfile(sender);
        
        if (messageList == null)
            return null;
        
        List<PublicMessageDto> messageDtoList = messageListToDtoList(messageList);
        Account account = accountDao.find(sender.getId());
        
        for (PublicMessageDto messageDto : messageDtoList)
            messageDto.setSenderLogin(account.getLogin());
        
        return messageDtoList;
    }
    
    @Override
    @Transactional
    public List<PublicMessage> findBySenderLogin(String login) {
        Profile sender = profileDao.findByLogin(login);
        
        return messageDao.findByProfile(sender);
    }
    
    @Override
    public List<PublicMessageDto> findDtoBySenderLogin(String login) {
        List<PublicMessage> messageList = findBySenderLogin(login);
        
        if (messageList == null)
            return null;
        
        List<PublicMessageDto> messageDtoList = messageListToDtoList(messageList);
        
        for (PublicMessageDto messageDto : messageDtoList)
            messageDto.setSenderLogin(login);
        
        return messageDtoList;
    }
    
    @Override
    public Integer create(PublicMessage publicMessage, Profile profile) {
        publicMessage.setSender(profile);
        return messageDao.persist(publicMessage);
    }
    
    @Override
    @Transactional
    public Integer create(PublicMessageDto publicMessageDto) {
        String login = publicMessageDto.getSenderLogin();
        
        if (login == null || login.isEmpty())
            return null;
        
        Profile profile = profileDao.findByLogin(publicMessageDto.getSenderLogin());
        PublicMessage pm = messageMapper.asPublicMessage(publicMessageDto);
        pm.setSender(profile);
        
        return messageDao.persist(pm);
    }
    
    @Override
    public PublicMessage update(PublicMessage publicMessage) {
        return messageDao.update(publicMessage);
    }
    
    @Override
    public PublicMessageDto update(PublicMessageDto publicMessageDto) {
        Integer id = publicMessageDto.getId();
        
        if (id == null || id == 0)
            return null;
        
        PublicMessage pm = messageMapper.asPublicMessage(publicMessageDto);
        pm = messageDao.update(pm);
        
        return messageMapper.asPublicMessageDto(pm);
    }
    
    @Override
    public void remove(PublicMessage publicMessage) {
        messageDao.remove(publicMessage);
    }
    
    @Override
    public void remove(int id) {
        PublicMessage pm = messageDao.find(id);
        
        if (pm == null)
            throw new NoResultException();
        
        messageDao.remove(pm);
    }
    
    private List<PublicMessageDto> messageListToDtoList(List<PublicMessage> messageList) {
        List<PublicMessageDto> dtoMessageList = new ArrayList<>();
    
        if (messageList != null && !messageList.isEmpty())
            for (PublicMessage pm : messageList) {
            PublicMessageDto dto = messageMapper.asPublicMessageDto(pm);
            dtoMessageList.add(dto);
        }
        
        return dtoMessageList;
    }
}
