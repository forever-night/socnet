package socnet.services.interfaces;

import socnet.entities.Account;


public interface AccountService {
    Account find(int id);
    
    Account findByLogin(String login);

    int signUp(Account account);

    Account update(Account account);

    Account updateEmail(Account account);

    Account updatePassword(Account account);

    void remove(Account account);

    String encodePassword(String password);
}
