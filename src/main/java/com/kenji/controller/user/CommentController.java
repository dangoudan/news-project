package com.kenji.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.kenji.domain.Comment;
import com.kenji.domain.News;
import com.kenji.domain.Reply;
import com.kenji.domain.User;
import com.kenji.service.CommentService;
import com.kenji.service.NewsService;
import com.kenji.service.ReplyService;
import com.kenji.service.UserService;
import com.kenji.util.InsertSortUtil;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/user")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private NewsService newsService;

    @RequestMapping("/addComment")
    public void addComment(@CookieValue("userId") int userId, int newsId, String content, HttpServletResponse response) throws IOException {
        Comment comment = new Comment(0, newsId, userId, content, TimeUtil.getNow(), 0, 0);
        commentService.addComment(comment);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"发布成功",null);
    }

    @ResponseBody
    @RequestMapping("/findOneComment")
    public void findOneComment(int newsId, int offset, int size, String type, HttpServletResponse response) throws IOException {
        List<Comment> commentList = null;
        if("hot".equals(type)) {
            commentList = commentService.getCommentPageByNewsId(newsId, offset, size);
        }else {
            commentList = commentService.getCommentPageByCtime(newsId, offset, size);
        }
        if(commentList.size() == 1) {
            Comment comment = commentList.get(0);
            User user = userService.getUserById(comment.getUserId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comment", comment);
            jsonObject.put("user", user);
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", jsonObject.toJSONString());
        }else {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "没有数据", null);
        }
    }

    @ResponseBody
    @RequestMapping("/getMoreComment")
    public String getMoreComment(int newsId, int offset, int size, String type) {
        List<Comment> commentList = null;
        if("hot".equals(type)) {
            commentList = commentService.getCommentPageByNewsId(newsId, offset, size);
        }else {
            commentList = commentService.getCommentPageByCtime(newsId, offset, size);
        }
        JSONObject jsonObject = new JSONObject();
        List<User> userList = new ArrayList<>();
        for(Comment comment : commentList) {
            User user = userService.getUserById(comment.getUserId());
            userList.add(user);
        }
        jsonObject.put("commentList", commentList);
        jsonObject.put("userList", userList);
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/getLatestComment")
    public String getLatestComment(@CookieValue("userId") int userId) {
        User user = userService.getUserById(userId);
        List<Comment> comments = commentService.getLatestComment(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("comment", comments.get(0));
        jsonObject.put("user", user);
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/getCommentCount")
    public int getCommentCount(int newsId) {
        return commentService.getCommentCountByNewsId(newsId);
    }

    @ResponseBody
    @RequestMapping("/getUserCommentCount")
    public int getUserCommentCount(@CookieValue("userId") int userId) {
        int replyCount = replyService.getUserReplyCount(userService.getUserById(userId).getAccount());
        int commentCount = commentService.getCommentCountByUserId(userId);
        return replyCount + commentCount;
    }

    @ResponseBody
    @RequestMapping("/getUserComment")
    public List<Comment> getUserComment(@CookieValue("userId") int userId) {
        return commentService.getLatestComment(userId);
    }

    @ResponseBody
    @RequestMapping("/getUserCommentAndReply")
    public List<Object> getUserCommentAndReply(@CookieValue("userId") int userId) {
        User user = userService.getUserById(userId);
        List<Reply> replyList = replyService.getLatestReply(user.getAccount());
        List<Comment> commentList = commentService.getLatestComment(userId);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(int i = 0; i < replyList.size(); i++) {
            Map<String, Object> objectMap = new HashMap<>();
            int newsId = commentService.getNewsIdByCommentId(replyList.get(i).getCommentId());
            objectMap.put("newsId", newsId);
            objectMap.put("title", newsService.getNewsById(newsId).getTitle());
            objectMap.put("content", replyList.get(i).getContent());
            objectMap.put("id", replyList.get(i).getId());
            objectMap.put("commentOrReply", 1); // comment 0表示， reply 1表示
            objectMap.put("ctime", replyList.get(i).getCtime());
            mapList.add(objectMap);
        }

        for(int i = 0; i < commentList.size(); i++) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("newsId", commentList.get(i).getNewsId());
            objectMap.put("title", newsService.getNewsById(commentList.get(i).getNewsId()).getTitle());
            objectMap.put("content", commentList.get(i).getContent());
            objectMap.put("id", commentList.get(i).getId());
            objectMap.put("commentOrReply", 0); // comment 0表示， reply 1表示
            objectMap.put("ctime", commentList.get(i).getCtime());
            mapList.add(objectMap);
        }

        return InsertSortUtil.insertSort(mapList);
    }

    @ResponseBody
    @RequestMapping("/getRepliedUserMsg")
    public List<Map<String, Object>> getRepliedUserMsg(@CookieValue("userId") int userId) {
        User user = userService.getUserById(userId);
        List<Integer> commentIdList = commentService.getCommentId(userId);
        if(commentIdList.size() == 0) {
            commentIdList.add(0);
        }
        List<Reply> replyList = replyService.getLatestCommentReply(user.getAccount(), commentIdList);

        List<Map<String, Object>> maps = new ArrayList<>();
        for(Reply reply : replyList) {
            int newsId = commentService.getNewsIdByCommentId(reply.getCommentId());
            String title = commentService.getContentById(reply.getCommentId());
            Map<String, Object> map = new HashMap<>();
            map.put("username", reply.getUserName());
            map.put("title", title);
            map.put("newsId", newsId);
            map.put("content", reply.getContent());
            map.put("id", reply.getId());
            map.put("ctime", reply.getCtime());
            maps.add(map);
        }
        return maps;
    }

    @ResponseBody
    @RequestMapping("/getRepliedUserCount")
    public int getRepliedUserCount(@CookieValue("userId") int userId) {
        User user = userService.getUserById(userId);
        List<Integer> commentIdList = commentService.getCommentId(userId);
        if(commentIdList.size() == 0) {
            commentIdList.add(0);
        }
        return replyService.getLatestCommentReply(user.getAccount(), commentIdList).size();
    }

    @ResponseBody
    @RequestMapping("/delComment")
    public String delComment(int commentOrReply, int id) {
        if(commentOrReply == 0) {
            commentService.delComment(id);
            replyService.delReplyByCommentId(id);
            return "OK";
        }else if(commentOrReply == 1) {
            replyService.delReply(id);
            return "OK";
        }
        return "FAIL";
    }
}
