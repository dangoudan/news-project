package com.kenji.service.impl;

import com.kenji.dao.NewsTagsMappingDao;
import com.kenji.domain.NewsTagsMapping;
import com.kenji.service.NewsTagsMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("newsTagsMappingService")
public class NewsTagsMappingServiceImpl implements NewsTagsMappingService {

    @Autowired
    private NewsTagsMappingDao dao;

    @Override
    public void addNewsTagsMapping(NewsTagsMapping newsTagsMapping) {
        dao.create(newsTagsMapping);
    }

    @Override
    public List<Integer> getTagsIdByNewsId(int newsId) {
        return dao.getTagsIdByNewsId(newsId);
    }

    @Override
    public List<Integer> getNewsIdByTagsId(int tagsId) {
        return dao.getNewsIdByTagsId(tagsId);
    }

    @Override
    public List<Integer> getIdByNewsId(int newsId) {
        return dao.getIdByNewsId(newsId);
    }

    @Override
    public void updateMapping(int tagsId, int id) {
        dao.updateMapping(tagsId, id);
    }

    @Override
    public void updateTagsId(int newTags, int lastTags) {
        dao.updateTagsId(newTags, lastTags);
    }

    @Override
    public void deleteMappingByNewsId(int newsId) {
        dao.deleteMappingByNewsId(newsId);
    }

    @Override
    public void deleteMappingByTagsId(int tagsId) {
        dao.deleteMappingByTagsId(tagsId);
    }
}
