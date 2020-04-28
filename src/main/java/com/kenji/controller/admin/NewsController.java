package com.kenji.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kenji.domain.News;
import com.kenji.domain.NewsTagsMapping;
import com.kenji.service.*;
import com.kenji.util.FileUploadUtil;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("adminNewsController")
@RequestMapping("/admin")
public class NewsController {

    private static String[] suffixes = new String[]{"jpg", "jpeg", "png"};
    private static long limitSize = 2 * 1024 * 1024;//2M大小

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsTagsMappingService newsTagsMappingService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private ReportService reportService;

    @RequestMapping("/toList")
    public String toList(@CookieValue("adminId") int adminId, Model model) {
        String name = adminService.getAccountById(adminId);
        model.addAttribute("name", name);
        return "list";
    }

    @RequestMapping("/toIndex")
    public String toIndex() {
        return "index";
    }

    @RequestMapping("/toAddNews")
    public String toAddNews() {
        return "addNews";
    }

    @RequestMapping("/toAdminNewsList")
    public String toAdminNewsList() {
        return "adminNewsList";
    }

    @RequestMapping("/toUpdateNews")
    public String toUpdateNews(String id, Model model) {
        model.addAttribute("id", id);
        return "updateNews";
    }

    @RequestMapping("/toReport")
    public String toReport(Model model) {
        int count = reportService.getReportCount();
        model.addAttribute("count", count);
        return "report";
    }

