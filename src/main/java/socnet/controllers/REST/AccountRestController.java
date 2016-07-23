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
    private static final Logger LOGGER = LogManager.getLogger(AccountRestController.class);
    
    @Autowired
    private AccountService accountService;
    
    @RequestMapping(path = "/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccountByLogin(@PathVariable String login) {
//        TODO: get account by login
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> signUp(@RequestBody Account account) {
        LOGGER.info("request to create " + account);
        
        if (account == null)
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        else if (account.getLogin() != null && account.getPassword() != null) {
            Integer id = accountService.signUp(account);
            account.setId(id);
            
            LOGGER.info("created " + account);

            return new ResponseEntity<Integer>(HttpStatus.CREATED);
        } else
            return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(path = "/{id}/email", method = RequestMethod.PUT)
    public ResponseEntity<Integer> updateEmail(@RequestBody Account account, @PathVariable int id) {
        if (account == null || id != account.getId() || account.getEmail() == null)
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        else {
            account = accountService.updateEmail(account);

            if (account != null)
                return new ResponseEntity<Integer>(account.getId(), HttpStatus.OK);
            else
                return new ResponseEntity<Integer>(0, HttpStatus.OK);
        }
    }

//    @RequestMapping(path = "{id}/password", method = RequestMethod.PUT)
//    public ResponseEntity<Integer> updatePassword(@RequestBody PasswordChangeDto passwordChangeDto,
//                                                  @PathVariable int id) {
//        if (passwordChangeDto == null || passwordChangeDto.getAccount().getId() != id)
//            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
//        else {
//            int updateStatus = accountService.authenticateAndUpdatePassword(passwordChangeDto);
//
//            return new ResponseEntity<Integer>(updateStatus, HttpStatus.OK);
//        }
//    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Integer> delete(@PathVariable int id) {
        if (id != 0) {
            Account account = new Account();
            account.setId(id);

            accountService.remove(account);

            return new ResponseEntity<Integer>(id, HttpStatus.OK);
        } else
            return new ResponseEntity<Integer>(id, HttpStatus.BAD_REQUEST);
    }
}
