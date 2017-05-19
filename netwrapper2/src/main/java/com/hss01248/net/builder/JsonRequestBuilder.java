package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.wrapper.MyNetListener;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class JsonRequestBuilder<T> extends StringRequestBuilder{

    public JsonRequestBuilder(){
        this.type = ConfigInfo.TYPE_JSON;
        isResponseJsonArray = false;
    }



    public Class<T> clazz;
    public JsonRequestBuilder<T> setJsonClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }


    //TODO 预期的响应是否为arr
    public boolean isResponseJsonArray ;
    public JsonRequestBuilder<T> setResponseJsonArray() {
        isResponseJsonArray = true;
        return this;
    }

    @Override
    protected ConfigInfo execute() {
        //做一些参数合理性校验
        if(clazz ==null){
            throw new RuntimeException("没有设置clazz参数");
        }


        return new ConfigInfo(this);
    }

    //todo 以下的都是复写基类的方法,强转成子类

    @Override
    public JsonRequestBuilder url(String url) {
        return (JsonRequestBuilder) super.url(url);
    }

    @Override
    public JsonRequestBuilder addHeader(String key, String value) {
        return (JsonRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public JsonRequestBuilder addParam(String key, String value) {
        return (JsonRequestBuilder) super.addParam(key, value);
    }

    @Override
    public JsonRequestBuilder addHeaders(Map headers) {
        return (JsonRequestBuilder) super.addHeaders(headers);
    }

    @Override
    public JsonRequestBuilder addParams(Map params) {
        return (JsonRequestBuilder) super.addParams(params);
    }

    @Override
    public JsonRequestBuilder addParamsInString(String paramsStr) {
        return (JsonRequestBuilder) super.addParamsInString(paramsStr);
    }

    @Override
    public JsonRequestBuilder callback(MyNetListener listener) {
        return (JsonRequestBuilder) super.callback(listener);
    }


    @Override
    public JsonRequestBuilder showLoadingDialog(Activity activity) {
        return (JsonRequestBuilder) super.showLoadingDialog(activity);
    }

    @Override
    public JsonRequestBuilder showLoadingDialog() {
        return (JsonRequestBuilder) super.showLoadingDialog();
    }

    @Override
    public JsonRequestBuilder showLoadingDialog(String loadingMsg) {
        return (JsonRequestBuilder) super.showLoadingDialog(loadingMsg);
    }



    @Override
    public JsonRequestBuilder showLoadingDialog(Activity activity,String loadingMsg) {
        return (JsonRequestBuilder) super.showLoadingDialog( activity,loadingMsg);
    }

    @Override
    public JsonRequestBuilder showLoadingDialog(Dialog loadingDialog) {
        return (JsonRequestBuilder) super.showLoadingDialog(loadingDialog);
    }

    @Override
    public JsonRequestBuilder setCacheMode(int cacheMode) {
        return (JsonRequestBuilder) super.setCacheMode(cacheMode);
    }

    /*@Override
    public JsonRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (JsonRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }*/

    @Override
    public JsonRequestBuilder setRetryCount(int retryCount) {
        return (JsonRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public JsonRequestBuilder setTimeout(int timeoutInMills) {
        return (JsonRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public JsonRequestBuilder setIgnoreCertificateVerify() {
        return (JsonRequestBuilder) super.setIgnoreCertificateVerify();
    }



    //todo 复写string的

    @Override
    public JsonRequestBuilder setParamsAsJson() {
        return (JsonRequestBuilder) super.setParamsAsJson();
    }

    @Override
    public JsonRequestBuilder setExtraTag(Object extraTag) {
        return (JsonRequestBuilder) super.setExtraTag(extraTag);
    }

    @Override
    public JsonRequestBuilder setCacheMaxAge(int cacheMaxAge) {
        return (JsonRequestBuilder) super.setCacheMaxAge(cacheMaxAge);
    }

    @Override
    public JsonRequestBuilder setAppendCommonHeaders(boolean appendCommonHeaders) {
        return (JsonRequestBuilder) super.setAppendCommonHeaders(appendCommonHeaders);
    }

    @Override
    public JsonRequestBuilder setAppendCommonParams(boolean appendCommonParams) {
        return (JsonRequestBuilder) super.setAppendCommonParams(appendCommonParams);
    }
}
