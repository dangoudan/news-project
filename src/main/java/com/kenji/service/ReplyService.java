package com.kenji.service;

import com.kenji.domain.Reply;

import java.util.List;

public interface ReplyService {

    void addReply(Reply reply);

    void addReplyToReplied(Reply reply);

    List<Reply> getNotRepliedByPage(int commentId, int offset, int size);

    List<Reply> getReplyByPage(int commentId, int offset, int size);

    List<Reply> getLatestReply(String userName);

    List<Reply> getLatestCommentReply(String userName, List<Integer> commentId);

    int getReplyCount(int commentId);

    int getUserReplyCount(String userName);

    String getReplyContentById(int id);

    void delReply(int id);

    void delReplyByCommentId(int commentId);

    void addReplyThumbs(int id);

    void delReplyThumbs(int id);

    void updateUserName(String userName, String oldUserName);

    void updateRepliedName(String repliedName, String oldRepliedName);
}
