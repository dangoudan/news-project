package com.kenji.service.impl;

import com.kenji.dao.UserDao;
import com.kenji.domain.User;
import com.kenji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    public User getUserByAccount(String account) {
        return userDao.getUserByAccount(account);
    }

    @Override
    public boolean login(User user) {
        User realUser = userDao.getUserByAccount(user.getAccount());
        if(realUser == null) {
            return false;
        }
        if(realUser.getPassword().equals(user.getPassword())) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }
}
