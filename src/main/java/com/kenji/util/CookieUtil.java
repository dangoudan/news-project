package com.kenji.util;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    public static String getCookieByName(Cookie[] cookies, String name){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Map<String,String> analysisCookie(Cookie[] cookies){
        Map<String,String> cookieMap = new HashMap<>();
        for(Cookie cookie : cookies){
            cookieMap.put(cookie.getName(),cookie.getValue());
        }
        return cookieMap;
    }

}
