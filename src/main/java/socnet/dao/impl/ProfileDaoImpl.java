package socnet.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Component
public class ProfileDaoImpl implements ProfileDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Profile find(int id) {
        return em.find(Profile.class, id);
    }
    
    @Override
    public Profile findByLogin(String login) {
        Query query = em.createQuery("from Profile p where p.id = (" +
                "select a.id from Account a where a.login = :login)");
        query.setParameter("login", login);
        
        return (Profile) query.getSingleResult();
    }
    
    @Override
    public List<Profile> findAll() {
        return em.createQuery("from Profile", Profile.class).getResultList();
    }

    @Override
    public Integer persist(Profile profile) {
        em.persist(profile);
        em.flush();

        return profile.getId();
    }

    @Override
    @Transactional
    public Profile update(Profile profile) {
        Profile old = em.find(Profile.class, profile.getId());

        old.setName(profile.getName());
        old.setDateOfBirth(profile.getDateOfBirth());
        old.setCountry(profile.getCountry());
        old.setCurrentCity(profile.getCurrentCity());
        old.setPhone(profile.getPhone());
        old.setInfo(profile.getInfo());

        return em.merge(old);
    }
    
    @Override
    @Transactional
    public Profile update(Profile profile, String login) {
        Profile old;
        
        if (profile.getId() != null)
            old = em.find(Profile.class, profile.getId());
        else
            old = findByLogin(login);
    
        old.setName(profile.getName());
        old.setDateOfBirth(profile.getDateOfBirth());
        old.setCountry(profile.getCountry());
        old.setCurrentCity(profile.getCurrentCity());
        old.setPhone(profile.getPhone());
        old.setInfo(profile.getInfo());
        
        return em.merge(old);
    }
    
    @Override
    public void remove(Integer id) {
        Profile old = em.find(Profile.class, id);

        em.remove(old);
    }

    @Override
    @Transactional
    public void remove(Profile profile) {
        Profile old = em.find(Profile.class, profile.getId());

        em.remove(old);
    }
}
