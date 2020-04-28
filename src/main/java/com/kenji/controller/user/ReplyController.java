package com.kenji.controller.user;

import com.kenji.domain.Reply;
import com.kenji.domain.User;
import com.kenji.service.ReplyService;
import com.kenji.service.UserService;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/user")
public class ReplyController {

    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;

    @RequestMapping("/addReply")
    public void addReply(@CookieValue("userId") int userId, int commentId, String content, HttpServletResponse response) throws IOException {
        User user = userService.getUserById(userId);
        Reply reply = new Reply(0, user.getAccount(), null, commentId, content, 0, TimeUtil.getNow());
        replyService.addReply(reply);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"发布成功",null);
    }

    @RequestMapping("/addReply2Replied")
    public void addReply2Replied(@CookieValue("userId") int userId, String repliedName, int commentId, String content, HttpServletResponse response) throws IOException {
        User user = userService.getUserById(userId);
        Reply reply = new Reply(0, user.getAccount(), repliedName, commentId, content, 0, TimeUtil.getNow());
        replyService.addReplyToReplied(reply);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"发布成功",null);
    }

    @ResponseBody
    @RequestMapping("/find2Reply")
    public List<Reply> find2Reply(int commentId, int offset, int size) {
        return replyService.getNotRepliedByPage(commentId, offset, size);
    }

    @ResponseBody
    @RequestMapping("/getLatestReply")
    public Reply getLatestReply(@CookieValue("userId") int userId) {
        User user = userService.getUserById(userId);
        List<Reply> reply = replyService.getLatestReply(user.getAccount());
        return reply.get(0);
    }

    @ResponseBody
    @RequestMapping("/getReplyCount")
    public int getReplyCount(int commentId) {
        return replyService.getReplyCount(commentId);
    }

    @ResponseBody
    @RequestMapping("/getAllReply")
    public List<Reply> getAllReply(int commentId, int offset, int size) {
        return replyService.getReplyByPage(commentId, offset, size);
    }

}
