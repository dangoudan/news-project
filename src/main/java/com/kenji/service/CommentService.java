package com.kenji.service;

import com.kenji.domain.Comment;

import java.util.List;

public interface CommentService {

    void addComment(Comment comment);

    List<Comment> getCommentByNewsId(int newsId);

    List<Comment> getCommentPageByUserId(int userId, int offset, int size);

    List<Comment> getCommentPageByNewsId(int newsId, int offset, int size);

    List<Comment> getCommentPageByCtime(int newsId, int offset, int size);

    void addCommentThumbs(int id);

    void delCommentThumbs(int id);

    int getCommentCountByNewsId(int newsId);

    int getCommentCountByUserId(int userId);

    String getCommentContentById(int id);

    List<Comment> getLatestComment(int userId);

    int getNewsIdByCommentId(int id);

    void delComment(int id);
}
