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
    public ResponseEntity<Account> signUp(@RequestBody Account account) {
        if (account != null && account.getLogin() != null) {
            Integer id = accountBean.signUp(account);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/profile/{id}").buildAndExpand(id).toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uri);

            return new ResponseEntity<Account>(headers, HttpStatus.CREATED);
        } else {
            LOGGER.error("error in sign up");
            return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Account> update(@RequestBody Account account, @PathVariable int id) {
        if (id == account.getId()) {
            account = accountBean.update(account);

            // TODO put account

            return new ResponseEntity<Account>(account, HttpStatus.OK);
        } else {
            LOGGER.error("error in update account");
            return new ResponseEntity<Account>(account, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
