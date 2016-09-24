package socnet.dao.impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import socnet.dao.interfaces.ProfileDao;
import socnet.dao.interfaces.PublicMessageDao;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Repository
public class PublicMessageDaoImpl implements PublicMessageDao {
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    ProfileDao profileDao;
    
    @Override
    public PublicMessage find(int id) {
        return em.find(PublicMessage.class, id);
    }
        
    @Override
    @Transactional
    public List<PublicMessage> findByProfile(Profile profile) {
        if (profile.getId() != null) {
            Profile sender = profileDao.find(profile.getId());
            
            if (sender != null) {
                List<PublicMessage> result = sender.getSentPublic();
                
                if (result != null)
                    Collections.reverse(result);
                
                return result == null || result.isEmpty() ? null : new ArrayList<>(sender.getSentPublic());
            }
        }
        
        throw new NoResultException();
    }
    
    @Override
    @Transactional
    public Integer persist(PublicMessage publicMessage) {
        if (publicMessage != null) {
    
            Profile sender = publicMessage.getSender();
            Hibernate.initialize(sender);
    
            if (sender != null) {
                Hibernate.initialize(sender.getSentPublic());
                List<PublicMessage> pmList = sender.getSentPublic();
    
                if (pmList == null)
                    pmList = new ArrayList<>();
    
                pmList.add(publicMessage);
                sender.setSentPublic(pmList);
    
                em.persist(publicMessage);
                em.flush();
    
                return publicMessage.getId();
            } else
                throw new NoResultException();
        } else
            throw new IllegalArgumentException();
    }
    
    @Override
    public PublicMessage update(PublicMessage publicMessage) {
        PublicMessage old = em.find(PublicMessage.class, publicMessage.getId());
        
        old.setTextContent(publicMessage.getTextContent());
        return em.merge(old);
    }
    
    @Override
    @Transactional
    public void remove(PublicMessage publicMessage) {
        PublicMessage old = em.find(PublicMessage.class, publicMessage.getId());
        
        if (old == null)
            throw new NoResultException();
        
        Profile sender = old.getSender();
        sender.getSentPublic().remove(old);
        
        em.remove(old);
        em.flush();
    }
}
