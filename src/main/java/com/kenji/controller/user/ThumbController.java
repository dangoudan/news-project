package com.kenji.controller.user;

import com.kenji.domain.Thumb;
import com.kenji.service.CommentService;
import com.kenji.service.NewsService;
import com.kenji.service.ReplyService;
import com.kenji.service.ThumbService;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class ThumbController {

    @Autowired
    private ThumbService thumbService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;

    @RequestMapping("/addThumbToNews")
    public void addThumbToNews(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws IOException {
        Thumb thumb = new Thumb();
        thumb.setUserId(userId);
        thumb.setNewsId(newsId);
        thumb.setCtime(TimeUtil.getNow());
        thumbService.addThumbToNews(thumb);
        newsService.addNewsThumbs(newsId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"点赞成功",null);
    }

    @RequestMapping("/addThumbToComment")
    public void addThumbToComment(@CookieValue("userId") int userId, int commentId, HttpServletResponse response) throws IOException {
        Thumb thumb = new Thumb();
        thumb.setUserId(userId);
        thumb.setCommentId(commentId);
        thumb.setCtime(TimeUtil.getNow());
        thumbService.addThumbToComment(thumb);
        commentService.addCommentThumbs(commentId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"点赞成功",null);
    }

    @RequestMapping("/addThumbToReply")
    public void addThumbToReply(@CookieValue("userId") int userId, int replyId, HttpServletResponse response) throws IOException {
        Thumb thumb = new Thumb();
        thumb.setUserId(userId);
        thumb.setReplyId(replyId);
        thumb.setCtime(TimeUtil.getNow());
        thumbService.addThumbToReply(thumb);
        replyService.addReplyThumbs(replyId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"点赞成功",null);
    }

    @RequestMapping("/findOneNewsThumb")
    public void findOneNewsThumb(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws IOException {
        Thumb thumb = thumbService.findOneNewsThumb(userId, newsId);
        if(thumb != null) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"已点赞",null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"未点赞",null);
        }
    }

    @RequestMapping("/findOneCommentThumb")
    public void findOneCommentThumb(@CookieValue("userId") int userId, int commentId, HttpServletResponse response) throws IOException {
        Thumb thumb = thumbService.findOneCommentThumb(userId, commentId);
        if(thumb != null) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"已点赞",null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"未点赞",null);
        }
    }

    @RequestMapping("/findOneReplyThumb")
    public void findOneReplyThumb(@CookieValue("userId") int userId, int replyId, HttpServletResponse response) throws IOException {
        Thumb thumb = thumbService.findOneReplyThumb(userId, replyId);
        if(thumb != null) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"已点赞",null);
        }else {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"未点赞",null);
        }
    }

    @RequestMapping("/delThumbToNews")
    public void delThumbToNews(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws Exception {
        thumbService.delThumbToNews(userId, newsId);
        newsService.delNewsThumbs(newsId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"取消成功",null);
    }

    @RequestMapping("/delThumbToComment")
    public void delThumbToComment(@CookieValue("userId") int userId, int commentId, HttpServletResponse response) throws Exception {
        thumbService.delThumbToComment(userId, commentId);
        commentService.delCommentThumbs(commentId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"取消成功",null);
    }

    @RequestMapping("/delThumbToReply")
    public void delThumbToReply(@CookieValue("userId") int userId, int replyId, HttpServletResponse response) throws Exception {
        thumbService.delThumbToReply(userId, replyId);
        replyService.delReplyThumbs(replyId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"取消成功",null);
    }

}
