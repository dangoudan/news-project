package com.kenji.util;


import java.util.*;

public class InsertSortUtil {

    public static List<Object> insertSort(List<Map<String, Object>> mapList){
        Object[] maps = mapList.toArray();
        for(int i = 1; i < maps.length; i++){
            for(int j = i - 1; j >= 0; j--){
                if((int)((Map<String, Object>)maps[j + 1]).get("ctime") > (int)((Map<String, Object>)maps[j]).get("ctime")){//若当前元素大于此元素之前的这个元素，就证明不会比之前的任何一个元素小
                    Object temp = maps[j + 1];
                    maps[j + 1] = maps[j];
                    maps[j] = temp;
                }
            }
        }
        return Arrays.asList(maps);
    }

    public static List<Object> insertSort1(List<Map<String, Integer>> mapList){
        Object[] maps = mapList.toArray();
        for(int i = 1; i < maps.length; i++){
            for(int j = i - 1; j >= 0; j--){
                if(((Map<String, Integer>)maps[j + 1]).get("commentCount") > ((Map<String, Integer>)maps[j]).get("commentCount")){//若当前元素大于此元素之前的这个元素，就证明不会比之前的任何一个元素小
                    Object temp = maps[j + 1];
                    maps[j + 1] = maps[j];
                    maps[j] = temp;
                }
            }
        }
        return Arrays.asList(maps);
    }

}
