package com.kenji.dao;

import com.kenji.domain.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao {

    @Insert("insert into admin(account, password) values(#{account}, #{password})")
    void create(Admin admin);

    @Select("select * from admin where account = #{account}")
    Admin getAdminByAccount(String account);

    @Select("select account from admin where id = #{id}")
    String getAccountById(int id);

}
