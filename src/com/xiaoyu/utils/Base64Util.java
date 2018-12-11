package com.xiaoyu.utils;
/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class Base64Util {
    //base64字符串转byte[]
    public static byte[] base64String2Byte(String base64Str) {
        return Base64.decode(base64Str, Base64.NO_WRAP);
    }

    //byte[]转base64
    public static String byte2base64String(byte[] b) {
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

}
