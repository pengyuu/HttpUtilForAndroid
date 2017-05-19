package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.wrapper.MyNetListener;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class StandardJsonRequestBuilder <T> extends JsonRequestBuilder{


    public StandardJsonRequestBuilder(){
        this.type = ConfigInfo.TYPE_JSON_FORMATTED;
        this.key_code = GlobalConfig.get().getStandardJsonKeyCode();
        this.key_data = GlobalConfig.get().getStandardJsonKeyData();
        this.key_msg = GlobalConfig.get().getStandardJsonKeyMsg();
        this.code_success = GlobalConfig.get().getCodeSuccess();
        this.code_unlogin = GlobalConfig.get().getCodeUnlogin();
        this.code_unFound = GlobalConfig.get().getCodeUnfound();
        isCustomCodeSet = false;
        isTreatEmptyDataAsSuccess  = true;//todo quanju
    }







    //todo 设置标准格式json本次响应的不同字段
    public String key_data = "";
    public String key_code = "";
    public String key_msg = "";
    // public String key_isSuccess = "";

    public int code_success;
    public int code_unlogin;
    public int code_unFound;

    public boolean isCustomCodeSet ;



    /**
     * 单个请求的
     * @param keyData
     * @param keyCode
     * @param keyMsg

     * @return
     */
    public StandardJsonRequestBuilder<T> setStandardJsonKey(String keyData,String keyCode,String keyMsg){
        this.key_data = keyData;
        this.key_code = keyCode;
        this.key_msg = keyMsg;
        return this;
    }

    /**
     * 单个请求的code的key可能会不一样
     * @param keyCode
     * @return
     */
    public StandardJsonRequestBuilder<T> setStandardJsonKeyCode(String keyCode){
        this.key_code = keyCode;
        return this;

    }

    /**
     * 单个请求用到的code的具体值
     * @param code_success
     * @param code_unlogin
     * @param code_unFound
     * @return
     */
    public StandardJsonRequestBuilder<T> setCustomCodeValue(int code_success,int code_unlogin,int code_unFound){
        this.code_success = code_success;
        this.code_unlogin = code_unlogin;
        this.code_unFound = code_unFound;
        isCustomCodeSet = true;
        return this;
    }


    //todo 状态为成功时,data对应的字段是否为空
    public boolean isTreatEmptyDataAsSuccess ;

    /**
     * 设置 当data对应字段为空时,回调是成功还是失败
     * 默认是成功.
     * 只有当data预期为jsonobject时,这个设置才生效
     */
    public StandardJsonRequestBuilder<T> setTreatEmptyDataStrAsSuccess(boolean treatEmptyDataAsSuccess){
        this.isTreatEmptyDataAsSuccess = treatEmptyDataAsSuccess;
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
    public StandardJsonRequestBuilder url(String url) {
        return (StandardJsonRequestBuilder) super.url(url);
    }

    @Override
    public StandardJsonRequestBuilder addHeader(String key, String value) {
        return (StandardJsonRequestBuilder) super.addHeader(key, value);
    }

    @Override
    public StandardJsonRequestBuilder addParam(String key, String value) {
        return (StandardJsonRequestBuilder) super.addParam(key, value);
    }

    @Override
    public StandardJsonRequestBuilder addHeaders(Map headers) {
        return (StandardJsonRequestBuilder) super.addHeaders(headers);
    }

    @Override
    public StandardJsonRequestBuilder addParams(Map params) {
        return (StandardJsonRequestBuilder) super.addParams(params);
    }

    @Override
    public StandardJsonRequestBuilder addParamsInString(String paramsStr) {
        return (StandardJsonRequestBuilder) super.addParamsInString(paramsStr);
    }

    @Override
    public StandardJsonRequestBuilder callback(MyNetListener listener) {
        return (StandardJsonRequestBuilder) super.callback(listener);
    }


    @Override
    public StandardJsonRequestBuilder showLoadingDialog() {
        return (StandardJsonRequestBuilder) super.showLoadingDialog();
    }

    @Override
    public StandardJsonRequestBuilder showLoadingDialog(Activity activity) {
        return (StandardJsonRequestBuilder) super.showLoadingDialog(activity);
    }

    @Override
    public StandardJsonRequestBuilder showLoadingDialog(String loadingMsg) {
        return (StandardJsonRequestBuilder) super.showLoadingDialog(loadingMsg);
    }

    @Override
    public StandardJsonRequestBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (StandardJsonRequestBuilder) super.showLoadingDialog(activity, loadingMsg);
    }

    @Override
    public StandardJsonRequestBuilder showLoadingDialog(Dialog loadingDialog) {
        return (StandardJsonRequestBuilder) super.showLoadingDialog(loadingDialog);
    }
    @Override
    public StandardJsonRequestBuilder setCacheMode(int cacheMode) {
        return (StandardJsonRequestBuilder) super.setCacheMode(cacheMode);
    }

    /*@Override
    public StandardJsonRequestBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (StandardJsonRequestBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }*/

    @Override
    public StandardJsonRequestBuilder setRetryCount(int retryCount) {
        return (StandardJsonRequestBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public StandardJsonRequestBuilder setTimeout(int timeoutInMills) {
        return (StandardJsonRequestBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public StandardJsonRequestBuilder setIgnoreCertificateVerify() {
        return (StandardJsonRequestBuilder) super.setIgnoreCertificateVerify();
    }



    //复写string


    @Override
    public StandardJsonRequestBuilder setJsonClazz(Class clazz) {
        return (StandardJsonRequestBuilder) super.setJsonClazz(clazz);
    }

    @Override
    public StandardJsonRequestBuilder setParamsAsJson() {
        return (StandardJsonRequestBuilder) super.setParamsAsJson();
    }

    @Override
    public StandardJsonRequestBuilder setResponseJsonArray() {
        return (StandardJsonRequestBuilder) super.setResponseJsonArray();
    }

    @Override
    public StandardJsonRequestBuilder setExtraTag(Object extraTag) {
        return (StandardJsonRequestBuilder) super.setExtraTag(extraTag);
    }

    @Override
    public StandardJsonRequestBuilder setCacheMaxAge(int cacheMaxAge) {
        return (StandardJsonRequestBuilder) super.setCacheMaxAge(cacheMaxAge);
    }

    @Override
    public StandardJsonRequestBuilder setAppendCommonHeaders(boolean appendCommonHeaders) {
        return (StandardJsonRequestBuilder) super.setAppendCommonHeaders(appendCommonHeaders);
    }

    @Override
    public StandardJsonRequestBuilder setAppendCommonParams(boolean appendCommonParams) {
        return (StandardJsonRequestBuilder) super.setAppendCommonParams(appendCommonParams);
    }
}
