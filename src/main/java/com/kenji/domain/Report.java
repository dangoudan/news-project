package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private int id;
    private int userId;
    private int commentId;
    private int replyId;
    private String reportTag;
    private String reportContent;
    private int ctime;

}
