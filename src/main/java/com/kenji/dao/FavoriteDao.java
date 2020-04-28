package com.kenji.dao;

import com.kenji.domain.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteDao {

    @Insert("insert into favorite(user_id, news_id, ctime) values(#{userId}, #{newsId}, #{ctime})")
    void create(Favorite favorite);

    @Delete("delete from favorite where user_id = #{userId} and news_id = #{newsId}")
    void delete(@Param("userId") int userId, @Param("newsId") int newsId);

    @Select("select * from favorite where user_id = #{userId}")
    List<Favorite> getFavorByUserId(int userId);

    @Select("select * from favorite where user_id = #{userId} limit #{offset}, #{size}")
    List<Favorite> getFavorByUserIdPage(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);

    @Select("select count(1) from favorite where user_id = #{userId}")
    int getFavorCount(int userId);

    @Select("select * from favorite where user_id = #{userId} and news_id = #{newsId}")
    Favorite getFavorOne(@Param("userId") int userId, @Param("newsId") int newsId);

}
