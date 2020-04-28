package com.kenji.controller.user;

import com.kenji.domain.Tags;
import com.kenji.domain.User;
import com.kenji.service.TagsService;
import com.kenji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class MyController {

    @Autowired
    private UserService userService;
    @Autowired
    private TagsService tagsService;

    @RequestMapping("/toMy")
    public String toMy(String type, Model model) {
        List<Tags> tagsList = tagsService.getAllTags();
        for(int i = 0; i < tagsList.size(); i++) {
            if(i == 0) {
                tagsList.get(i).setTag("首页");
            }
            if(tagsList.get(i).getTag().equals("西安")) {
                tagsList.remove(i);
                i--;
            }
        }
        model.addAttribute("tagsList", tagsList);
        model.addAttribute("type", type);
        return "my";
    }

    @ResponseBody
    @RequestMapping("/myMsg")
    public User myMsg(int id) {
        return userService.getUserById(id);
    }

}
