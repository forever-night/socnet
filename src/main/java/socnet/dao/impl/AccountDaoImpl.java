package socnet.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.AccountDao;
import socnet.entities.Account;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Component
public class AccountDaoImpl implements AccountDao{
    private static final Logger LOGGER = LogManager.getLogger(AccountDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Account find(int id) {
        return em.find(Account.class, id);
    }

    @Override
    public List<Account> findAll() {
        return em.createQuery("from Account", Account.class).getResultList();
    }

    @Override
    public Integer persist(Account account) {
        em.persist(account);
        em.flush();

        return account.getId();
    }

    @Override
    @Transactional
    public Account update(Account account) {
        Account old = em.find(Account.class, account.getId());

        old.setLogin(account.getLogin());
        old.setPassword(account.getPassword());
        old.setSalt(account.getSalt());
        old.setEmail(account.getEmail());

        return em.merge(old);
    }

    @Override
    public void remove(Account account) {
        Account old = em.find(Account.class, account.getId());

        em.remove(old);
    }

    @Override
    public Account authenticate(Account account) {
        Account acc = null;

        Query query = em.createQuery("from Account acc where acc.email = :email and acc.password = :password");
        query.setParameter("email", account.getEmail());
        query.setParameter("password", account.getPassword());

        try {
            acc = (Account) query.getSingleResult();
        } catch (NoResultException e) {
            LOGGER.info("user " + account.getEmail() + " not found");
            acc = null;
        } finally {
            return acc;
        }
    }
}
