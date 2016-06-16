package socnet.controllers.REST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import socnet.beans.interfaces.AccountBean;
import socnet.dto.PasswordChangeDto;
import socnet.entities.Account;

import java.net.URI;


@RestController
@RequestMapping("/api/account")
public class AccountRestController {
    private static final Logger LOGGER = LogManager.getLogger(AccountRestController.class);

    @Autowired
    private AccountBean accountBean;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccountById(@PathVariable int id) {
        if (id > 0)
            return accountBean.find(id);
        else
            return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Integer> signUp(@RequestBody Account account) {
        if (account == null)
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        else if (account.getLogin() != null && account.getPassword() != null) {
            Integer id = accountBean.signUp(account);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/settings/{id}").buildAndExpand(id).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            return new ResponseEntity<Integer>(id, headers, HttpStatus.CREATED);
        } else
            return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(path = "/{id}/check", method = RequestMethod.POST)
    public ResponseEntity<Boolean> authenticate(@RequestBody Account account) {
        if (account != null) {
            Account persistent = accountBean.authenticate(account);

            if (persistent != null)
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            else
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        } else
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "/{id}/email", method = RequestMethod.PUT)
    public ResponseEntity<Integer> updateEmail(@RequestBody Account account, @PathVariable int id) {
        if (account == null || id != account.getId() || account.getEmail() == null)
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        else {
            account = accountBean.updateEmail(account);

            if (account != null)
                return new ResponseEntity<Integer>(account.getId(), HttpStatus.OK);
            else
                return new ResponseEntity<Integer>(0, HttpStatus.OK);
        }
    }

    @RequestMapping(path = "{id}/password", method = RequestMethod.PUT)
    public ResponseEntity<Integer> updatePassword(@RequestBody PasswordChangeDto passwordChangeDto,
                                                  @PathVariable int id) {
        if (passwordChangeDto == null || passwordChangeDto.getAccount().getId() != id)
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        else {
            int updateStatus = accountBean.authenticateAndUpdatePassword(passwordChangeDto);

            return new ResponseEntity<Integer>(updateStatus, HttpStatus.OK);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Integer> delete(@PathVariable int id) {
        if (id != 0) {
            Account account = new Account();
            account.setId(id);

            accountBean.remove(account);

            return new ResponseEntity<Integer>(id, HttpStatus.OK);
        } else
            return new ResponseEntity<Integer>(id, HttpStatus.BAD_REQUEST);
    }
}
