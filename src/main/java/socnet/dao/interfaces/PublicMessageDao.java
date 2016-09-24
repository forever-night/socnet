package socnet.dao.interfaces;

import socnet.entities.Profile;
import socnet.entities.PublicMessage;

import java.util.List;


public interface PublicMessageDao {
    /**
     * Find a public message with a given id.
     *
     * @return PublicMessage or null if not found.
     * */
    PublicMessage find(int id);
    
    /**
     * Find all public messages sent by given profile. Messages are ordered by createdAt desc.
     *
     * @return list of PublicMessage or null if has no messages
     * @throws javax.persistence.NoResultException if profile not found
     * */
    List<PublicMessage> findByProfile(Profile profile);
    
    /**
     * Persists a public message.
     * */
    Integer persist(PublicMessage publicMessage);
    
    /**
     * Updates a public message.
     * */
    PublicMessage update(PublicMessage publicMessage);
    
    /**
     * Removes a public message.
     *
     * @throws javax.persistence.NoResultException if public message not found
     * */
    void remove(PublicMessage publicMessage);
}
