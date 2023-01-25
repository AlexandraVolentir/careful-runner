package models;

import jakarta.persistence.*;
import org.dom4j.tree.AbstractEntity;

import java.io.Serializable;

@Entity
@Table(name = "routes")
@NamedQueries({
        @NamedQuery(name = "Route.findById",
                query = "select e from Route e where e.id = :id"),
        @NamedQuery(name = "Route.findByName",
                query = "select e from Route e where e.name = :name"),
})

/**
 * entity class for Route
 */
public class Route extends AbstractEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "id")
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "path")
    private String path;

    @Basic
    @Column(name = "name")
    private String name;

    public Route() {
    }

    public Route(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    /**
     * getter for the city name
     * @return the city name
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * setter for the city name
     * @param name the city name
     */
    public void setPath(String name) {
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
