package socnet.beans.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socnet.beans.interfaces.AccountBean;
import socnet.beans.interfaces.ProfileBean;
import socnet.dao.interfaces.AccountDao;
import socnet.dto.PasswordChangeDto;
import socnet.entities.Account;

import java.io.IOException;


@Component
public class AccountBeanImpl implements AccountBean {
    private static final Logger LOGGER = LogManager.getLogger(AccountBeanImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ProfileBean profileBean;

    @Override
    @Transactional
    public int signUp(Account account) {
        if (account.getId() != null)
            account.setId(null);
        
//        TODO check if email already used

        String salt = createSalt();
        String password = createPasswordHash(account.getPassword(), salt);

        account.setSalt(salt);
        account.setPassword(password);


        int id = accountDao.persist(account);
        profileBean.create(id);

        return id;
    }

    @Override
    public Account find(int id) {
        return accountDao.find(id);
    }

    @Override
    public Account authenticate(Account account) {
        String hash = createPasswordHash(account.getPassword(), account.getSalt());
        account.setPassword(hash);

        if (account.getEmail() != null)
            return accountDao.authenticateByEmail(account);
        else if (account.getLogin() != null)
            return accountDao.authenticateByLogin(account);
        else
            return null;
    }

    @Override
    public Account update(Account account) {
        return accountDao.update(account);
    }

    @Override
    public Account updatePassword(Account account) {
        Account old = accountDao.find(account.getId());

        String salt = createSalt();
        String password = createPasswordHash(account.getPassword(), salt);

        old.setSalt(salt);
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
        profileBean.remove(account.getId());
        accountDao.remove(account);
    }

    /**
     * @return 0 if update successful, <br> 1 if old password is wrong, <br> 2 if the parameter is null
     * */
    @Override
    public int authenticateAndUpdatePassword(PasswordChangeDto passwordChangeDto) {
        if (passwordChangeDto == null)
            return 2;

        Account temp = authenticate(passwordChangeDto.getAccount());

        if (temp == null)
            return 1;

        temp.setPassword(passwordChangeDto.getNewPassword());
        updatePassword(temp);
        return 0;
    }

    @Override
    public String createSalt() {
        return "";
    }

    @Override
    public String createPasswordHash(String password, String salt) {
        return password;
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
