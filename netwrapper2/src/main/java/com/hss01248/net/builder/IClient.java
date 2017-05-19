package com.hss01248.net.builder;


import com.hss01248.net.cache.ACache;
import com.hss01248.net.cache.CacheStrategy;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.HttpMethod;
import com.hss01248.net.util.TextUtils;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.Tool;

import java.util.concurrent.Executor;


/**
 * Created by Administrator on 2017/1/19 0019.
 */
public abstract class IClient {
    public abstract Executor getExecutor();
    protected abstract <E> ConfigInfo<E> getString(ConfigInfo<E> info);

    protected abstract  <E> ConfigInfo<E> postString(ConfigInfo<E> info);

    protected abstract ConfigInfo download(ConfigInfo info);

    protected abstract ConfigInfo upload(ConfigInfo info);

    public <E> ConfigInfo<E> start(ConfigInfo<E> info){


        //读缓存
        if (getCache(info)){
            return info;
        }

        //todo 判断有没有网络,没有网,或者网连不通,直接就返回
        if(!Tool.isNetworkAvailable()){

            if(info.cacheMode == CacheStrategy.REQUEST_FAILED_READ_CACHE){
                info.shouldReadCache = true;
                start(info);
            }else {
                Tool.dismiss(info.loadingDialog);
                info.listener.onNoNetwork();
            }
            return info;
        }


        // 真正网络请求时,才弹出dialog
        Tool.showDialog(info.loadingDialog);
        switch (info.type){

            case ConfigInfo.TYPE_STRING:
            case ConfigInfo.TYPE_JSON:
            case ConfigInfo.TYPE_JSON_FORMATTED:{
                if(info.method == HttpMethod.GET){
                    return getString(info);
                }else if(info.method == HttpMethod.POST){
                    return postString(info);
                }
            }
            case ConfigInfo.TYPE_DOWNLOAD:
                return download(info);
            case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:
                return upload(info);
            default:
                return info;
        }
    }

    public abstract void cancleRequest(Object tag);
    public abstract void cancleAllRequest();

    //public abstract IClient getInstance(ConfigInfo info);


    /**
     * 缓存获取. 完全的客户端控制,只针对String和Json类型.
     * @param configInfo
     * @param <E>
     * @return
     */
    private <E> boolean getCache(final ConfigInfo<E> configInfo) {
        if(!configInfo.shouldReadCache){
            return false;
        }

        /*if(configInfo.cacheMode== CacheStrategy.DEFAULT || configInfo.cacheMode== CacheStrategy.NO_CACHE){//直接请求网络
            return false;
        }*/


        switch (configInfo.type){
            case ConfigInfo.TYPE_STRING:
            case ConfigInfo.TYPE_JSON:
            case ConfigInfo.TYPE_JSON_FORMATTED:{
                boolean isOnlyCache = false;
                //拿缓存
                if (configInfo.shouldReadCache){

                    getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = ACache.get(HttpUtil.context).getAsString(Tool.getCacheKey(configInfo));
                            MyLog.e("key of get:"+Tool.getCacheKey(configInfo)+"\n"+result);
                            if (TextUtils.isEmpty(result)){
                                if(configInfo.cacheMode==CacheStrategy.IF_NONE_CACHE_REQUEST){
                                    configInfo.shouldReadCache = false;
                                    configInfo.isFromCache = false;
                                    start(configInfo);//没有缓存就去访问网络
                                }else if(configInfo.cacheMode==CacheStrategy.REQUEST_FAILED_READ_CACHE){
                                    configInfo.listener.onError("网络请求失败,也没有读取到缓存");
                                }else if(configInfo.cacheMode==CacheStrategy.FIRST_CACHE_THEN_REQUEST){
                                    configInfo.shouldReadCache = false;
                                    configInfo.isFromCache = false;
                                    start(configInfo);//没有缓存就去访问网络
                                }
                            }else {//如果拿到了缓存数据,解析,然后
                                configInfo.isFromCache = true;//给Tool.parseStringByType里面识别的
                                Tool.parseStringByType(result,configInfo,true);
                                if(configInfo.isFromCacheSuccess){//识别在Tool.parseStringByType中,走的是成功还是失败的回调
                                    if(configInfo.cacheMode==CacheStrategy.IF_NONE_CACHE_REQUEST){//有缓存,就只读缓存,不再操作了

                                    }else if(configInfo.cacheMode==CacheStrategy.REQUEST_FAILED_READ_CACHE){//请求失败,但有缓存,就成功了

                                    }else if(configInfo.cacheMode==CacheStrategy.FIRST_CACHE_THEN_REQUEST){
                                        configInfo.shouldReadCache = false;
                                        configInfo.isFromCache = false;
                                        start(configInfo);//没有缓存就去访问网络
                                    }

                                }else {//读缓存失败
                                    if(configInfo.cacheMode==CacheStrategy.IF_NONE_CACHE_REQUEST){
                                        configInfo.shouldReadCache = false;
                                        configInfo.isFromCache = false;
                                        start(configInfo);//没有缓存就去访问网络
                                    }else if(configInfo.cacheMode==CacheStrategy.REQUEST_FAILED_READ_CACHE){
                                        configInfo.listener.onError("网络请求失败,也没有读取到缓存");
                                    }else if(configInfo.cacheMode==CacheStrategy.FIRST_CACHE_THEN_REQUEST){
                                        configInfo.shouldReadCache = false;
                                        configInfo.isFromCache = false;
                                        start(configInfo);//没有缓存就去访问网络
                                    }
                                }
                            }
                        }
                    });

                    isOnlyCache = true;//不再网络请求,而是进入这个子线程执行
                }else {
                    isOnlyCache = false;
                }
                return isOnlyCache;
            }

            case ConfigInfo.TYPE_DOWNLOAD:
            case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:
            case ConfigInfo.TYPE_UPLOAD_NONE_PROGRESS:
                return false;
            default:return false;
        }
    }





}
