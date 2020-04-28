package com.kenji.service;

import com.alibaba.fastjson.JSONArray;
import com.kenji.domain.ViewRecord;

public interface ViewRecordService {

    void addViewRecord(ViewRecord viewRecord);

    JSONArray getViewRecordList(int userId);

    void delRecord(int userId, int newsId);
}
