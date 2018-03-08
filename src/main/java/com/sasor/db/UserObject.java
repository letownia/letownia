package com.sasor.db;

import com.sasor.model.UserObjectRestWrapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic hibernate object representing a User.
 * A few issues/intricacies concerning the object:
 * 1.) In some systems userName would be a sufficient primaryKey but that would require us to make userName immutable.
 * 2.) LocalDate would be a better way to represent DateOfBirth but javascript-date/json interaction uses time-zones.
 * 3.) USER would be a better name, hower USER is a reserved keyword in some SQL implementations.
 *
 * @author Borg Lojasiewicz
 *
 */
@Entity
public class UserObject {
    @Id @GeneratedValue
    private int id;

    @NotNull @Column(name = "userName",unique=true)
    private String userName;
    @NotNull
    private String password; // Change to char[] in future (String objects are harder to get rid of/clear)
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    /* ZonedDateTime is not the right solution for a date-of-birth, however the interaction between
       javascripts date object and JSON made it the easier solution.
     */
    @NotNull
    private ZonedDateTime dateOfBirth;

//  @ElementCollection(targetClass=GroupObject.class)
    @ManyToMany
    private Set<GroupObject> groups = new HashSet<>();

    /* Borg - this could get expensive/redundant if all we make a request to get all users, and in the process we calculate all
       the groups for every user.
     */
//(strategy= GenerationType.AUTO)
    public Set<GroupObject> getGroups() { return groups; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ZonedDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(ZonedDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Converts a UserObjectRestWrapper to a UserObject
     *
     * WARNING - ignores new password (only uses the standard password)
     *
     * @param wrapper A wrapper for the REST parameters from web client.
     * @return returns a new hibernate ready UserObject
     */
    public static UserObject fromUserObjectHolder(UserObjectRestWrapper wrapper){
        UserObject user = new UserObject();
        user.userName = wrapper.getUserName();
        user.firstName = wrapper.getFirstName();
        user.lastName = wrapper.getLastName();
        user.password = wrapper.getPassword();
        user.dateOfBirth = wrapper.getDateOfBirth();
        /*WARNING - we are ignoring the newPassword here !!*/
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
