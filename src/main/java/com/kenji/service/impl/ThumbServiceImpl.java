package com.kenji.service.impl;

import com.kenji.dao.ThumbDao;
import com.kenji.domain.Thumb;
import com.kenji.service.ThumbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThumbServiceImpl implements ThumbService {

    @Autowired
    private ThumbDao thumbDao;


    @Override
    public void addThumbToNews(Thumb thumb) {
        thumbDao.addThumbToNews(thumb);
    }

    @Override
    public void addThumbToComment(Thumb thumb) {
        thumbDao.addThumbToComment(thumb);
    }

    @Override
    public void addThumbToReply(Thumb thumb) {
        thumbDao.addThumbToReply(thumb);
    }

    @Override
    public Thumb findOneNewsThumb(int userId, int newsId) {
        return thumbDao.findOneNewsThumb(userId, newsId);
    }

    @Override
    public Thumb findOneCommentThumb(int userId, int commentId) {
        return thumbDao.findOneCommentThumb(userId, commentId);
    }

    @Override
    public Thumb findOneReplyThumb(int userId, int replyId) {
        return thumbDao.findOneReplyThumb(userId, replyId);
    }

    @Override
    public void delThumbToNews(int userId, int newsId) {
        thumbDao.delThumbToNews(userId, newsId);
    }

    @Override
    public void delThumbToComment(int userId, int commentId) {
        thumbDao.delThumbToComment(userId, commentId);
    }

    @Override
    public void delThumbToReply(int userId, int replyId) {
        thumbDao.delThumbToReply(userId, replyId);
    }
}
