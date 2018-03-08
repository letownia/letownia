package com.sasor.controller.rest;

import com.sasor.model.UserObjectRestWrapper;
import com.sasor.service.UserGroupService;
import com.sasor.db.UserObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController for requests to /users
 *
 * TODO
 * More intelligent error handling (Maybe create some sort of exception hierarchy)
 *
 * @author Borg Lojasiewicz
 *
 */
@RestController
public class UserController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(method= RequestMethod.GET, value="/users")
    public List<UserObject> getUserList(){
        return userGroupService.getUserList();
    }

    @RequestMapping(method= RequestMethod.PUT,  value="/users")
    public ResponseEntity<String> createUser(@RequestBody UserObjectRestWrapper newUser){
        userGroupService.createUser(UserObject.fromUserObjectHolder(newUser));
        return responseOK();
    }

    @RequestMapping(method= RequestMethod.POST,  value="/users/{userName}")
    public ResponseEntity<String> updateUser(@PathVariable String userName, @RequestBody UserObjectRestWrapper restData){
        userGroupService.updateUser( userName,  UserObject.fromUserObjectHolder(restData), restData.getNewPassword());
        return responseOK();
    }

    @RequestMapping(method= RequestMethod.DELETE,  value="/users/{userName}")
    public ResponseEntity<String> deleteUser(@PathVariable String userName, @RequestBody UserObjectRestWrapper restData){
        userGroupService.deleteUser( userName, UserObject.fromUserObjectHolder(restData));
        return responseOK();
    }

    @RequestMapping(method= RequestMethod.POST, value="/users/{userName}/{groupName}")
    public ResponseEntity<String> addToGroup(@PathVariable String userName, @PathVariable String groupName){
        userGroupService.addUserToGroup(userName,groupName);
        return responseOK();
    }

    @RequestMapping(method= RequestMethod.DELETE,  value="/users/{userName}/{groupName}")
    public ResponseEntity<String> deleteFromGroup(@PathVariable String userName, @PathVariable String groupName){
        userGroupService.deleteUserFromGroup(userName, groupName);
        return responseOK();
    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }


}