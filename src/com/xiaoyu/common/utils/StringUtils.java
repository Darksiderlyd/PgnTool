package com.xiaoyu.common.utils;

/**
 * @Author: lyd
 * @Date: 2018/12/14 下午4:47
 * @Version 1.0.0
 */
public class StringUtils {

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static String getPathName(String path) {
        int index1 = path.lastIndexOf("/");
        int index2 = path.lastIndexOf(".");
        return path.substring(index1 + 1, index2);
    }

}