    @RequestMapping("/addNews")
    public void addNews(HttpServletRequest request,
                          @CookieValue("adminId") String adminId,
                          HttpServletResponse response) throws IOException{
        try {
            Map<String, String> params = FileUploadUtil.uploadFile(request, suffixes, limitSize, 10);

            String title = params.get("title");
            String msgContent = params.get("msgContent");
            String isTop = params.get("isTop");
            String pic = params.get("pic");
            String picUrl = null;
            if(pic != null) {
                JSONArray jsonArray = JSON.parseArray(pic);
                picUrl = jsonArray.getString(0);
            }
            String contentUrl = params.get("contentUrl");

            String account = adminService.getAccountById(Integer.parseInt(adminId));

            News news = new News();
            news.setTitle(title);
            news.setContent(msgContent);
            news.setAuthor(account);
            news.setIsTop(Integer.parseInt(isTop));
            news.setPicUrl(picUrl);
            news.setContentUrl(contentUrl);
            news.setThumbs(0);
            news.setViews(0);
            news.setCtime(TimeUtil.getNow());

            int newsId = newsService.create(news);//获得当前newsId

            //将bookId和tagsId存入mapping中
            String tags = params.get("tags");
            List<String> tagsIds = JSONObject.parseArray(tags, String.class);//将json字符串转化为list对象
            for(String tag : tagsIds) {
                if(tag != null) {
                    int tagsId = Integer.parseInt(tag);
                    NewsTagsMapping newsTagsMapping = new NewsTagsMapping(0, newsId, tagsId, TimeUtil.getNow());
                    newsTagsMappingService.addNewsTagsMapping(newsTagsMapping);
                }
            }
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "上传成功", null);
        } catch (FileUploadUtil.SuffixNotMatchException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "后缀不符合要求", null);
        } catch (FileUploadUtil.OutOfLimitSizeException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件大小限制", null);
        } catch (FileUploadUtil.OutOfLimitFileNumException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件数量限制", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getNewsByCtimeDesc")
    public void getNewsByCtimeDesc(int offset, int size, HttpServletResponse response) throws Exception {
        List<News> result = newsService.getNewsByCtimeDesc(offset, size);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newsList", result);
        Map<String, List<String>> map = new HashMap<>();
        for(int i = 0; i < result.size(); i++) {
            News news = result.get(i);
            //查出一条新闻的对应标签
            List<Integer> tagsIds = newsTagsMappingService.getTagsIdByNewsId(news.getId());
            List<String> tagsList = new ArrayList<>();
            for(int tagsId : tagsIds) {
                String tagName = tagsService.getTagById(tagsId);
                tagsList.add(tagName);
            }
            map.put("result" + i, tagsList);
        }
        jsonObject.put("map", map);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", jsonObject.toJSONString());
    }

    @RequestMapping("/getNewsByCtimeAsc")
    public void getNewsByCtimeAsc(int offset, int size, HttpServletResponse response) throws Exception {
        List<News> newsList = newsService.getNewsByCtimeAsc(offset, size);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("newsList", newsList);
        Map<String, List<String>> map = new HashMap<>();
        for(int i = 0; i < newsList.size(); i++) {
            News news = newsList.get(i);
            //查出一条新闻的对应标签
            List<String> tagsList = new ArrayList<>();
            List<Integer> tagsIds = newsTagsMappingService.getTagsIdByNewsId(news.getId());
            for(int tagsId : tagsIds) {
                String tagName = tagsService.getTagById(tagsId);
                tagsList.add(tagName);
            }
            map.put("result" + i, tagsList);
        }
        jsonObject.put("map", map);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", jsonObject.toJSONString());
    }

    @RequestMapping("/deleteNews")
    public void deleteNews(int id, HttpServletResponse response) throws Exception {
        newsService.deleteNews(id);
        newsTagsMappingService.deleteMappingByNewsId(id);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "删除成功", null);
    }

    @ResponseBody
    @RequestMapping("/getAllNewsCount")
    public int getAllNewsCount() {
        return newsService.getNewsCount();
    }

    @RequestMapping("/findOneNews")
    public void findOneNews(int id, HttpServletResponse response) throws IOException {
        List<Integer> tagsIds = newsTagsMappingService.getTagsIdByNewsId(id);
        List<String> tagList = new ArrayList<>();
        for(int tagsId : tagsIds) {
            String tagName = tagsService.getTagById(tagsId);
            tagList.add(tagName);
        }
        JSONObject jsonObject = new JSONObject();
        News news = newsService.getNewsById(id);
        jsonObject.put("tagList", tagList);
        jsonObject.put("news", news);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", jsonObject.toJSONString());
    }

    @RequestMapping("/updateNews")
    public void updateNews(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, String> params = FileUploadUtil.uploadFile(request, suffixes, limitSize, 10);

            String title = params.get("title");
            String msgContent = params.get("msgContent");
            String tags = params.get("tags");
            String isTop = params.get("isTop");
            String newsId = params.get("newsId");
            String pic = params.get("pic");//需要更改则有值或为null, "null"是不需要更改
            String picUrl = null;
            if(pic != null && !"null".equals(pic)) {
                JSONArray jsonArray = JSON.parseArray(pic);
                picUrl = jsonArray.getString(0);
            }else if("null".equals(pic)) {//不需要更改
                picUrl = "null";
            }
            String contentUrl = params.get("contentUrl");
            int temp = Integer.parseInt(newsId);
            //判断news
            if (!"null".equals(title) || !"null".equals(msgContent) || !"null".equals(isTop) || !"null".equals(picUrl) || contentUrl != null) {
                System.out.println("qwe");
                newsService.updateNews(title, msgContent, isTop, picUrl, contentUrl, temp, TimeUtil.getNow());
            }
            //判断tags
            if (tags != null && !"null".equals(tags)) {
                System.out.println("asd");
                List<String> tagsIds = JSONObject.parseArray(tags, String.class);//将json字符串转化为list对象
                newsTagsMappingService.deleteMappingByNewsId(temp);//删除当前文章中之前存在的标签
                for (String tag : tagsIds) {
                    int tagsId = Integer.parseInt(tag);
                    NewsTagsMapping newsTagsMapping = new NewsTagsMapping(0, temp, tagsId, TimeUtil.getNow());
                    newsTagsMappingService.addNewsTagsMapping(newsTagsMapping);
                }
            }
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "修改成功", null);
        } catch (FileUploadUtil.SuffixNotMatchException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "后缀不符合要求", null);
        } catch (FileUploadUtil.OutOfLimitSizeException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件大小限制", null);
        } catch (FileUploadUtil.OutOfLimitFileNumException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件数量限制", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
