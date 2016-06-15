package socnet.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.ProfileDao;
import socnet.entities.Profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @SuppressWarnings("unchecked")
    public List<Profile> findAll() {
        return em.createQuery("from Profile").getResultList();
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
