package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.cache.CacheStrategy;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.wrapper.MyNetListener;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class StringRequestBuilder <T> extends BaseNetBuilder{

    public StringRequestBuilder(){
        this.type = ConfigInfo.TYPE_STRING;
    }

    public    int cacheMode = CacheStrategy.NO_CACHE;
    //todo 以下是缓存控制策略
    public boolean shouldReadCache = false;
    public boolean shouldCacheResponse = false;

    public StringRequestBuilder setCacheMaxAge(int cacheMaxAge) {
        this.cacheMaxAge = cacheMaxAge;
        return this;
    }

    public int cacheMaxAge = Integer.MAX_VALUE/2; //单位秒
    public boolean isFromCache = false;//内部控制,不让外部设置
    /**
     * 只支持String和json类型的请求,不支持文件下载的缓存.
     * @param shouldReadCache 是否先去读缓存
     * @param shouldCacheResponse 是否缓存response  内部已做判断,只会缓存状态是成功的那些请求
     * @param cacheTimeInSeconds 缓存的时间,单位是秒
     * @return
     *

     */

    //todo 请求以什么形式,key=value&key=value还是json形式
    public boolean paramsAsJson = false;
    public StringRequestBuilder<T> setParamsAsJson() {
        this.paramsAsJson = true;

        return this;
    }

    @Override
    protected ConfigInfo execute() {
        //做一些参数合理性校验

        return new ConfigInfo(this);
    }

    public StringRequestBuilder setCacheMode(int cacheMode) {
        this.cacheMode = cacheMode;
        return this;
    }

//todo 以下的都是复写基类的方法,强转成子类

    @Override
    public StringRequestBuilder url(String url) {
        return (StringRequestBuilder) super.url(url);
    }

    @Override
    public StringRequestBuilder addHeader(String key, String value) {
        return (StringRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public StringRequestBuilder addParam(String key, String value) {
        return (StringRequestBuilder) super.addParam(key, value);
    }

    @Override
    public StringRequestBuilder addHeaders(Map headers) {
        return (StringRequestBuilder) super.addHeaders(headers);
    }

    @Override
    public StringRequestBuilder addParams(Map params) {
        return (StringRequestBuilder) super.addParams(params);
    }

    @Override
    public StringRequestBuilder addParamsInString(String paramsStr) {
        return (StringRequestBuilder) super.addParamsInString(paramsStr);
    }

    @Override
    public StringRequestBuilder callback(MyNetListener listener) {
        return (StringRequestBuilder) super.callback(listener);
    }


    @Override
    public StringRequestBuilder showLoadingDialog() {
        return (StringRequestBuilder) super.showLoadingDialog();
    }

    @Override
    public StringRequestBuilder showLoadingDialog(String loadingMsg) {
        return (StringRequestBuilder) super.showLoadingDialog(loadingMsg);
    }

    @Override
    public StringRequestBuilder showLoadingDialog(Activity activity) {
        return (StringRequestBuilder) super.showLoadingDialog(activity);
    }

    @Override
    public StringRequestBuilder showLoadingDialog(Activity activity,String loadingMsg) {
        return (StringRequestBuilder) super.showLoadingDialog( activity,loadingMsg);
    }

    @Override
    public StringRequestBuilder showLoadingDialog(Dialog loadingDialog) {
        return (StringRequestBuilder) super.showLoadingDialog(loadingDialog);
    }

   /* @Override
    public StringRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (StringRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }*/

    @Override
    public StringRequestBuilder setRetryCount(int retryCount) {
        return (StringRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public StringRequestBuilder setTimeout(int timeoutInMills) {
        return (StringRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public StringRequestBuilder setIgnoreCertificateVerify() {
        return (StringRequestBuilder) super.setIgnoreCertificateVerify();
    }



    @Override
    public StringRequestBuilder setExtraTag(Object extraTag) {
        return (StringRequestBuilder) super.setExtraTag(extraTag);
    }

    @Override
    public StringRequestBuilder setAppendCommonHeaders(boolean appendCommonHeaders) {
        return (StringRequestBuilder) super.setAppendCommonHeaders(appendCommonHeaders);
    }

    @Override
    public StringRequestBuilder setAppendCommonParams(boolean appendCommonParams) {
        return (StringRequestBuilder) super.setAppendCommonParams(appendCommonParams);
    }
}
