package socnet.beans.interfaces;

import socnet.entities.Account;

import java.io.IOException;


public interface AccountBean {
    Account find(int id);

    Account authenticate(Account account);

    int signUp(Account account);

    Account update(Account account);

    Account updatePassword(Account account);

    Account updateEmail(Account account);

    void remove(Account account);

    String createSalt();

    String createPasswordHash(String password, String salt);

    Account fromJson(String json) throws IOException;
}
