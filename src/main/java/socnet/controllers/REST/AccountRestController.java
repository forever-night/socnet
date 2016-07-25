package socnet.controllers.REST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socnet.entities.Account;
import socnet.services.interfaces.AccountService;


@RestController
@RequestMapping("/api/account")
public class AccountRestController {
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccountByLogin(@PathVariable String login) {
        if (login != null && !login.isEmpty())
            return accountService.findByLogin(login);
        else
            return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody Account account) {
        if (account == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        else if (account.getLogin() != null && account.getPassword() != null) {
            Integer id = accountService.signUp(account);
            account.setId(id);

            return new ResponseEntity(HttpStatus.CREATED);
        } else
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(path = "/{login}/email", method = RequestMethod.PUT)
    public ResponseEntity updateEmail(@RequestBody Account account, @PathVariable String login) {
        if (account == null || login == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        
        if (!login.equals(account.getLogin()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
        accountService.updateEmail(account);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @RequestMapping(path = "/{login}/password", method = RequestMethod.PUT)
    public ResponseEntity updatePassword(@RequestBody Account account, @PathVariable String login) {
        if (account == null || login == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        
        if (!login.equals(account.getLogin()))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        
        accountService.updatePassword(account);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable int id, @RequestParam String login) {
        if (id < 1)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        
        Account account = new Account();
        account.setId(id);
        account.setLogin(login);
        
        int status = accountService.remove(account);
        
        switch (status) {
            case 0:
                return new ResponseEntity(HttpStatus.OK);
            case 1:
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
