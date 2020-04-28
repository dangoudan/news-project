package com.kenji.controller.user;

import com.alibaba.fastjson.JSON;
import com.kenji.domain.News;
import com.kenji.domain.Tags;
import com.kenji.service.NewsService;
import com.kenji.service.TagsService;
import com.kenji.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class SearchController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private TagsService tagsService;

    @RequestMapping("/toSearch")
    public String toSearch(String keyword, Model model){
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
        model.addAttribute("keyword", keyword);
        model.addAttribute("tagsList", tagsList);
        return "search";
    }

    @RequestMapping("/search")
    public void search(String keyword, int offset, int size, String type, HttpServletResponse response) throws Exception {
        List<News> newsList = null;
        if("normal".equals(type)) {
            newsList = newsService.getNewsByBlurNamePage(keyword, offset, size);
        }else if("hot".equals(type)) {
            newsList = newsService.getNewsByBlurNameHotPage(keyword, offset, size);
        }else {
            newsList = newsService.getNewsByBlurNameLatestPage(keyword, offset, size);
        }
        if(newsList != null && newsList.size() > 0){
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK,"查询成功", JSON.toJSONString(newsList));
        }else {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL,"没有此类文章",null);
        }
    }

    @ResponseBody
    @RequestMapping("/getBlurNameCount")
    public int getCount(String keyword) {
        return newsService.getNewsByBlurNameCount(keyword);
    }

}
