package com.kenji.service;

import com.kenji.domain.Tags;

import java.util.List;

public interface TagsService {

    void save(Tags tags) throws Exception;

    List<Tags> getAllTags();

    void deleteTagsById(int id);

    void deleteTags();

    String getTagById(int id);

    Tags getTagsByTag(String tag);

    int getIdByTag(String tag);

    void updateId(int newId, int lastId);

    void updateIds(int id);

    List<Tags> getTagsByPage(int offset, int size);

    int getTagsCount();
}
