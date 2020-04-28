package com.kenji.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kenji.domain.News;
import com.kenji.service.NewsService;
import com.kenji.service.NewsTagsMappingService;
import com.kenji.service.TagsService;
import com.kenji.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("adminSearchController")
@RequestMapping("/admin")
public class SearchController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsTagsMappingService newsTagsMappingService;
    @Autowired
    private TagsService tagsService;

    @RequestMapping("/search")
    public void search(String keyword, int offset, int size, HttpServletResponse response) throws Exception {
        List<News> newsList = newsService.getNewsByBlurNamePage(keyword, offset, size);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newsList", newsList);
        Map<String, List<String>> map = new HashMap<>();
        for(int i = 0; i < newsList.size(); i++) {
            //查出一条新闻的对应标签
            List<String> tagsList = new ArrayList<>();
            News news = newsList.get(i);
            List<Integer> tagsIds = newsTagsMappingService.getTagsIdByNewsId(news.getId());
            for(int tagsId : tagsIds) {
                String tagName = tagsService.getTagById(tagsId);
                tagsList.add(tagName);
            }
            map.put("result" + i, tagsList);
        }
        jsonObject.put("map", map);
        if(newsList.size() > 0){
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", jsonObject.toJSONString());
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
