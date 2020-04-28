package com.kenji.dao;

import com.kenji.domain.Thumb;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbDao {

    @Insert("insert into thumb(user_id, news_Id, ctime) values(#{userId}, #{newsId}, #{ctime})")
    void addThumbToNews(Thumb thumb);

    @Insert("insert into thumb(user_id, comment_id, ctime) values(#{userId}, #{commentId}, #{ctime})")
    void addThumbToComment(Thumb thumb);

    @Insert("insert into thumb(user_id, reply_id, ctime) values(#{userId}, #{replyId}, #{ctime})")
    void addThumbToReply(Thumb thumb);

    @Select("select * from thumb where user_id = #{userId} and news_id = #{newsId}")
    Thumb findOneNewsThumb(@Param("userId") int userId, @Param("newsId") int newsId);

    @Select("select * from thumb where user_id = #{userId} and comment_id = #{commentId}")
    Thumb findOneCommentThumb(@Param("userId") int userId, @Param("commentId") int commentId);

    @Select("select * from thumb where user_id = #{userId} and reply_id = #{replyId}")
    Thumb findOneReplyThumb(@Param("userId") int userId, @Param("replyId") int replyId);

    @Delete("delete from thumb where user_id = #{userId} and news_id = #{newsId}")
    void delThumbToNews(@Param("userId") int userId, @Param("newsId") int newsId);

    @Delete("delete from thumb where user_id = #{userId} and comment_id = #{commentId}")
    void delThumbToComment(@Param("userId") int userId, @Param("commentId") int commentId);

    @Delete("delete from thumb where user_id = #{userId} and reply_id = #{replyId}")
    void delThumbToReply(@Param("userId") int userId, @Param("replyId") int replyId);

}
