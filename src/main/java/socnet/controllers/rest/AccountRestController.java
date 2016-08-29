package socnet.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import socnet.controllers.EmptyRequestException;
import socnet.dto.AccountDto;
import socnet.entities.Account;
import socnet.mappers.AccountMapper;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.UserService;
import socnet.util.Global;


@RestController
@RequestMapping("/api/account")
public class AccountRestController {
    @Autowired
    AccountService accountService;
    
    @Autowired
    AccountMapper accountMapper;
    
    @Autowired
    UserService userService;
    
    public AccountRestController() {}
    
    public AccountRestController(AccountService accountService, AccountMapper accountMapper, UserService userService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.userService = userService;
    }
    
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET)
    public AccountDto getAccountByLogin(@PathVariable String login) {
        return accountMapper.asAccountDto(accountService.findByLogin(login));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody AccountDto accountDto) throws EmptyRequestException {
        if (accountDto == null ||
                accountDto.getLogin() == null || accountDto.getLogin().isEmpty() ||
                accountDto.getPassword() == null || accountDto.getPassword().isEmpty())
            throw new EmptyRequestException();
        
        
        Account account = accountMapper.asAccount(accountDto);
        
        Integer id = accountService.signUp(account);
        account.setId(id);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{login}/email", method = RequestMethod.PUT)
    public ResponseEntity updateEmail(@RequestBody AccountDto accountDto, @PathVariable String login)
            throws EmptyRequestException {
        if (accountDto == null || login == null)
            throw new EmptyRequestException();
        else if (!login.equals(accountDto.getLogin()))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        
        Account account = accountMapper.asAccount(accountDto);
        accountService.updateEmail(account);
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(path = "/{login}/password", method = RequestMethod.PUT)
    public ResponseEntity updatePassword(@RequestBody AccountDto accountDto, @PathVariable String login)
            throws EmptyRequestException {
        if (accountDto == null || login == null)
            throw new EmptyRequestException();
        else if (!login.equals(accountDto.getLogin()))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        
        Account account = accountMapper.asAccount(accountDto);
        accountService.updatePassword(account);
        
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String login) throws EmptyRequestException {
        if (login == null || login.isEmpty())
            throw new EmptyRequestException();
        
        
        String currentLogin = userService.getCurrentLogin();
        
        if (!login.equals(currentLogin))
            throw new AccessDeniedException(Global.Error.ACCESS_DENIED.getMessage());
        
        
        Account account = accountService.findByLogin(login);
        accountService.remove(account);
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
