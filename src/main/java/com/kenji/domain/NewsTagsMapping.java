package com.kenji.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsTagsMapping {

    private int id;
    private int newsId;
    private int tagsId;
    private int ctime;
}
