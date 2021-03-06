package com.kenji.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kenji.domain.User;
import com.kenji.service.UserService;
import com.kenji.util.FileUploadUtil;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller("userLoginController")
@RequestMapping("/user")
public class LoginController {

    private static String[] suffixes = new String[]{"jpg", "jpeg", "png"};
    private static long limitSize = 2 * 1024 * 1024;//2M大小

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "userLogin";
    }

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "userRegister";
    }

    @RequestMapping("/toUpdatePwd")
    public String updatePwd() {
        return "updatePwd";
    }

    @RequestMapping("/login")
    public void login(User user, boolean auto, HttpServletResponse response) throws IOException {
        boolean status = userService.getStatus(user.getAccount());
        if(!status) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"当前用户已被封禁，请等待解禁",null);
            return;
        }
        boolean result = userService.login(user);
        if(!result){
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"用户名或密码有误",null);
            return;
        }
        User realUser = userService.getUserByAccount(user.getAccount());
        Cookie cookie = new Cookie("userId","" + realUser.getId());
        cookie.setPath("/user");
        if(auto) {
            cookie.setMaxAge(60 * 60 * 24);
        }
        response.addCookie(cookie);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"登录成功",null);
    }

    @RequestMapping("/register")
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            Map<String, String> params = FileUploadUtil.uploadFile(request, suffixes, limitSize, 1);
            String account = params.get("account");
            String password = params.get("password");
            String email = params.get("email");
            JSONArray jsonArray = JSON.parseArray(params.get("pic"));
            String picUrl = jsonArray.getString(0);
            User user = new User(0, account, password, email, picUrl, 0, TimeUtil.getNow());
            userService.create(user);
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"注册成功",null);
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

    @RequestMapping("/check")
    public void check(String account, HttpServletResponse response) throws IOException {
        User user = userService.getUserByAccount(account);
        if(user != null) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"用户名已注册",null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"用户名可用",null);
        }
    }

    @ResponseBody
    @RequestMapping("/updatePwd")
    public String updatePwd(String account, String password, String oldPassword) {
        boolean flag = userService.updatePassword(password, oldPassword, account);
        if(flag) return "OK";
        return "FAIL";
    }

}
