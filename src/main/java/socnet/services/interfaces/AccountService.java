package socnet.services.interfaces;

import socnet.dto.PasswordChangeDto;
import socnet.entities.Account;

import java.io.IOException;


public interface AccountService {
    Account find(int id);

    int signUp(Account account);

    Account update(Account account);

    Account updatePassword(Account account);

    Account updateEmail(Account account);

    void remove(Account account);

//    int authenticateAndUpdatePassword(PasswordChangeDto passwordChangeDto);

    String encodePassword(String password);

    boolean passwordMatches(Account account);

    Account fromJson(String json) throws IOException;
}
