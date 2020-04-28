package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private int id;
    private int newsId;
    private int userId;
    private String content;
    private int ctime;
    private int thumbs;
    private int isTop;

}
