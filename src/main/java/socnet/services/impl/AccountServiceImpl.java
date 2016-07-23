package socnet.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.ProfileService;
import socnet.dao.interfaces.AccountDao;
import socnet.dto.PasswordChangeDto;
import socnet.entities.Account;

import java.io.IOException;


@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public int signUp(Account account) {
        if (account.getId() != null)
            account.setId(null);
        
//        TODO check if email already used

        String password = encodePassword(account.getPassword());
        account.setPassword(password);
        

        int id = accountDao.persist(account);
        profileService.create(id);

        return id;
    }

    @Override
    public Account find(int id) {
        return accountDao.find(id);
    }

    @Override
    public Account update(Account account) {
        return accountDao.update(account);
    }

    @Override
    public Account updatePassword(Account account) {
        Account old = accountDao.find(account.getId());

        String password = encodePassword(account.getPassword());
        old.setPassword(password);

        return accountDao.update(old);
    }

    @Override
    @Transactional
    public Account updateEmail(Account account) {
        Account accountWithEmail = accountDao.findByEmail(account.getEmail());
        Account old = accountDao.find(account.getId());

        if (old.getEmail().equals(account.getEmail()))
            return null;

        if (accountWithEmail == null) {
            old.setEmail(account.getEmail());

            return accountDao.update(old);
        }

        // account with this email already exists
        return null;
    }

    @Override
    @Transactional
    public void remove(Account account) {
        profileService.remove(account.getId());
        accountDao.remove(account);
    }

    /**
     * @return 0 if update successful, <br> 1 if old password is wrong, <br> 2 if the parameter is null
//     * */
//    @Override
//    public int authenticateAndUpdatePassword(PasswordChangeDto passwordChangeDto) {
//        if (passwordChangeDto == null)
//            return 2;
//
//        Account temp = authenticate(passwordChangeDto.getAccount());
//
//        if (temp == null)
//            return 1;
//
//        temp.setPassword(passwordChangeDto.getNewPassword());
//        updatePassword(temp);
//        return 0;
//    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean passwordMatches(Account account) {
        Account temp = accountDao.findByLogin(account.getLogin());

        return temp != null && passwordEncoder.matches(account.getPassword(), temp.getPassword());
    }

    @Override
    public Account fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();

        Account account = null;

        try {
            account = mapper.readValue(json, Account.class);

        } catch (IOException e) {
            LOGGER.error(e.getStackTrace());
        } finally {
            return account;
        }
    }
}
