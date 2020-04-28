package com.kenji.dao;

import com.kenji.domain.Reply;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyDao {

    @Insert("insert into reply(user_name, replied_name, comment_id, content, ctime) values(#{userName}, #{repliedName}, #{commentId}, #{content}, #{ctime})")
    void addReplyToReplied(Reply reply);

    @Insert("insert into reply(user_name, comment_id, content, ctime) values(#{userName}, #{commentId}, #{content}, #{ctime})")
    void addReply(Reply reply);

    @Select("select * from reply where comment_id = #{commentId} and replied_name is null order by thumbs desc, id asc limit #{offset}, #{size}")
    List<Reply> getNotRepliedByPage(@Param("commentId") int commentId, @Param("offset") int offset, @Param("size") int size);

    @Select("select * from reply where comment_id = #{commentId} order by thumbs desc, id asc limit #{offset}, #{size}")
    List<Reply> getReplyByPage(@Param("commentId") int commentId, @Param("offset") int offset, @Param("size") int size);

    @Select("select * from reply where user_name = #{userName} order by ctime desc")
    List<Reply> getLatestReply(String userName);

    @Select("select count(1) from reply where comment_id = #{commentId}")
    int getReplyCount(int commentId);

    @Select("select count(1) from reply where user_name = #{userName}")
    int getUserReplyCount(String userName);

    @Select("select content from reply where id = #{id}")
    String getReplyContentById(int id);

    @Delete("delete from reply where id = #{id}")
    void delReply(int id);

    @Delete("delete from reply where comment_id = #{commentId}")
    void delReplyByCommentId(int commentId);

    @Update("update reply set thumbs = thumbs + 1 where id = #{id}")
    void addReplyThumbs(int id);

    @Update("update reply set thumbs = thumbs - 1 where id = #{id}")
    void delReplyThumbs(int id);
}
