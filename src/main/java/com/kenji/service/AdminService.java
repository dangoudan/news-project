package com.kenji.service;

import com.kenji.domain.Admin;

public interface AdminService {

    void create(Admin admin);

    Admin getAdminByAccount(String account);

    String getAccountById(int id);
}
