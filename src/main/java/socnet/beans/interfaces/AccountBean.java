package socnet.beans.interfaces;

import socnet.entities.Account;

import java.io.IOException;


public interface AccountBean {
    Account find(int id);

    Account authenticate(Account account);

    int signUp(Account account);

    Account update(Account account);

    Account fromJson(String json) throws IOException;
}
