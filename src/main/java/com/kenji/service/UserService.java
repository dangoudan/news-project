package com.kenji.service;

import com.kenji.domain.User;

public interface UserService {

    void create(User user);

    User getUserByAccount(String account);

    boolean login(User user);

    User getUserById(int id);
}
