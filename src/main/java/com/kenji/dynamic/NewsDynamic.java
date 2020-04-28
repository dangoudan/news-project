package com.kenji.dynamic;

import org.apache.ibatis.annotations.Param;

public class NewsDynamic {

    public String provideSql(@Param("title") String title,
                             @Param("content") String content,
                             @Param("isTop") int isTop,
                             @Param("picUrl") String picUrl,
                             @Param("contentUrl") String contentUrl,
                             @Param("id") int id,
                             @Param("ctime") int ctime) {
        StringBuilder sb = new StringBuilder();
        sb.append("update news set ");
        if(title != null && !"null".equals(title)){
            sb.append("title = #{title},");
        }
        if(content != null && !"null".equals(content)){
            sb.append("content = #{content},");
        }
        if(isTop != -1) {
            sb.append("is_top = #{isTop},");
        }
        if(!"null".equals(picUrl)) {
            sb.append("pic_url = #{picUrl},");
        }
        if(contentUrl != null && !"null".equals(contentUrl)) {
            sb.append("content_url = #{contentUrl},");
        }
        sb.append("ctime = #{ctime} ");
        sb.append("where id = #{id}");
        return sb.toString();
    }

}
