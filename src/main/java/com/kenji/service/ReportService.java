package com.kenji.service;

import com.kenji.domain.Report;

import java.util.List;

public interface ReportService {

    void addReportToComment(Report report);

    void addReportToReply(Report report);

    int getReportCount();

    List<Report> getReportByPage(int offset, int size);

    void deleteOne(int id);
}
