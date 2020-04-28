package com.kenji.service.impl;

import com.kenji.dao.TagsDao;
import com.kenji.domain.Tags;
import com.kenji.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tagsService")
public class TagsServiceImpl implements TagsService {

    @Autowired
    private TagsDao tagsDao;

    @Override
    public void save(Tags tags) throws Exception {
        tagsDao.save(tags);
    }

    @Override
    public List<Tags> getAllTags() {
        return tagsDao.readAll();
    }

    @Override
    public void deleteTagsById(int id) {
        tagsDao.deleteTagsById(id);
    }

    @Override
    public void deleteTags() {
        tagsDao.deleteTags();
    }

    @Override
    public String getTagById(int id) {
        return tagsDao.getTagById(id);
    }

    @Override
    public Tags getTagsByTag(String tag) {
        return tagsDao.getTagsByTag(tag);
    }

    @Override
    public int getIdByTag(String tag) {
        return tagsDao.getIdByTag(tag);
    }

    @Override
    public void updateId(int newId, int lastId) {
        tagsDao.updateId(newId, lastId);
    }

    public void updateIds(int id) {
        tagsDao.updateIds(id);
    }

    @Override
    public List<Tags> getTagsByPage(int offset, int size) {
        return tagsDao.getTagsByPage(offset, size);
    }

    @Override
    public int getTagsCount() {
        return tagsDao.getTagsCount();
    }
}
