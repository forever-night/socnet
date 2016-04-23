package dao;

import entities.Account;
import util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by anna on 05/04/16.
 */
public class AccountDAO {
    @PersistenceContext(unitName = "socnet-unit")
    private EntityManager em;

    public Account find(int id) {
        return em.find(Account.class, id);
    }

    public List<Account> findAll() {
        em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        List<Account> accounts = em.createQuery("from Account", Account.class).getResultList();
        em.getTransaction().commit();
        em.close();

        return accounts;
    }

    public void persist(Account account) {
        em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Account account) {
        em = HibernateUtil.getEntityManager();

        Account old = em.find(Account.class, account.getId());
        old.setLogin(account.getLogin());
        old.setPassword(account.getPassword());
        old.setSalt(account.getSalt());
        old.setEmail(account.getEmail());

        em.getTransaction().begin();
        em.merge(old);
        em.getTransaction().commit();
        em.close();
    }

    public void remove(Account account) {
        em = HibernateUtil.getEntityManager();
        Account old = em.find(Account.class, account.getId());

        em.getTransaction().begin();
        em.remove(old);
        em.getTransaction().commit();
        em.close();
    }
}
