package com.kenji.service.impl;

import com.kenji.dao.CommentDao;
import com.kenji.domain.Comment;
import com.kenji.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao dao;

    @Override
    public void addComment(Comment comment) {
        dao.addComment(comment);
    }

    @Override
    public List<Comment> getCommentByNewsId(int newsId) {
        return dao.getCommentByNewsId(newsId);
    }

    @Override
    public List<Comment> getCommentPageByUserId(int userId, int offset, int size) {
        return dao.getCommentPageByUserId(userId, offset, size);
    }

    @Override
    public List<Comment> getCommentPageByNewsId(int newsId, int offset, int size) {
        return dao.getCommentPageByNewsId(newsId, offset, size);
    }

    @Override
    public List<Comment> getCommentPageByCtime(int newsId, int offset, int size) {
        return dao.getCommentPageByCtime(newsId, offset, size);
    }

    @Override
    public String getContentById(int id) {
        return dao.getContentById(id);
    }

    @Override
    public void addCommentThumbs(int id) {
        dao.addCommentThumbs(id);
    }

    @Override
    public void delCommentThumbs(int id) {
        dao.delCommentThumbs(id);
    }

    @Override
    public int getCommentCountByNewsId(int newsId) {
        return dao.getCommentCountByNewsId(newsId);
    }

    @Override
    public int getCommentCountByUserId(int userId) {
        return dao.getCommentCountByUserId(userId);
    }

    @Override
    public String getCommentContentById(int id) {
        return dao.getCommentContentById(id);
    }

    @Override
    public List<Comment> getLatestComment(int userId) {
        return dao.getLatestComment(userId);
    }

    @Override
    public List<Integer> getCommentId(int userId) {
        return dao.getCommentId(userId);
    }

    @Override
    public int getNewsIdByCommentId(int id) {
        return dao.getNewsIdByCommentId(id);
    }

    @Override
    public void delComment(int id) {
        dao.delComment(id);
    }
}
