package socnet.services.interfaces;

import socnet.entities.Account;


public interface AccountService {
    Account find(int id);
    
    Account findByLogin(String login);

    int signUp(Account account);

    Account update(Account account);

    Account updateEmail(Account account);

    Account updatePassword(Account account);

    /**
     * @return -1 if account does not exist,</br>
     *     0 if account is removed successfully,</br>
     *     1 if account is not the same as persistent account with the same ID.
     * */
    int remove(Account account);

    String encodePassword(String password);
}
