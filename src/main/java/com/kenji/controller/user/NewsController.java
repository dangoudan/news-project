package com.kenji.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenji.domain.*;
import com.kenji.service.*;
import com.kenji.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class NewsController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private NewsTagsMappingService newsTagsMappingService;
    @Autowired
    private ViewRecordService viewRecordService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @RequestMapping("/toDetail")
    public String toDetail(HttpServletRequest request, Model model, int id) {
        News news = newsService.getNewsById(id);
        List<Tags> tagsList = tagsService.getAllTags();
        for(int i = 0; i < tagsList.size(); i++) {
            if(i == 0) {
                tagsList.get(i).setTag("首页");
            }
            if(tagsList.get(i).getTag().equals("西安")) {
                tagsList.remove(i);
                i--;
            }
        }
        model.addAttribute("news", news);
        model.addAttribute("newsJson", JSON.toJSONString(news));
        model.addAttribute("tagsList", tagsList);
        newsService.addNewsViews(id);//views + 1
        Cookie[] cookies = request.getCookies();
        String userId = CookieUtil.getCookieByName(cookies,"userId");//用户id
        if(userId != null) {
            ViewRecord viewRecord = new ViewRecord();
            viewRecord.setNewsId(id);
            viewRecord.setUserId(Integer.parseInt(userId));
            viewRecord.setCtime(TimeUtil.getNow());
            viewRecordService.addViewRecord(viewRecord);
        }
        return "detail";
    }

    @ResponseBody
    @RequestMapping("/getRelation")
    public List<News> getRelation(int id) {
        List<Integer> tagIds = newsTagsMappingService.getTagsIdByNewsId(id);
        List<Integer> newsIdList = new ArrayList<>();
        for (int tagId : tagIds) {
            List<Integer> newsIds = newsTagsMappingService.getNewsIdByTagsId(tagId);
            for (int newsId : newsIds) {
                if(!newsIdList.contains(newsId)) {
                    newsIdList.add(newsId);
                }
            }
        }
        List<News> newsList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            int ran = (int) (Math.random() * newsIdList.size());
            News news = newsService.getNewsById(newsIdList.get(ran));
            if(newsList.contains(news) || news.getId() == id || news.getPicUrl() == null) {
                i--;
                continue;
            }
            newsList.add(news);
        }
        return newsList;
    }

    @ResponseBody
    @RequestMapping("/getRecommend")
    public List<News> getRecommend(int id, int offset, int size) {
        List<Integer> tagIds = newsTagsMappingService.getTagsIdByNewsId(id);
        List<News> newsList = newsService.getNewsByRelation(tagIds, offset, size);
        for(int i = 0; i < newsList.size(); i++) {
            if(newsList.get(i).getId() == id) {
                newsList.remove(i);
                break;
            }
        }
        return newsList;
    }

    @ResponseBody
    @RequestMapping("/getRecommendCount")
    public int getRecommendCount(int id) {
        List<Integer> tagIds = newsTagsMappingService.getTagsIdByNewsId(id);
        List<Integer> newsIdList = new ArrayList<>();
        for(int tagId : tagIds) {
            List<Integer> newsIds = newsTagsMappingService.getNewsIdByTagsId(tagId);
            for (int newsId : newsIds) {
                if(!newsIdList.contains(newsId) && newsId != id) {
                    newsIdList.add(newsId);
                }
            }
        }
        return newsIdList.size();
    }

    @RequestMapping({"", "/toNewsList"})
    public String toNewsList(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        int tagsId = tagsService.getIdByTag("校园资讯");
        List<Integer> newsId = newsTagsMappingService.getNewsIdByTagsId(tagsId);
        List<Map<String, Integer>> mapList = new ArrayList<>();
        for(int id : newsId) {
            int commentCount = commentService.getCommentCountByNewsId(id);
            Map<String, Integer> map = new HashMap<>();
            map.put("newsId", id);
            map.put("commentCount", commentCount);
            mapList.add(map);
        }
        List<Object> objectList = InsertSortUtil.insertSort1(mapList);
        List<News> hotCommentNews = new ArrayList<>();//热评
        for(Object obj : objectList) {
            int id = ((Map<String, Integer>) obj).get("newsId");
            News news = newsService.getNewsById(id);
            if(hotCommentNews.size() < 15 && news.getPicUrl() != null) {
                hotCommentNews.add(news);
            }
        }
        List<News> newsList = newsService.getNewsByTagsThumbs(tagsId, 0, 5);//热点
        List<News> topNews = newsService.getNewsByTagsIsTop(tagsId, 0, 5);//顶置
        List<Tags> tags = tagsService.getAllTags();
        String date = TimeUtil.getDate(TimeUtil.getNow());
        String cookieValue = CookieUtil.getCookieByName(request.getCookies(),"tags");
        if(cookieValue == null) {
            String tagsJson = URLEncoder.encode(JSON.toJSONString(tags),"UTF-8");//进行中文编码 后端用URLDecode解码 js用decodeURIComponent
            Cookie cookie = new Cookie("tags", tagsJson);
            cookie.setMaxAge(60 * 60);
            cookie.setPath("/user");
            response.addCookie(cookie);
        }
        model.addAttribute("hotCommentNews", hotCommentNews);
        model.addAttribute("topNews", topNews);
        model.addAttribute("newsList", newsList);
        model.addAttribute("date", date);
        return "newsList";
    }

    @ResponseBody
    @RequestMapping("/allNews")
    public List<Tags> allNew() {
        List<Tags> tagsList = tagsService.getAllTags();
        return tagsList;
    }

    @RequestMapping("/findNews")
    public String findNews(Model model, String name) {
        int tagId = tagsService.getIdByTag(name);
        List<Integer> newsId = newsTagsMappingService.getNewsIdByTagsId(tagId);
        List<News> newsList = new ArrayList<>();
        for(int id : newsId) {
            News news = newsService.getNewsById(id);
            newsList.add(news);
        }
        model.addAttribute("newsList", newsList);
        return "newsList";
    }

    @ResponseBody
    @RequestMapping("/getCount")
    public int getCount() {
        return newsService.getNewsCount();
    }

    @ResponseBody
    @RequestMapping("/getNewsTagsCount")
    public int getNewsTagsCount() {
        int tagsId = tagsService.getIdByTag("校园资讯");
        return newsTagsMappingService.getNewsIdByTagsId(tagsId).size();
    }

    @RequestMapping("/setPage")
    public void setPage(int offset, int size, HttpServletResponse response) throws IOException {
        int tagsId = tagsService.getIdByTag("校园资讯");
        List<News> newsList = newsService.getNewsByTagsCtime(tagsId, offset, size);
        List<Integer> commentCounts = new ArrayList<>();
        for(News news : newsList){
            commentCounts.add(commentService.getCommentCountByNewsId(news.getId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commentCounts", commentCounts);
        jsonObject.put("newsList", newsList);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK,"查询成功", jsonObject.toJSONString());
    }


}
