package com.sasor.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic hibernate object representing a group that holds users.
 * A few issues/intricacies concerning the object:
 * 1.) In some systems groupName would be a sufficient primaryKey but that would require us to make groupName immutable.
 * 3.) Group would be a better classname, however GROUP is a reserved keyword in some SQL implementations.
 *
 * @author Borg Lojasiewicz
 *
 */
@Entity
public class GroupObject {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="ID")
    private int id;

    @NotNull
    @Column(name = "groupName",unique=true)
    private String groupName;



    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @ManyToMany(cascade=CascadeType.ALL, mappedBy="groups")
    private Set<UserObject> users = new HashSet<>();
    public Set<UserObject> getUsers() {
        return users;
    }

    public int getId() {
        return id;
    }

}
