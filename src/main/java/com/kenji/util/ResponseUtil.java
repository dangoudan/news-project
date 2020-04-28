package com.kenji.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    public enum ResponseEnum{//内部枚举类表示状态
        OK("OK"),FAIL("FAIL");
        private String value;
        ResponseEnum(String value){
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    public static void writeJSON(HttpServletResponse response, ResponseEnum responseEnum, String msg, String data) throws IOException {
        //response工具 responseEnum表示状态 msg对应状态的解释 data需要给前端返回的数据
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        JSONObject result = new JSONObject();
        result.put("status",responseEnum.getValue());
        result.put("msg",msg);
        result.put("data",data);
        response.getWriter().write(result.toJSONString());
    }

}
