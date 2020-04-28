package com.kenji.dao;

import com.kenji.domain.NewsTagsMapping;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsTagsMappingDao {

    @Insert("insert into news_tags_mapping(news_id, tags_id, ctime) values(#{newsId}, #{tagsId}, #{ctime})")
    void create(NewsTagsMapping newsTagsMapping);

    @Select("select tags_id from news_tags_mapping where news_id = #{newsId}")
    List<Integer> getTagsIdByNewsId(int newsId);

    @Select("select id from news_tags_mapping where news_id = #{newsId}")
    List<Integer> getIdByNewsId(int newsId);

    @Update("update news_tags_mapping set tags_id = #{tagsId} where id = #{id}")
    void updateMapping(@Param("tagsId") int tagsId, @Param("id") int id);

    @Update("update news_tags_mapping set tags_id = #{newTags} where tags_id = #{lastTags}")
    void updateTagsId(@Param("newTags") int newTags, @Param("lastTags") int lastTags);

    @Delete("delete from news_tags_mapping where news_id = #{newsId}")
    void deleteMappingByNewsId(int newsId);

    @Delete("delete from news_tags_mapping where tags_id = #{tagsId}")
    void deleteMappingByTagsId(int tagsId);

    @Select("select news_id from news_tags_mapping where tags_id = #{tagsId}")
    List<Integer> getNewsIdByTagsId(int tagsId);

}
