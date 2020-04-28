package com.kenji.service;

import com.kenji.domain.Favorite;
import com.kenji.domain.News;

import java.util.List;

public interface FavoriteService {

    void addFavorite(Favorite favorite);

    List<News> getFavorList(int userId, int offset, int size);

    int getFavorCount(int userId);

    void cancelFavor(int userId, int newsId);

    Favorite getFavorOne(int userId, int newsId);
}
