package repos;

import jakarta.persistence.EntityManager;
import org.dom4j.tree.AbstractEntity;
import utils.EntityManagerFactorySingleton;

import java.io.Serializable;
import java.util.List;

/**
 * abstract class that helps in creating entities for the repo
 * @param <T> the entity
 * @param <ID> data type
 */
public abstract class DataRepository <T extends AbstractEntity, ID extends Serializable> {

    private EntityManager entityManager;

    /**
     * getter for the entity manager
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * setter for the entity manager
     * @param entityManager the entity manager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * the find by id function
     * @param id the id
     * @return the entity
     */
    public abstract T findById(Integer id);
    public abstract List<T> findByName(String id);

    /**
     * persist funciton
     * @param entity the entity
     * @return the truth statement of the success of the persist
     */
    public boolean persist(T entity) {
        try {
            beginTransaction();
            EntityManagerFactorySingleton entityManagerFactorySingleton = EntityManagerFactorySingleton.getInstance();
            EntityManager entityManager = entityManagerFactorySingleton.getEntityManager();
            entityManager.persist(entity);
            commit();
            return true;
        } catch (Exception e) {
            handleException(e);
            rollback();
        }
        return false;
    }

    /**
     * rollback function
     */
    private void rollback() {
        entityManager.getTransaction().rollback();
    }

    private void handleException(Exception exception) {
        System.out.println(exception);
    }

    public void commit() {
        entityManager.getTransaction().commit();
    }

    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

}
