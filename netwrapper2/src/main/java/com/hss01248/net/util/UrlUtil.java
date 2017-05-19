package com.hss01248.net.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class UrlUtil {

    public static String guessFileName(String url) {
        String str = url;
        try {
             str = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = url;
        }

        if(str.contains("?")){
            str = str.substring(0,str.lastIndexOf("?"));
        }
        int index = str.lastIndexOf("/");
        return str.substring(index+1);

    }
}
