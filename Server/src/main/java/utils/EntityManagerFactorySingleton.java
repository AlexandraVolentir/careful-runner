package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * a singleton class responsible with the management of an EntityManagerFactory
 */
public class EntityManagerFactorySingleton {

    private static EntityManagerFactory entityManagerFactory = null;
    
    private static EntityManagerFactorySingleton entityManagerFactorySingleton = null;

    /**
     * private constructor for the entity manager factory singleton
     */
    private EntityManagerFactorySingleton() {

    }

    /**
     * creates an instance of an entity manager factory
     * @return the singleton with the entity manager factory
     */
    public static EntityManagerFactorySingleton getInstance() {
        if(entityManagerFactorySingleton == null) {
            entityManagerFactorySingleton = new EntityManagerFactorySingleton();
            entityManagerFactory = Persistence.createEntityManagerFactory("ExamplePU");
        }
        return entityManagerFactorySingleton;
    }

    /**
     * getter for the entity manager
     * @return the entity manager factory
     */
    public EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }
}
