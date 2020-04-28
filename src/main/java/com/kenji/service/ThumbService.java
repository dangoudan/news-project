package com.kenji.service;

import com.kenji.domain.Thumb;

public interface ThumbService {

    void addThumbToNews(Thumb thumb);

    void addThumbToComment(Thumb thumb);

    void addThumbToReply(Thumb thumb);

    Thumb findOneNewsThumb(int userId, int newsId);

    Thumb findOneCommentThumb(int userId, int commentId);

    Thumb findOneReplyThumb(int userId, int replyId);

    void delThumbToNews(int userId, int newsId);

    void delThumbToComment(int userId, int commentId);

    void delThumbToReply(int userId, int replyId);

}
