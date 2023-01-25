package repos;

import models.Route;
import utils.EntityManagerFactorySingleton;

import java.util.List;

/**
 * repository for manipulating the route object
 */
public class RouteRepository extends DataRepository<Route, Integer> {

    /**
     * constructor for the route repository that
     * initializes the EntityManagerFactorySingleton
     */
    public RouteRepository() {
        EntityManagerFactorySingleton entityManagerFactorySingleton = EntityManagerFactorySingleton.getInstance();
        setEntityManager(entityManagerFactorySingleton.getEntityManager());
    }

    /**
     * finds the route by its id by executing the query in the entity
     * @param id the id of the route
     * @return the route object
     */
    public Route findById(Integer id) {
        return (Route) getEntityManager().createNamedQuery("Route.findById").setParameter("id", id).getSingleResult();
    }

    public List<Route> findByName(String name) {
        return (List<Route>) getEntityManager().createNamedQuery("Route.findByName").setParameter("name", name).getResultList();
    }
    
    /**
     * creates the route in the database
     * @param route the route object
     */
    public void create(Route route){
        beginTransaction();
        getEntityManager().persist(route);
        commit();
    }
}
