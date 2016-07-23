package socnet.dao.interfaces;

import socnet.entities.Account;

import java.util.List;


public interface AccountDao {
    Account find(int id);

    Account findByLogin(String login);

    Account findByEmail(String email);

    List<Account> findAll();

    Integer persist(Account account);

    Account update(Account account);

    void remove(Account account);
}
