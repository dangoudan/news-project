package com.kenji.service.impl;

import com.kenji.dao.UserDao;
import com.kenji.domain.User;
import com.kenji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void updateMsg1(User user) {
        userDao.updateMsg1(user);
    }

    @Override
    public void updateMsg2(User user) {
        userDao.updateMsg2(user);
    }

    @Override
    public List<User> getAllUserByPage(int offset, int size) {
        return userDao.getAllUserByPage(offset, size);
    }

    @Override
    public int getUserCount() {
        return userDao.getUserCount();
    }

    @Override
    public void delUser(int id) {
        userDao.delUser(id);
    }

    @Override
    public void banned(int id, int status) {
        if(status == 0) {
            userDao.banned(id);
        }else {
            userDao.unBanned(id);
        }
    }

    @Override
    public boolean updatePassword(String password, String oldPassword, String account) {
        int num = userDao.updatePassword(password, oldPassword, account);
        if(num == 1) return true;
        return false;
    }

    @Override
    public boolean getStatus(String account) {
        return userDao.getStatus(account) == 0;
    }
}
