package com.kenji.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println(TimeUtil.getNow());
        long time = TimeUtil.getNow();
        System.out.println(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date(time * 1000)));
        System.out.println(sdf.format(new Date(System.currentTimeMillis())));
    }

}
