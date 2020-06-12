package com.kenji.controller.admin;

import com.kenji.domain.Admin;
import com.kenji.service.AdminService;
import com.kenji.util.CodeUtil;
import com.kenji.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller("adminLoginController")
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    @RequestMapping("/login")
    public void handleLogin(String account, String password, boolean auto, HttpServletResponse response) throws IOException {
        Admin admin = adminService.getAdminByAccount(account);
        if(admin == null){
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"找不到这个用户",null);
            return;
        }
        if(admin.getPassword().equals(password)){//登录成功
            Cookie cookie = new Cookie("adminId","" + admin.getId());
            //将cookie设入
//            cookie.setMaxAge();
//            cookie.setDomain("baidu.com");//可以种在一级域名下
            cookie.setPath("/admin");
            if(auto) {
                cookie.setMaxAge(60 * 60 * 24);
            }
            response.addCookie(cookie);
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"登录成功",null);
            return;
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"用户名密码不匹配",null);
            return;
        }

    }

    @RequestMapping("/register")
    public void register(String account, String password, String code, HttpServletResponse response) throws IOException {
        String realCode = CodeUtil.getCode();
        if(realCode != null && realCode.equals(code)) {
            Admin admin = new Admin();
            admin.setAccount(account);
            admin.setPassword(password);
            adminService.create(admin);
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"注册成功",null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"验证码有误",null);
        }


    }

    @RequestMapping("/check")
    public void check(String account, HttpServletResponse response) throws IOException {
        Admin admin = adminService.getAdminByAccount(account);
        if(admin != null) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"用户名已注册",null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"用户名可用",null);
        }
    }



}
