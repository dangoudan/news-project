package com.kenji.service;

import com.kenji.domain.User;

import java.util.List;

public interface UserService {

    void create(User user);

    User getUserByAccount(String account);

    boolean login(User user);

    User getUserById(int id);

    void updateMsg1(User user);

    void updateMsg2(User user);

    List<User> getAllUserByPage(int offset, int size);

    int getUserCount();

    void delUser(int id);

    void banned(int id, int status);

    boolean updatePassword(String password, String oldPassword, String account);

    boolean getStatus(String account);
}
