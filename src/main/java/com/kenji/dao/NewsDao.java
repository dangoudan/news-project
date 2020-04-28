package com.kenji.dao;

import com.kenji.domain.News;
import com.kenji.dynamic.NewsDynamic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDao {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("insert into news(title, author, content, ctime, views, thumbs, is_top, pic_url, content_url) values(#{title}, #{author}, #{content}, #{ctime}, #{views}, #{thumbs}, #{isTop}, #{picUrl}, #{contentUrl})")
    int create(News news);

    @Select("select * from news")
    List<News> getAllNews();

    @Delete("delete from news where id = #{id}")
    void deleteNews(int id);

    @Select("select * from news where id = #{id}")
    News getNewsById(int id);

    @UpdateProvider(NewsDynamic.class)
    void updateNews(@Param("title") String title, @Param("content") String content, @Param("isTop") int isTop,
                    @Param("picUrl") String picUrl, @Param("contentUrl") String contentUrl, @Param("id") int id, @Param("ctime") int ctime);

    @Update("update news set views = views + 1 where id = #{id}")
    void addNewsViews(int id);

    @Update("update news set thumbs = thumbs + 1 where id = #{id}")
    void addNewsThumbs(int id);

    @Update("update news set thumbs = thumbs - 1 where id = #{id}")
    void delNewsThumbs(int id);

    @Select("select count(1) from news")
    int getNewsCount();

    @Select("select * from news where title like concat(concat(\"%\", #{keyword}), \"%\")")
    List<News> getNewsByBlurName(String keyword);

    @Select("select * from news where title like concat(concat(\"%\", #{keyword}), \"%\") limit #{offset}, #{size}")
    List<News> getNewsByBlurNamePage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    @Select("select * from news where title like concat(concat(\"%\", #{keyword}), \"%\") order by views desc, thumbs desc limit #{offset}, #{size}")
    List<News> getNewsByBlurNameHotPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    @Select("select * from news where title like concat(concat(\"%\", #{keyword}), \"%\") order by ctime desc limit #{offset}, #{size}")
    List<News> getNewsByBlurNameLatestPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    @Select("select count(1) from news where title like concat(concat(\"%\", #{keyword}), \"%\")")
    int getNewsByBlurNameCount(String keyword);

    @Select("select n.* from tags t inner join news_tags_mapping ntm on t.id = ntm.tags_id inner join news n on ntm.news_id = n.id " +
            "where t.id = #{tagsId} and n.is_top = 1 order by id desc limit #{offset}, #{size}")
    List<News> getNewsByTagsIsTop(@Param("tagsId") int tagsId, @Param("offset") int offset, @Param("size") int size);

    @Select("select n.* from tags t inner join news_tags_mapping ntm on t.id = ntm.tags_id inner join news n on ntm.news_id = n.id " +
            "where t.id = #{tagsId} and n.is_top = #{isTop} order by id desc limit #{offset}, #{size}")
    List<News> getNewsByTagsIsTopOne(@Param("tagsId") int tagsId, @Param("isTop") int isTop, @Param("offset") int offset, @Param("size") int size);

    @Select("select n.* from tags t inner join news_tags_mapping ntm on t.id = ntm.tags_id inner join news n on ntm.news_id = n.id " +
            "where t.id = #{tagsId} order by ctime desc, id asc limit #{offset}, #{size}")
    List<News> getNewsByTagsCtime(@Param("tagsId") int tagsId, @Param("offset") int offset, @Param("size") int size);

    @Select({
            "<script>",
            "select distinct n.* from tags t inner join news_tags_mapping ntm on t.id = ntm.tags_id inner join news n on ntm.news_id = n.id ",
            "where t.id in ",
            "<foreach collection='tagsIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "order by views desc, id asc limit #{offset}, #{size}",
            "</script>"
    })
    List<News> getNewsByRelation(@Param("tagsIds") List<Integer> tagsIds, @Param("offset") int offset, @Param("size") int size);



    @Select("select * from news order by ctime desc, id asc limit #{offset}, #{size}")
    List<News> getNewsByCtimeDesc(@Param("offset") int offset, @Param("size") int size);

    @Select("select * from news order by ctime asc, id asc limit #{offset}, #{size}")
    List<News> getNewsByCtimeAsc(@Param("offset") int offset, @Param("size") int size);

    @Select({
            "<script>",
            "select * from news where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "order by thumbs desc, views desc limit #{offset}, #{size}",
            "</script>"
    })
    List<News> getNewsByTagsThumbs1(@Param("ids") List<Integer> ids, @Param("offset") int offset, @Param("size") int size);

    @Select("select n.* from tags t inner join news_tags_mapping ntm on t.id = ntm.tags_id inner join news n on ntm.news_id = n.id " +
            "where t.id = #{tagsId} order by thumbs desc, views desc limit #{offset}, #{size}")
    List<News> getNewsByTagsThumbs2(@Param("tagsId") int tagsId, @Param("offset") int offset, @Param("size") int size);
}
