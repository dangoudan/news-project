package com.kenji.service.impl;

import com.kenji.dao.FavoriteDao;
import com.kenji.dao.NewsDao;
import com.kenji.domain.Favorite;
import com.kenji.domain.News;
import com.kenji.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;
    @Autowired
    private NewsDao newsDao;

    @Override
    public void addFavorite(Favorite favorite) {
        favoriteDao.create(favorite);
    }

    @Override
    public List<News> getFavorList(int userId, int offset, int size) {
        List<Favorite> favorites = favoriteDao.getFavorByUserIdPage(userId, offset, size);
        List<News> result = new ArrayList<>();
        for(Favorite favorite : favorites) {
            News news = newsDao.getNewsById(favorite.getNewsId());
            result.add(news);
        }
        return result;
    }

    @Override
    public int getFavorCount(int userId) {
        return favoriteDao.getFavorCount(userId);
    }

    @Override
    public void cancelFavor(int userId, int newsId) {
        favoriteDao.delete(userId, newsId);
    }

    @Override
    public Favorite getFavorOne(int userId, int newsId) {
        return favoriteDao.getFavorOne(userId, newsId);
    }
}
