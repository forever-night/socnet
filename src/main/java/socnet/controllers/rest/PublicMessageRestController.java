package socnet.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import socnet.controllers.EmptyRequestException;
import socnet.dto.PublicMessageDto;
import socnet.entities.Account;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;
import socnet.mappers.PublicMessageMapper;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.PublicMessageService;
import socnet.services.interfaces.UserService;
import socnet.util.Global;

import java.util.List;


@RestController
@RequestMapping(path = "/api/public")
public class PublicMessageRestController {
    @Autowired
    PublicMessageService messageService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    AccountService accountService;
    
    @Autowired
    PublicMessageMapper messageMapper;
    
    public PublicMessageRestController() {}
    
    public PublicMessageRestController(PublicMessageService messageService, UserService userService,
                                       PublicMessageMapper messageMapper, AccountService accountService) {
        this.messageService = messageService;
        this.userService = userService;
        this.messageMapper = messageMapper;
        this.accountService = accountService;
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public PublicMessageDto getMessageById(@PathVariable Integer id) {
        PublicMessage pm = messageService.find(id);
        return messageMapper.asPublicMessageDto(pm);
    }
    
    @RequestMapping(params = {"sender"}, method = RequestMethod.GET)
    public List<PublicMessageDto> getMessagesByLogin(@RequestParam(name = "sender") String login)
            throws EmptyRequestException {
        if (login == null || login.isEmpty())
            throw new EmptyRequestException();
        
        return messageService.findDtoBySenderLogin(login);
    }
    
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public ResponseEntity send(@RequestBody PublicMessageDto messageDto) throws EmptyRequestException {
        if (messageDto == null || messageDto.getTextContent() == null ||
                messageDto.getTextContent().isEmpty())
            throw new EmptyRequestException();
        
        String currentLogin = userService.getCurrentLogin();
        
        if (messageDto.getSenderLogin() == null || messageDto.getSenderLogin().isEmpty())
            messageDto.setSenderLogin(currentLogin);
        else if (!messageDto.getSenderLogin().equals(currentLogin))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        Integer id = messageService.create(messageDto);
        
        HttpStatus status = id > 0 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity(status);
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody PublicMessageDto messageDto, @PathVariable Integer id)
            throws EmptyRequestException {
        if (messageDto == null ||
                messageDto.getTextContent() == null || messageDto.getTextContent().isEmpty() ||
                messageDto.getSenderLogin() == null || messageDto.getSenderLogin().isEmpty())
            throw new EmptyRequestException();
        
        String currentLogin = userService.getCurrentLogin();
        
        if (!currentLogin.equals(messageDto.getSenderLogin()))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        PublicMessageDto dto = messageService.update(messageDto);
        
        HttpStatus status = dto == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
        return new ResponseEntity(status);
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Integer id) throws EmptyRequestException {
        if (id == null || id <= 0)
            throw new EmptyRequestException();
        
        PublicMessage pm = messageService.find(id);
        Profile senderProfile = pm.getSender();
        Account senderAccount = accountService.find(senderProfile.getId());
        
        String currentLogin = userService.getCurrentLogin();
        
        if (!senderAccount.getLogin().equals(currentLogin))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        messageService.remove(pm);
        return new ResponseEntity(HttpStatus.OK);
    }
}
