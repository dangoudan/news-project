package com.kenji.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static int getNow() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String getDate(long time) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 ");
        return sdf.format(new Date(time * 1000)) + weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];//秒级别
    }

    public static void main(String[] args) {
        System.out.println(getDate(getNow()));
    }

}
