package com.kenji.service.impl;

import com.kenji.dao.AdminDao;
import com.kenji.domain.Admin;
import com.kenji.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminDao adminDao;

    @Override
    public void create(Admin admin) {
        adminDao.create(admin);
    }

    @Override
    public Admin getAdminByAccount(String account) {
        return adminDao.getAdminByAccount(account);
    }

    @Override
    public String getAccountById(int id) {
        return adminDao.getAccountById(id);
    }
}
