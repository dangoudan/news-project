package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Thumb {

    private int id;
    private int userId;
    private int newsId;
    private int commentId;
    private int replyId;
    private int ctime;

}
