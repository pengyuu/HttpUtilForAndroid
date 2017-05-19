package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.interfaces.HttpMethod;
import com.hss01248.net.wrapper.MyNetListener;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class DownloadBuilder <T> extends ProgressBaseBuilder{

    public DownloadBuilder(){
        super();
        this.type = ConfigInfo.TYPE_DOWNLOAD;
    }

    public String savedPath;





    //是否打開,是否讓媒体库扫描,是否隐藏文件夹
    public boolean isOpenAfterSuccess = false;//默认不扫描
    public boolean isHideFolder = false;
    public boolean isNotifyMediaCenter = true;//媒体文件下载后,默认:通知mediacenter扫描


    //文件校验相关设置(默认不校验)MD5,SHA1,CRC32三种校验方法,任选一种即可
    public boolean isVerify = false;//是否校驗文件
    public String verifyStr;
    public boolean verfyByMd5OrShar1 = false;


    public DownloadBuilder<T> setNotifyMediaCenter(boolean notifyMediaCenter) {
        isNotifyMediaCenter = notifyMediaCenter;
        return this;
    }
    public DownloadBuilder<T> setOpenAfterSuccess() {
        isOpenAfterSuccess = true;
        return this;
    }
    public DownloadBuilder<T> setHideFile() {
        isHideFolder = true;
        return this;
    }


    public DownloadBuilder<T> savedPath(String path ){
        this.savedPath = path;
        return this;
    }

    public DownloadBuilder<T> verifyMd5(String md5Str ){
        this.isVerify = true;
        verfyByMd5OrShar1 = true;
        this.verifyStr = md5Str;
        return this;
    }

    public DownloadBuilder<T> verifyShar1(String shar1Str ){
        this.isVerify = true;
        verfyByMd5OrShar1 = false;
        this.verifyStr = shar1Str;
        return this;
    }



    @Override
    protected ConfigInfo execute() {
        method = HttpMethod.GET;
        this.type = ConfigInfo.TYPE_DOWNLOAD;
        if(TextUtils.isEmpty(savedPath) ){
            savedPath = getDefaultSavedPath();
        }
        Log.e("dd","savedPath:"+savedPath);
        Log.e("dd","url:"+url);
        return new ConfigInfo(this);
    }

    /**
     * 默认保存到download/retrofit文件夹下
     * @return
     */
    private String getDefaultSavedPath() {
        String fileName =  URLUtil.guessFileName(url,"","");
        if(TextUtils.isEmpty(fileName)){
            fileName = UUID.randomUUID().toString();
        }
       File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"retrofit");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir,fileName);
       /* if(file.exists()){
            file.delete();
        }else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        return file.getAbsolutePath();
    }

    //todo 以下的都是复写基类的方法,强转成子类

    @Override
    public DownloadBuilder url(String url) {
        return (DownloadBuilder) super.url(url);
    }

    @Override
    public DownloadBuilder addHeader(String key, String value) {
        return (DownloadBuilder) super.addHeader(key, value);
    }

    @Override
    public DownloadBuilder addParam(String key, String value) {
        return (DownloadBuilder) super.addParam(key, value);
    }

    @Override
    public DownloadBuilder addHeaders(Map headers) {
        return (DownloadBuilder) super.addHeaders(headers);
    }

    @Override
    public DownloadBuilder addParams(Map params) {
        return (DownloadBuilder) super.addParams(params);
    }

    @Override
    public DownloadBuilder addParamsInString(String paramsStr) {
        return (DownloadBuilder) super.addParamsInString(paramsStr);
    }

    @Override
    public DownloadBuilder callback(MyNetListener listener) {
        return (DownloadBuilder) super.callback(listener);
    }

    @Override
    public DownloadBuilder showLoadingDialog(Activity activity, String loadingMsg, boolean updateProgress, boolean horizontal) {
        return (DownloadBuilder) super.showLoadingDialog(activity, loadingMsg, updateProgress, horizontal);
    }

    public DownloadBuilder showLoadingDialog() {
        return (DownloadBuilder) showLoadingDialog(null,"文件下载中",updateProgress,isLoadingDialogHorizontal);
    }

    @Override
    public DownloadBuilder showLoadingDialog(Activity activity) {
        return showLoadingDialog(activity,"文件下载中");
    }

    @Override
    public DownloadBuilder showLoadingDialog(Activity activity, String loadingMsg) {
        return (DownloadBuilder) super.showLoadingDialog(activity,loadingMsg);
    }

    @Override
    public DownloadBuilder showLoadingDialog(String loadingMsg) {
        return (DownloadBuilder) super.showLoadingDialog(loadingMsg);
    }



    @Override
    public DownloadBuilder showLoadingDialog(Dialog loadingDialog) {
        return (DownloadBuilder) super.showLoadingDialog(loadingDialog);
    }

    /*@Override
    public DownloadBuilder setCacheControl(boolean shouldReadCache, boolean shouldCacheResponse, long cacheTimeInSeconds) {
        return (DownloadBuilder) super.setCacheControl(shouldReadCache, shouldCacheResponse, cacheTimeInSeconds);
    }*/

    @Override
    public DownloadBuilder setRetryCount(int retryCount) {
        return (DownloadBuilder) super.setRetryCount(retryCount);
    }

    @Override
    public DownloadBuilder setTimeout(int timeoutInMills) {
        return (DownloadBuilder) super.setTimeout(timeoutInMills);
    }

    @Override
    public DownloadBuilder setIgnoreCertificateVerify() {
        return (DownloadBuilder) super.setIgnoreCertificateVerify();
    }



    @Override
    public DownloadBuilder setExtraTag(Object extraTag) {
        return (DownloadBuilder) super.setExtraTag(extraTag);
    }

    @Override
    public DownloadBuilder setAppendCommonHeaders(boolean appendCommonHeaders) {
        return (DownloadBuilder) super.setAppendCommonHeaders(appendCommonHeaders);
    }

    @Override
    public DownloadBuilder setAppendCommonParams(boolean appendCommonParams) {
        return (DownloadBuilder) super.setAppendCommonParams(appendCommonParams);
    }
}
