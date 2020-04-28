package com.kenji.dao;

import com.kenji.domain.Report;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDao {

    @Insert("insert into report(user_id, comment_id, report_tag, report_content, ctime) values(#{userId}, #{commentId}, #{reportTag}, #{reportContent}, #{ctime})")
    void addReportToComment(Report report);

    @Insert("insert into report(user_id, reply_id, report_tag, report_content, ctime) values(#{userId}, #{replyId}, #{reportTag}, #{reportContent}, #{ctime})")
    void addReportToReply(Report report);

    @Select("select count(1) from report")
    int getReportCount();

    @Select("select * from report order by ctime desc, id asc limit #{offset}, #{size}")
    List<Report> getReportByPage(@Param("offset") int offset, @Param("size") int size);

    @Delete("delete from report where id = #{id}")
    void deleteOne(int id);
}
