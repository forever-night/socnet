package socnet.services.interfaces;

import org.springframework.security.core.userdetails.User;


public interface UserService {
    User findById(int id);
    
    User findByLogin(String login);
    
    String getCurrentLogin();
}
