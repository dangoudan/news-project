package com.kenji.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kenji.dao.ViewRecordDao;
import com.kenji.domain.News;
import com.kenji.domain.ViewRecord;
import com.kenji.service.NewsService;
import com.kenji.service.ViewRecordService;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewRecordServiceImpl implements ViewRecordService {

    @Autowired
    private ViewRecordDao dao;
    @Autowired
    private NewsService newsService;

    @Override
    public void addViewRecord(ViewRecord viewRecord) {
        dao.create(viewRecord);
    }

    @Override
    public JSONArray getViewRecordList(int userId) {
        //加上只查一周之内的
        int now = TimeUtil.getNow();
        int begin = now - 7 * 24 * 3600;
        List<ViewRecord> viewRecordList = dao.readOneWeek(userId, begin, now);
        JSONArray result = new JSONArray();//返回jsonArray将news转化为字符串 否则重复记录会被json吞掉
        Map<Integer, News> newsMap = new HashMap<>();//缓存 为了不让相同的news的浏览记录一直查询数据库
        for(ViewRecord viewRecord : viewRecordList){
            News news = newsMap.get(viewRecord.getNewsId());
            if(news == null){
                news = newsService.getNewsById(viewRecord.getNewsId());
                newsMap.put(viewRecord.getNewsId(), news);
                result.add(JSON.toJSONString(news));
            }
        }
        return result;
    }

    @Override
    public void delRecord(int userId, int newsId) {
        dao.delRecord(userId, newsId);
    }
}
