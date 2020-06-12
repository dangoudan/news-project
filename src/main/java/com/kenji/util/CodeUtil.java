package com.kenji.util;

import java.io.IOException;
import java.util.Properties;

public class CodeUtil {

    public static String getCode() {
        try {
            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("code.properties"));

            return properties.getProperty("code");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
