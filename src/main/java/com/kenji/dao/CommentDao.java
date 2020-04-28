package com.kenji.dao;

import com.kenji.domain.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao {

    @Insert("insert into comment(news_id, user_id, content, ctime) values(#{newsId}, #{userId}, #{content}, #{ctime})")
    void addComment(Comment comment);

    @Select("select * from comment where news_id = #{newsId}")
    List<Comment> getCommentByNewsId(int newsId);

    @Select("select * from comment where user_id = #{userId} order by ctime desc limit #{offset}, #{size}")
    List<Comment> getCommentPageByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);

    @Select("select * from comment where news_id = #{newsId} order by thumbs desc, id asc limit #{offset}, #{size}")
    List<Comment> getCommentPageByNewsId(@Param("newsId") int newsId, @Param("offset") int offset, @Param("size") int size);

    @Select("select * from comment where news_id = #{newsId} order by ctime desc, id asc limit #{offset}, #{size}")
    List<Comment> getCommentPageByCtime(@Param("newsId") int newsId, @Param("offset") int offset, @Param("size") int size);

    @Select("select count(1) from comment where news_id = #{newsId}")
    int getCommentCountByNewsId(int newsId);

    @Select("select count(1) from comment where user_id = #{userId}")
    int getCommentCountByUserId(int userId);

    @Select("select content from comment where id = #{id}")
    String getCommentContentById(int id);

    @Select("select * from comment where user_id = #{userId} order by ctime desc")
    List<Comment> getLatestComment(int userId);

    @Select("select news_id from comment where id = #{id}")
    int getNewsIdByCommentId(int id);

    @Delete("delete from comment where id = #{id}")
    void delComment(int id);

    @Update("update comment set thumbs = thumbs + 1 where id = #{id}")
    void addCommentThumbs(int id);

    @Update("update comment set thumbs = thumbs - 1 where id = #{id}")
    void delCommentThumbs(int id);
}
