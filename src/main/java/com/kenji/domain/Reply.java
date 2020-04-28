package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

    private int id;
    private String userName;
    private String repliedName;
    private int commentId;
    private String content;
    private int thumbs;
    private int ctime;

}
