package com.kenji.dao;

import com.kenji.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    @Insert("insert into user(account, password, email, pic_url) values(#{account}, #{password}, #{email}, #{picUrl})")
    void create(User user);

    @Select("select * from user where account = #{account}")
    User getUserByAccount(String account);

    @Select("select * from user where id = #{id}")
    User getUserById(int id);

}
