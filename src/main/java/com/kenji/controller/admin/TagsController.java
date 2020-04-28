package com.kenji.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.kenji.domain.NewsTagsMapping;
import com.kenji.domain.Tags;
import com.kenji.service.NewsTagsMappingService;
import com.kenji.service.TagsService;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagsController {

    @Autowired
    private TagsService service;
    @Autowired
    private NewsTagsMappingService newsTagsMappingService;

    @RequestMapping("/toAddTags")
    public String toAddTags() {
        return "addTags";
    }

    @RequestMapping("/toTagsList")
    public String toTagsList() {
        return "tagsList";
    }

    @RequestMapping("/addTags")
    public void addTags(String tag, int tagType, HttpServletResponse response) throws IOException {
        Tags tags = new Tags(0, tag, tagType, TimeUtil.getNow());
        try {
            service.save(tags);
        } catch (Exception e) {
            ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.FAIL,"标签已存在","");
            return;
        }
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"添加成功","");
    }

    @ResponseBody
    @RequestMapping("/allTags")
    public List<Tags> getAllTags() {
        return service.getAllTags();
    }

    @ResponseBody
    @RequestMapping("/getTagsByPage")
    public List<Tags> getTagsByPage(int offset, int size) {
        return service.getTagsByPage(offset, size);
    }

    @ResponseBody
    @RequestMapping("/deleteTags")
    public String deleteTagsById(int id) {
        service.deleteTagsById(id);
        newsTagsMappingService.deleteMappingByTagsId(id);
        return "tagsList";
    }

    @ResponseBody
    @RequestMapping("/updateTagsList")
    public String updateTagsList(String tagsList) {
        List<String> tagNames = JSONObject.parseArray(tagsList, String.class);//将json字符串转化为list对象
        List<Tags> allTags = service.getAllTags();
        boolean flag = false;
        for(int i = 0; i < tagNames.size(); i++) {
            if(!allTags.get(i).getTag().equals(tagNames.get(i))) {
                flag = true;
                break;
            }
        }
        if(!flag) {
            return "NO";
        }
        List<Tags> newTags = new ArrayList<>();
        for(int i = 0; i < tagNames.size(); i++) {
            for(int j = 0; j < allTags.size(); j++) {
                if (allTags.get(j).getTag().equals(tagNames.get(i))) {
                    service.updateIds(allTags.get(j).getId());
                    newsTagsMappingService.updateTagsId(allTags.get(i).getId() + 100, allTags.get(i).getId());
                }
            }
        }
        for(String tagName : tagNames) {
            newTags.add(service.getTagsByTag(tagName));
        }
        System.out.println(newTags);
        for(int i = 0; i < newTags.size(); i++) {
            service.updateId(i + 1, newTags.get(i).getId());
            newsTagsMappingService.updateTagsId(i + 1, newTags.get(i).getId());
        }
        return "YES";
    }

    @ResponseBody
    @RequestMapping("/getTagsCount")
    public int getTagsCount() {
        return service.getTagsCount();
    }

}
