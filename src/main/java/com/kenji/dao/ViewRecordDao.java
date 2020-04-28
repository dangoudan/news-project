package com.kenji.dao;

import com.kenji.domain.ViewRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewRecordDao {

    @Insert("insert into view_record(user_id, news_id, ctime) values(#{userId}, #{newsId}, #{ctime})")
    void create(ViewRecord viewRecord);

    @Select("select * from view_record where user_id = #{userId}")
    List<ViewRecord> readAll(int userId);

    @Select("select * from view_record where user_id = #{userId} and ctime between #{begin} and #{now}")
    List<ViewRecord> readOneWeek(@Param("userId") int userId, @Param("begin") int begin, @Param("now") int now);

    @Delete("delete from view_record where user_id = #{userId} and news_id = #{newsId}")
    void delRecord(@Param("userId") int userId, @Param("newsId") int newsId);
}
