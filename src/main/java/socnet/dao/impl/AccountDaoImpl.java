package socnet.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.AccountDao;
import socnet.entities.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Component
public class AccountDaoImpl implements AccountDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Account find(int id) {
        return em.find(Account.class, id);
    }

    @Override
    public Account findByLogin(String login) {
        Query query = em.createQuery("from Account where login = :login");
        query.setParameter("login", login);

        return (Account) query.getSingleResult();
    }

    @Override
    public Account findByEmail(String email) {
        Query query = em.createQuery("from Account where email = :email");
        query.setParameter("email", email);

        return (Account) query.getSingleResult();
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
        Account old;
        
        if (account.getId() != null)
            old = em.find(Account.class, account.getId());
        else
            old = findByLogin(account.getLogin());

        old.setPassword(account.getPassword());
        old.setEmail(account.getEmail());

        return em.merge(old);
    }

    @Override
    public void remove(Account account) {
        Account old = em.find(Account.class, account.getId());

        em.remove(old);
    }
}
