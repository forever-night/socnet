package main.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by anna on 13/03/16.
 */
public class HibernateUtil {
    private static final String FACTORY_NAME = "entity_manager_pu";
    private static EntityManagerFactory entityManagerFactory;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory(FACTORY_NAME);
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
