package com.kenji.dao;

import com.kenji.domain.Tags;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsDao {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert(value = "insert into tags(tag, tags_type, ctime) values(#{tag}, #{tagsType}, #{ctime})")
    int save(Tags tags);

    @Select("select * from tags")
    List<Tags> readAll();

    @Delete("delete from tags where id = #{id}")
    void deleteTagsById(int id);

    @Delete("delete from tags")
    void deleteTags();

    @Select("select tag from tags where id = #{id}")
    String getTagById(int id);

    @Select("select * from tags where tag = #{tag}")
    Tags getTagsByTag(String tag);

    @Select("select id from tags where tag = #{tag}")
    int getIdByTag(String tag);

    @Select("select * from tags limit #{offset}, #{size}")
    List<Tags> getTagsByPage(@Param("offset") int offset, @Param("size") int size);

    @Select("select count(1) from tags")
    int getTagsCount();

    @Update("update tags set id = #{newId} where id = #{lastId}")
    void updateId(@Param("newId") int newId, @Param("lastId") int lastId);

    @Update("update tags set id = id + 100 where id = #{id}")
    void updateIds(int id);

}
