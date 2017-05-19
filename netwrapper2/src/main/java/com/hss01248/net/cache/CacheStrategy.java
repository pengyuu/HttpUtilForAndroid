package com.hss01248.net.cache;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class CacheStrategy {

    //缓存策略,分类参考:https://github.com/jeasonlzy/okhttp-OkGo
    public static final int NO_CACHE = 1;//不使用缓存,该模式下,cacheKey,cacheMaxAge 参数均无效
    public static final int DEFAULT = 2;//完全按照HTTP协议的默认缓存规则，例如有304响应头时缓存。
    public static final int REQUEST_FAILED_READ_CACHE = 3;//先请求网络，如果请求网络失败，则读取缓存，如果读取缓存失败，本次请求失败。会导致强制缓存响应.
    public static final int IF_NONE_CACHE_REQUEST = 4;//如果缓存不存在才请求网络，否则使用缓存。会导致强制缓存响应
    public static final int FIRST_CACHE_THEN_REQUEST = 5;//先使用缓存，不管是否存在，仍然请求网络。会导致强制缓存响应
    @IntDef({NO_CACHE, DEFAULT,REQUEST_FAILED_READ_CACHE,IF_NONE_CACHE_REQUEST,FIRST_CACHE_THEN_REQUEST})
     @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }


}
