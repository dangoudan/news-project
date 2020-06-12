package com.kenji.dao;

import com.kenji.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    @Insert("insert into user(account, password, email, pic_url, ctime) values(#{account}, #{password}, #{email}, #{picUrl}, #{ctime})")
    void create(User user);

    @Select("select * from user where account = #{account}")
    User getUserByAccount(String account);

    @Select("select * from user where id = #{id}")
    User getUserById(int id);

    @Update("update user set account = #{account}, email = #{email}, pic_url = #{picUrl} where id = #{id}")
    void updateMsg1(User user);

    @Update("update user set account = #{account}, email = #{email} where id = #{id}")
    void updateMsg2(User user);

    @Update("update user set password = #{password} where account = #{account} and password = #{oldPassword}")
    int updatePassword(@Param("password") String password, @Param("oldPassword") String oldPassword, @Param("account") String account);

    @Select("select * from user order by ctime desc limit #{offset}, #{size}")
    List<User> getAllUserByPage(@Param("offset") int offset, @Param("size") int size);

    @Select("select count(1) from user")
    int getUserCount();

    @Delete("delete from user where id = #{id}")
    void delUser(int id);

    @Update("update user set status = 1 where id = #{id}")
    void banned(int id);

    @Update("update user set status = 0 where id = #{id}")
    void unBanned(int id);

    @Select("select status from user where account = #{account}")
    int getStatus(String account);
}
