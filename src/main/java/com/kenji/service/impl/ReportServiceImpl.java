package com.kenji.service.impl;

import com.kenji.dao.ReportDao;
import com.kenji.domain.Report;
import com.kenji.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;


    @Override
    public void addReportToComment(Report report) {
        reportDao.addReportToComment(report);
    }

    @Override
    public void addReportToReply(Report report) {
        reportDao.addReportToReply(report);
    }

    @Override
    public int getReportCount() {
        return reportDao.getReportCount();
    }

    @Override
    public List<Report> getReportByPage(int offset, int size) {
        return reportDao.getReportByPage(offset, size);
    }

    @Override
    public void deleteOne(int id) {
        reportDao.deleteOne(id);
    }
}
