package com.kenji.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.kenji.service.ViewRecordService;
import com.kenji.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class ViewRecordController {

    @Autowired
    private ViewRecordService service;

    @RequestMapping("/getRecords")
    public void getViewRecords(@CookieValue("userId") int userId, HttpServletResponse response) throws Exception {
        JSONArray result = service.getViewRecordList(userId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"查询成功", result.toJSONString());
    }

    @ResponseBody
    @RequestMapping("/getRecordCount")
    public int getRecordCount(@CookieValue("userId") int userId) throws Exception {
        return service.getViewRecordList(userId).size();
    }

    @RequestMapping("/delRecord")
    public void delRecord(@CookieValue("userId") int userId, int newsId, HttpServletResponse response) throws Exception {
        service.delRecord(userId, newsId);
        ResponseUtil.writeJSON(response,ResponseUtil.ResponseEnum.OK,"删除成功", null);
    }
}
