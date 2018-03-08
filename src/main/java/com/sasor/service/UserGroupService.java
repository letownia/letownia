package com.sasor.service;

import com.sasor.db.GroupObject;
import com.sasor.db.GroupRepository;
import com.sasor.db.UserObject;
import com.sasor.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer
 *
 * TODO - More fine grained exception Exceptions/exceptionHandling
 * TODO - Better validation than non-empty text
 *
 */

@Service
public class UserGroupService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;


    public List<GroupObject> getGroupList(){
        List<GroupObject> groups = new ArrayList<>();
        groupRepository.findAll().forEach(groups::add);
        return groups;
    }

    public void createGroup(GroupObject group){
        groupRepository.save(group);
    }

    public void updateGroup(String groupName, String newGroupName){
        GroupObject group = getGroupObject(groupName);
        if(group == null){
            throw new UserGroupServiceException("No such group" + groupName);
        }
        group.setGroupName(newGroupName);
        groupRepository.save(group);
    }
    public boolean createGroupForce(GroupObject group){
        GroupObject two = new GroupObject();
        two.setGroupName(group.getGroupName());
        groupRepository.save(group);
        groupRepository.save(two);
        return true;
    }

    public GroupObject getGroupObject(String groupName){
        return groupRepository.getGroupByName(groupName).orElse(null);
    }


    public List<UserObject> getUserList(){
        List<UserObject> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public UserObject getUser(String userName){
        return  userRepository.getUserByName(userName).orElse(null);
    }

    public void updateUser(String userName, UserObject userFromREST, String newPassword){
        UserObject oldUser =  userRepository.getUserByName(userName).orElseThrow(() ->  new
                UserGroupServiceException(" No such user " + userName));
        validatePassword(oldUser, userFromREST);
        oldUser.setFirstName(userFromREST.getFirstName());
        oldUser.setLastName(userFromREST.getLastName());
        oldUser.setUserName(userFromREST.getUserName());
        oldUser.setDateOfBirth(userFromREST.getDateOfBirth());
        if(newPassword != null){
            oldUser.setPassword(newPassword);
        }
        userRepository.save(oldUser);
    }


    public void createUser(UserObject user){
        userRepository.save(user);
    }
    public void deleteGroup(String groupName) {
        GroupObject group = groupRepository.getGroupByName(groupName).orElseThrow( () ->  new
                UserGroupServiceException(" No such group " + groupName));
        groupRepository.delete(group);
    }

    public void deleteUser(String userName, UserObject userFromREST) {
        UserObject oldUser = userRepository.getUserByName(userName).orElseThrow( () ->  new
                UserGroupServiceException(" No such user " + userName));
        validatePassword(oldUser, userFromREST);
        userRepository.delete(oldUser);
    }

    public void addUserToGroup(String userName, String groupName){
        UserObject user = userRepository.getUserByName(userName).orElse(null);
        GroupObject group = groupRepository.getGroupByName(groupName).orElse(null);
        if(user != null){
            if(group == null) {
                group = new GroupObject();
                group.setGroupName(groupName);
                groupRepository.save(group);
            }
            if( !group.getUsers().stream().anyMatch( x -> x.getId() == user.getId())){
                user.getGroups().add(group);
            }else{
                throw new UserGroupServiceException( userName + " already belongs to " + groupName);
            }
            userRepository.save(user);
        }else{
            throw new UserGroupServiceException( userName + " doesn't exist ");
        }
    }
    public void deleteUserFromGroup(String userName, String groupName) {
        UserObject user = userRepository.getUserByName(userName).orElse(null);
        GroupObject group = groupRepository.getGroupByName(groupName).orElse(null);
        if(user == null || group == null){
            throw new UserGroupServiceException( userName + " or " + groupName + " doesn't exist");
        }
        GroupObject foundGroup = user.getGroups().stream().filter(x->x.getId() == group.getId()).findFirst().orElse
                (null);
        if(foundGroup != null) {
            user.getGroups().remove(foundGroup);
            userRepository.save(user);
        }else{
            throw new UserGroupServiceException( userName + " doesn't belong to " + groupName);
        }
    }

    private void validatePassword(UserObject databaseVersion, UserObject userFromREST){
        if ( ! databaseVersion.getPassword().equals(userFromREST.getPassword())){
            throw new UserGroupServiceException("invalid password");
        }
    }
}
