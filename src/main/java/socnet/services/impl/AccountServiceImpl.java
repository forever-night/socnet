package socnet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.AccountDao;
import socnet.entities.Account;
import socnet.services.interfaces.AccountService;
import socnet.services.interfaces.ProfileService;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public int signUp(Account account) {
        if (account.getId() != null)
            account.setId(null);
        
        String password = encodePassword(account.getPassword());
        account.setPassword(password);
        
        
        int id = accountDao.persist(account);
        profileService.create(id);
        
        return id;
    }
    
    @Override
    public Account find(int id) {
        return accountDao.find(id);
    }
    
    @Override
    public Account findByLogin(String login) {
        if (login == null || login.isEmpty())
            return null;
        
        return accountDao.findByLogin(login);
    }
    
    @Override
    public Account update(Account account) {
        return accountDao.update(account);
    }
    
    @Override
    @Transactional
    public Account updateEmail(Account account) {
        Account oldAccount = accountDao.findByLogin(account.getLogin());
        
        if (oldAccount.getEmail().equals(account.getEmail()))
            return oldAccount;
        
        oldAccount.setEmail(account.getEmail());
        return accountDao.update(oldAccount);
    }
    
    @Override
    @Transactional
    public Account updatePassword(Account account) {
        Account oldAccount = accountDao.findByLogin(account.getLogin());
    
        String password = encodePassword(account.getPassword());
        
        if (oldAccount.getPassword().equals(password))
            return oldAccount;
        
        oldAccount.setPassword(password);
        return accountDao.update(oldAccount);
    }
    
    @Override
    @Transactional
    public int remove(Account account) {
        Account persistent = accountDao.find(account.getId());
        
        if (persistent == null)
            return -1;
        
        if (persistent.getLogin().equals(account.getLogin())) {
            profileService.remove(account.getId());
            accountDao.remove(account);
            
            return 0;
        } else
            return 1;
    }
    
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
