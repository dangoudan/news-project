package com.kenji.service;

import com.kenji.domain.NewsTagsMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsTagsMappingService {

    void addNewsTagsMapping(NewsTagsMapping newsTagsMapping);

    List<Integer> getTagsIdByNewsId(int newsId);

    List<Integer> getNewsIdByTagsId(int tagsId);

    List<Integer> getIdByNewsId(int newsId);

    void updateMapping(int newsId, int tagsId);

    void updateTagsId(int newTags, int lastTags);

    void deleteMappingByNewsId(int newsId);

    void deleteMappingByTagsId(int tagsId);

}
