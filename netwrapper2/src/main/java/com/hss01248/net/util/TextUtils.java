package com.hss01248.net.util;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class TextUtils {
    public static boolean isEmpty(String str){
        return str==null || "".equals(str);
    }
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
