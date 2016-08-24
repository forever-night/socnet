package socnet.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socnet.dto.AccountDto;
import socnet.entities.Account;
import socnet.mappers.AccountMapper;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.UserService;


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
    
//    for tests
    public AccountRestController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }
    
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET)
    public Account getAccountByLogin(@PathVariable String login) {
        if (login != null && !login.isEmpty())
            return accountService.findByLogin(login);
        else
            return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody AccountDto accountDto) {
        if (accountDto == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else if (accountDto.getLogin() == null || accountDto.getLogin().isEmpty() ||
                accountDto.getPassword() == null || accountDto.getPassword().isEmpty())
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        
        
        Account account = accountMapper.asAccount(accountDto);
        
        Integer id = accountService.signUp(account);
        account.setId(id);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/{login}/email", method = RequestMethod.PUT)
    public ResponseEntity updateEmail(@RequestBody AccountDto accountDto, @PathVariable String login) {
        if (accountDto == null || login == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else if (!login.equals(accountDto.getLogin()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
        
        Account account = accountMapper.asAccount(accountDto);
        accountService.updateEmail(account);
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(path = "/{login}/password", method = RequestMethod.PUT)
    public ResponseEntity updatePassword(@RequestBody AccountDto accountDto, @PathVariable String login) {
        if (accountDto == null || login == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else if (!login.equals(accountDto.getLogin()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
        
        Account account = accountMapper.asAccount(accountDto);
        accountService.updatePassword(account);
        
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String login) {
        if (login == null || login.isEmpty())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else {
            String currentLogin = userService.getCurrentLogin();
            
            if (!login.equals(currentLogin))
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            
            Account account = accountService.findByLogin(login);
            accountService.remove(account);
            
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
