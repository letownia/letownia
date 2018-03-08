package com.sasor.controller.rest;

import com.sasor.db.GroupObject;
import com.sasor.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController for requests to /groups
 *
 * @author Borg Lojasiewicz
 *
 */
@RestController
public class GroupController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(method= RequestMethod.GET, value="/groups")
    public List<GroupObject> getGroupList(){
        return userGroupService.getGroupList();
    }


    @RequestMapping(method= RequestMethod.PUT,  value="/groups")
    public ResponseEntity<String> createGroup(@RequestBody GroupObject newGroup){
        userGroupService.createGroup(newGroup);
        return responseOK();
    }

    @RequestMapping(method= RequestMethod.POST,  value="/groups/{groupName}")
    public ResponseEntity<String> updateGroup(@PathVariable String groupName, @RequestBody GroupObject newGroup){
        userGroupService.updateGroup(groupName, newGroup.getGroupName());
        return responseOK();
    }

    @RequestMapping(method= RequestMethod.DELETE,  value="/groups/{groupName}")
    public ResponseEntity<String> deleteUser(@PathVariable String groupName){
        userGroupService.deleteGroup(groupName);
        return responseOK();
    }

    private static ResponseEntity<String> responseOK() {
        return ResponseEntity.ok(null);
    }


}