package socnet.dao.interfaces;

import socnet.entities.Account;

import java.util.List;


public interface AccountDao {
    Account authenticate(Account account);

    Account find(int id);

    List<Account> findAll();

    Integer persist(Account account);

    Account update(Account account);

    void remove(Account account);
}
