package com.kenji.service.impl;

import com.kenji.dao.NewsDao;
import com.kenji.domain.News;
import com.kenji.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("newsService")
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao dao;

    @Override
    public int create(News news) {
        dao.create(news);
        return news.getId();
    }

    @Override
    public List<News> getAllNews() {
        return dao.getAllNews();
    }

    @Override
    public void deleteNews(int id) {
        dao.deleteNews(id);
    }

    @Override
    public News getNewsById(int id) {
        return dao.getNewsById(id);
    }

    @Override
    public void updateNews(String title, String content, String isTop, String picUrl, String contentUrl, int id, int ctime) {
        int temp = -1;
        if(isTop != null && !"null".equals(isTop)){
            temp = Integer.parseInt(isTop);
        }
        dao.updateNews(title, content, temp, picUrl, contentUrl, id, ctime);
    }

    @Override
    public void addNewsViews(int id) {
        dao.addNewsViews(id);
    }

    @Override
    public void addNewsThumbs(int id) {
        dao.addNewsThumbs(id);
    }

    @Override
    public void delNewsThumbs(int id) {
        dao.delNewsThumbs(id);
    }

    @Override
    public int getNewsCount() {
        return dao.getNewsCount();
    }

    @Override
    public List<News> getNewsByBlurName(String keyword) {
        return dao.getNewsByBlurName(keyword);
    }

    @Override
    public int getNewsByBlurNameCount(String keyword) {
        return dao.getNewsByBlurNameCount(keyword);
    }

    @Override
    public List<News> getNewsByBlurNamePage(String keyword, int offset, int size) {
        return dao.getNewsByBlurNamePage(keyword, offset, size);
    }

    @Override
    public List<News> getNewsByBlurNameHotPage(String keyword, int offset, int size) {
        return dao.getNewsByBlurNameHotPage(keyword, offset, size);
    }

    @Override
    public List<News> getNewsByBlurNameLatestPage(String keyword, int offset, int size) {
        return dao.getNewsByBlurNameLatestPage(keyword, offset, size);
    }

    @Override
    public List<News> getNewsByTagsIsTop(int tagsId, int offset, int size) {
        return dao.getNewsByTagsIsTop(tagsId, offset, size);
    }

    @Override
    public List<News> getNewsByTagsIsTopOne(int tagsId, int isTop, int offset, int size) {
        return dao.getNewsByTagsIsTopOne(tagsId, isTop, offset, size);
    }

    @Override
    public List<News> getNewsByTagsCtime(int tagsId, int offset, int size) {
        return dao.getNewsByTagsCtime(tagsId, offset, size);
    }

    @Override
    public List<News> getNewsByRelation(List<Integer> tagsIds, int offset, int size) {
        return dao.getNewsByRelation(tagsIds, offset, size);
    }

    @Override
    public List<News> getNewsByCtimeDesc(int offset, int size) {
        return dao.getNewsByCtimeDesc(offset, size);
    }

    @Override
    public List<News> getNewsByCtimeAsc(int offset, int size) {
        return dao.getNewsByCtimeAsc(offset, size);
    }

    @Override
    public List<News> getNewsByTagsThumbs(List<Integer> ids, int offset, int size) {
        return dao.getNewsByTagsThumbs1(ids, offset, size);
    }

    @Override
    public List<News> getNewsByTagsThumbs(int tagsId, int offset, int size) {
        return dao.getNewsByTagsThumbs2(tagsId, offset, size);
    }

}
