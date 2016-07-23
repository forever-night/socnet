package socnet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import socnet.dao.interfaces.AccountDao;
import socnet.entities.Account;
import socnet.services.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AccountDao accountDao;
    
    @Override
    public User findById(int id) {
        Account account = accountDao.find(id);
        
        return new User(account.getLogin(), account.getPassword(), getGrantedAuthorities(account));
    }
    
    @Override
    public User findByLogin(String login) {
        Account account = accountDao.findByLogin(login);
        
        return new User(account.getLogin(), account.getPassword(), getGrantedAuthorities(account));
    }
    
    @Override
    public String getCurrentLogin() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        if (user instanceof UserDetails)
            return ((UserDetails) user).getUsername();
        else
            return null;
    }
    
    private List<GrantedAuthority> getGrantedAuthorities(Account account) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        authorities.add(new SimpleGrantedAuthority(account.getRole().getRoleTitle()));
        
        return authorities;
    }
}
