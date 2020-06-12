package com.kenji.service.impl;

import com.kenji.dao.ReplyDao;
import com.kenji.domain.Reply;
import com.kenji.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyDao dao;

    @Override
    public void addReply(Reply reply) {
        dao.addReply(reply);
    }

    @Override
    public void addReplyToReplied(Reply reply) {
        dao.addReplyToReplied(reply);
    }

    @Override
    public List<Reply> getNotRepliedByPage(int commentId, int offset, int size) {
        return dao.getNotRepliedByPage(commentId, offset, size);
    }

    @Override
    public List<Reply> getReplyByPage(int commentId, int offset, int size) {
        return dao.getReplyByPage(commentId, offset, size);
    }

    @Override
    public List<Reply> getLatestReply(String userName) {
        return dao.getLatestReply(userName);
    }

    @Override
    public List<Reply> getLatestCommentReply(String userName, List<Integer> commentId) {
        return dao.getLatestCommentReply(userName, commentId);
    }

    @Override
    public int getReplyCount(int commentId) {
        return dao.getReplyCount(commentId);
    }

    @Override
    public int getUserReplyCount(String userName) {
        return dao.getUserReplyCount(userName);
    }

    @Override
    public String getReplyContentById(int id) {
        return dao.getReplyContentById(id);
    }

    @Override
    public void delReply(int id) {
        dao.delReply(id);
    }

    @Override
    public void delReplyByCommentId(int commentId) {
        dao.delReplyByCommentId(commentId);
    }

    @Override
    public void addReplyThumbs(int id) {
        dao.addReplyThumbs(id);
    }

    @Override
    public void delReplyThumbs(int id) {
        dao.delReplyThumbs(id);
    }

    @Override
    public void updateUserName(String userName, String oldUserName) {
        dao.updateUserName(userName, oldUserName);
    }

    @Override
    public void updateRepliedName(String repliedName, String oldRepliedName) {
        dao.updateRepliedName(repliedName, oldRepliedName);
    }
}
