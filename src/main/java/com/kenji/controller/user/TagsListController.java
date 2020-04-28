package com.kenji.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenji.domain.News;
import com.kenji.domain.Tags;
import com.kenji.service.CommentService;
import com.kenji.service.NewsService;
import com.kenji.service.NewsTagsMappingService;
import com.kenji.service.TagsService;
import com.kenji.util.CookieUtil;
import com.kenji.util.ResponseUtil;
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
import java.util.List;

@Controller
@RequestMapping("/user")
public class TagsListController {

    @Autowired
    private TagsService tagsService;
    @Autowired
    private NewsTagsMappingService newsTagsMappingService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping("/getTagsList")
    public List<Tags> getTagsList() {
        List<Tags> tagsList = tagsService.getAllTags();
        return tagsList;
    }

    @RequestMapping("/toNewsTagsList")
    public String toNewsTagsList(int tagsId, Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//        List<News> newsList = newsService.getNewsByTagsCtime(tagsId, 0, 10);
//        List<News> hotNews = newsService.getNewsByTagsThumbs(tagsId, 0, 5);
//        List<Integer> commentCounts = new ArrayList<>();
//        for(News news : newsList){
//            commentCounts.add(commentService.getCommentCountByNewsId(news.getId()));
//        }
//        List<News> newsJson = newsService.getAllNews();
        List<News> topNews = newsService.getNewsByTagsIsTopOne(tagsId, 1, 0, 3);
        List<Tags> tags = tagsService.getAllTags();
        String cookieValue = CookieUtil.getCookieByName(request.getCookies(),"tags");
        if(cookieValue == null) {
            String tagsJson = URLEncoder.encode(JSON.toJSONString(tags),"UTF-8");//进行中文编码 后端用URLDecode解码 js用decodeURIComponent
            Cookie cookie = new Cookie("tags", tagsJson);
            cookie.setPath("/user");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);
        }
//        model.addAttribute("commentCounts", commentCounts);
        model.addAttribute("tagsId", tagsId);
        model.addAttribute("topNews", topNews);
//        model.addAttribute("newsList", newsList);
//        model.addAttribute("hotNews", hotNews);
//        model.addAttribute("newsJson", newsJson);
        return "newsTagsList";
    }

    @ResponseBody
    @RequestMapping("/getCountByTagsId")
    public int getCountByTagsId(int tagsId) {
        return newsTagsMappingService.getNewsIdByTagsId(tagsId).size();
    }

    @ResponseBody
    @RequestMapping("/hotTagsPage")
    public List<News> hotTagsPage(int tagsId, int offset, int size) {
        List<News> newsList = newsService.getNewsByTagsThumbs(tagsId, offset, size);
        List<News> news = new ArrayList<>();
        for(int i = 0; i < newsList.size(); i++) {
            if(newsList.get(i).getPicUrl() == null) {
                continue;
            }
            news.add(newsList.get(i));
        }
        return news;
    }

    @RequestMapping("/tagsPage")
    public void tagsPage(int tagsId, int offset, int size, HttpServletResponse response) throws IOException {
        List<News> newsList = newsService.getNewsByTagsCtime(tagsId, offset, size);
        List<News> topNews = newsService.getNewsByTagsIsTopOne(tagsId, 1, 0, 3);
        for(int i = 0; i < topNews.size(); i++) {
            for(int j = 0; j < newsList.size(); j++) {
                if(topNews.get(i).getId() == newsList.get(j).getId()) {
                    newsList.remove(j);
                    break;
                }
            }
        }
        List<Integer> commentCounts = new ArrayList<>();
        for(News news : newsList){
            commentCounts.add(commentService.getCommentCountByNewsId(news.getId()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newsList", newsList);
        jsonObject.put("commentCounts", commentCounts);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK,"查询成功", jsonObject.toJSONString());
    }


}
