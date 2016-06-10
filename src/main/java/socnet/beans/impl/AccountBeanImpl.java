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
import socnet.entities.Account;

import java.io.IOException;


@Component
public class AccountBeanImpl implements AccountBean {
    private static final Logger LOGGER = LogManager.getLogger(AccountBeanImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ProfileBean profileBean;

    @Transactional
    public int signUp(Account account) {
        int id = accountDao.persist(account);

        profileBean.create(id);

        return id;
    }

    @Override
    public Account find(int id) {
        return accountDao.find(id);
    }

    public Account authenticate(Account account) {
        return accountDao.authenticate(account);
    }

    public Account update(Account account) {
        return accountDao.update(account);
    }

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
