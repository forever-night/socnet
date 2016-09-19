package socnet.services.interfaces;

import socnet.dto.PublicMessageDto;
import socnet.entities.Profile;
import socnet.entities.PublicMessage;

import java.util.List;


public interface PublicMessageService {
    /**
     * Find a public message with a given id.
     *
     * @return PublicMessage or null if not found.
     * */
    PublicMessage find(int id);
    
    /**
     * Find a list of public messages with a given sender.
     *
     * @return list of PublicMessage or null if not found
     * @throws javax.persistence.NoResultException if sender not found
     * */
    List<PublicMessage> findBySender(Profile sender);
    
    /**
     * Find a list of publicMessageDto with a given sender.
     *
     * @return list of PublicMessageDto or null if not found
     * @throws javax.persistence.NoResultException if sender not found
     * */
    List<PublicMessageDto> findDtoBySender(Profile sender);
    
    /**
     * Find a list of public messages with a sender that has a given login.
     *
     * @return list of PublicMessage or null if not found
     * @throws javax.persistence.NoResultException if sender not found
     * */
    List<PublicMessage> findBySenderLogin(String login);
    
    /**
     * Find a list of publicMessageDto with a sender that has a given login.
     *
     * @return list of PublicMessageDto or null if not found
     * @throws javax.persistence.NoResultException if sender not found
     * */
    List<PublicMessageDto> findDtoBySenderLogin(String login);
    
    /**
     * Persists a given public message of a given profile.
     * */
    Integer create(PublicMessage publicMessage, Profile profile);
    
    /**
     * Persists a public message with a given dto, which contains sender login.
     * */
    Integer create(PublicMessageDto publicMessageDto);
    
    /**
     * Updates a given public message.
     * */
    PublicMessage update(PublicMessage publicMessage);
    
    /**
     * Updates a public message with a given dto.
     * */
    PublicMessageDto update(PublicMessageDto publicMessageDto);
    
    /**
     * Removes a given public message.
     *
     * @throws javax.persistence.NoResultException if public message not found
     * */
    void remove(PublicMessage publicMessage);
    
    /**
     * Removes a public message with a given id.
     *
     * @throws javax.persistence.NoResultException if public message not found
     * */
    void remove(int id);
}
