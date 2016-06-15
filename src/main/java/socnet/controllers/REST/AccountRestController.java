package socnet.controllers.REST;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import socnet.beans.interfaces.AccountBean;
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
            return new ResponseEntity<Integer>(HttpStatus.NOT_ACCEPTABLE);
        else if (account.getLogin() != null && account.getPassword() != null) {
            Integer id = accountBean.signUp(account);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/profile/{id}").buildAndExpand(id).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            return new ResponseEntity<Integer>(id, headers, HttpStatus.CREATED);
        } else {
            LOGGER.error("error in sign up");
            return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/{id}/check", method = RequestMethod.POST)
    public ResponseEntity<Boolean> authenticate(@RequestBody Account account) {
        if (account != null){
            Account persistent = accountBean.authenticate(account);

            if (persistent != null)
                return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        } else
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping(path = "/{id}/email", method = RequestMethod.PUT)
    public ResponseEntity<Integer> updateEmail(@RequestBody Account account, @PathVariable int id) {
        if (account == null || id != account.getId() || account.getEmail() == null)
            return new ResponseEntity<Integer>(HttpStatus.NOT_ACCEPTABLE);
        else {
            account = accountBean.updateEmail(account);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/settings/{id}#account").buildAndExpand(id).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            return new ResponseEntity<Integer>(account.getId(), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(path = "{id}/password", method = RequestMethod.PUT)
    public ResponseEntity<Integer> updatePassword(@RequestBody Account account, @PathVariable int id) {
        if (account == null || id != account.getId() || account.getPassword() == null)
            return new ResponseEntity<Integer>(HttpStatus.NOT_ACCEPTABLE);
        else {
            account = accountBean.updatePassword(account);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/settings/{id}#account").buildAndExpand(id).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            return new ResponseEntity<Integer>(account.getId(), headers, HttpStatus.OK);
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
            return new ResponseEntity<Integer>(id, HttpStatus.NOT_ACCEPTABLE);
    }
}
