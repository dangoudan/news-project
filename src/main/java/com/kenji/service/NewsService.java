package com.kenji.service;

import com.kenji.domain.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsService {

    int create(News news);

    List<News> getAllNews();

    void deleteNews(int id);

    News getNewsById(int id);

    void updateNews(String title, String content, String isTop, String picUrl, String contentUrl, int id, int ctime);

    void addNewsViews(int id);

    void addNewsThumbs(int id);

    void delNewsThumbs(int id);

    int getNewsCount();

    List<News> getNewsByBlurName(String keyword);

    int getNewsByBlurNameCount(String keyword);

    List<News> getNewsByBlurNamePage(String keyword, int offset, int size);

    List<News> getNewsByBlurNameHotPage(String keyword, int offset, int size);

    List<News> getNewsByBlurNameLatestPage(String keyword, int offset, int size);

    List<News> getNewsByTagsIsTop(int tagsId, int offset, int size);

    List<News> getNewsByTagsIsTopOne(int tagsId, int isTop, int offset, int size);

    List<News> getNewsByTagsCtime(int tagsId, int offset, int size);
    
    List<News> getNewsByRelation(List<Integer> tagsIds, int offset, int size);

    List<News> getNewsByCtimeDesc(int offset, int size);

    List<News> getNewsByCtimeAsc(int offset, int size);

    List<News> getNewsByTagsThumbs(List<Integer> ids, int offset, int size);

    List<News> getNewsByTagsThumbs(int tagsId, int offset, int size);
}
