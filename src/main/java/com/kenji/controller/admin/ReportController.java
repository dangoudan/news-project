package com.kenji.controller.admin;

import com.kenji.domain.Report;
import com.kenji.service.CommentService;
import com.kenji.service.ReplyService;
import com.kenji.service.ReportService;
import com.kenji.service.UserService;
import com.kenji.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("adminReportController")
@RequestMapping("/admin")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/getReportCount")
    public int getReportCount() {
        return reportService.getReportCount();
    }

    @ResponseBody
    @RequestMapping("/getReportByPage")
    public List<Map<String, Object>> getReportByPage(int offset, int size) {
        List<Report> reportList = reportService.getReportByPage(offset, size);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(Report report : reportList) {
            Map<String, Object> map = new HashMap<>();
            if(report.getCommentId() != 0) {
                String content = commentService.getCommentContentById(report.getCommentId());
                map.put("id", report.getCommentId());
                map.put("commentOrReply", 0); // comment 0表示， reply 1表示
                map.put("content", content);
            }else {
                String content = replyService.getReplyContentById(report.getReplyId());
                map.put("id", report.getReplyId());
                map.put("commentOrReply", 1); // comment 0表示， reply 1表示
                map.put("content", content);
            }
            String name = userService.getUserById(report.getUserId()).getAccount();
            map.put("reportId", report.getId());
            map.put("name", name);
            map.put("reportTag", report.getReportTag());
            map.put("ctime", report.getCtime());
            if(!"".equals(report.getReportContent()) && report.getReportContent() != null) {
                map.put("reportContent", report.getReportContent());
            }
            mapList.add(map);
        }
        return mapList;
    }

    @RequestMapping("/delComment")
    public void delComment(int commentOrReply, int id, HttpServletResponse response) throws IOException {
        if(commentOrReply == 0) {
            commentService.delComment(id);
            replyService.delReplyByCommentId(id);
        }else if(commentOrReply == 1) {
            replyService.delReply(id);
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "删除成功", null);
    }

    @RequestMapping("/delReport")
    public void delReport(int reportId, HttpServletResponse response) throws IOException {
        reportService.deleteOne(reportId);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "删除成功", null);
    }

}
