package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private int id;
    private String title;
    private String author;
    private String content;
    private String picUrl;
    private String contentUrl;
    private int views;
    private int thumbs;
    private int isTop;
    private int ctime;

}
