package com.sasor.controller.html;

import com.google.gson.Gson;
import com.sasor.db.GroupObject;
import com.sasor.service.UserGroupService;
import com.sasor.db.UserObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * HTML controller for the 3 basic pages
 * 1.) Home Page
 * 2.) Group info page
 * 3.) User info page
 *
 * @author Borg Lojasiewicz
 */
@Controller
public class PageController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping("/")
    public String welcome(Model model) {
        model.addAttribute("usersJSON", userListToJSON(userGroupService.getUserList()));
        model.addAttribute("groupsJSON", groupListToJSON(userGroupService.getGroupList()));
        return "welcome";
    }

    @RequestMapping("/users/{userName}")
    public String user(@PathVariable String userName, Model model) {
        UserObject user = userGroupService.getUser(userName);
        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("userGroupsJSON",  groupListToJSON(user.getGroups()));
        }
        model.addAttribute("requestedUserName", userName);
        return "user";
    }

    @RequestMapping("/groups/{groupName}")
    public String group(@PathVariable String groupName, Model model) {

        GroupObject group = userGroupService.getGroupObject(groupName);
        model.addAttribute("group", group);
        if (group != null) {
            model.addAttribute("groupUsersJSON",userListToJSON(group.getUsers()));
        }

        model.addAttribute("requestedGroupName", groupName);

        return "group";
    }


    private String groupListToJSON(Collection<GroupObject> users){
        return new Gson().toJson(users.stream().map(x->x.getGroupName()).collect(Collectors.toList()));
    }

    private String userListToJSON(Collection<UserObject> users){
        return new Gson().toJson(users.stream().map(x->x.getUserName()).collect(Collectors.toList()));
    }


}