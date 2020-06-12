package com.kenji.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kenji.domain.Tags;
import com.kenji.domain.User;
import com.kenji.service.ReplyService;
import com.kenji.service.TagsService;
import com.kenji.service.UserService;
import com.kenji.util.FileUploadUtil;
import com.kenji.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class MyController {

    private static String[] suffixes = new String[]{"jpg", "jpeg", "png"};
    private static long limitSize = 2 * 1024 * 1024;//2M大小

    @Autowired
    private UserService userService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private ReplyService replyService;

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

    @ResponseBody
    @RequestMapping("/updateMsg")
    public void updateMsg(HttpServletRequest request,
                            @CookieValue("userId") int userId,
                            HttpServletResponse response) throws IOException {

        try {
            Map<String, String> params = FileUploadUtil.uploadFile(request, suffixes, limitSize, 1);

            String account = params.get("account");
            String email = params.get("email");
            String file = params.get("file");
            if(file != null) {
                JSONArray jsonArray = JSON.parseArray(file);
                file = jsonArray.getString(0);
            }
            String oldName = userService.getUserById(userId).getAccount();
            User user = new User(userId, account, "", email, file, 0, 0);
            if(file != null) {
                userService.updateMsg1(user);
            }else {
                userService.updateMsg2(user);
            }
            replyService.updateUserName(account, oldName);
            replyService.updateRepliedName(account, oldName);
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "修改成功", null);
        } catch (FileUploadUtil.SuffixNotMatchException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "后缀不符合要求", null);
        } catch (FileUploadUtil.OutOfLimitSizeException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件大小限制", null);
        } catch (FileUploadUtil.OutOfLimitFileNumException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件数量限制", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
