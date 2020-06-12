package com.kenji.controller.admin;

import com.kenji.domain.User;
import com.kenji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserListController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toUserList")
    public String toUserList() {
        return "userList";
    }

    @ResponseBody
    @RequestMapping("/getUserList")
    public List<User> getUserList(int offset, int size) {
        return userService.getAllUserByPage(offset, size);
    }

    @ResponseBody
    @RequestMapping("/getAllUserCount")
    public int getAllUserCount() {
        return userService.getUserCount();
    }

    @ResponseBody
    @RequestMapping("/delUser")
    public String delUser(int id) {
        userService.delUser(id);
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/bannedUser")
    public String bannedUser(int id, int status) {
        userService.banned(id, status);
        return "OK";
    }

}
