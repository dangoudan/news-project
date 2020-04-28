package com.kenji.controller.user;

import com.kenji.domain.Report;
import com.kenji.service.ReportService;
import com.kenji.util.ResponseUtil;
import com.kenji.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller("userReportController")
@RequestMapping("/user")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping("/addReportToComment")
    public void addReportToComment(@CookieValue("userId") int userId, int commentId, String reportTag, String reportContent, HttpServletResponse response) throws IOException {
        Report report = new Report();
        report.setUserId(userId);
        report.setCommentId(commentId);
        report.setReportTag(reportTag);
        report.setReportContent(reportContent);
        report.setCtime(TimeUtil.getNow());
        try {
            reportService.addReportToComment(report);
        } catch (Exception e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "已举报", null);
            return;
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "举报成功", null);
    }

    @RequestMapping("/addReportToReply")
    public void addReportToReply(@CookieValue("userId") int userId, int replyId, String reportTag, String reportContent, HttpServletResponse response) throws IOException {
        Report report = new Report();
        report.setUserId(userId);
        report.setReplyId(replyId);
        report.setReportTag(reportTag);
        report.setReportContent(reportContent);
        report.setCtime(TimeUtil.getNow());
        try {
            reportService.addReportToReply(report);
        } catch (Exception e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "已举报", null);
            return;
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "举报成功", null);
    }


}
