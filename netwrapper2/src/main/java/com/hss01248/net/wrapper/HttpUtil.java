package com.hss01248.net.wrapper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.hss01248.net.builder.DownloadBuilder;
import com.hss01248.net.builder.IClient;
import com.hss01248.net.builder.JsonRequestBuilder;
import com.hss01248.net.builder.StandardJsonRequestBuilder;
import com.hss01248.net.builder.StringRequestBuilder;
import com.hss01248.net.builder.UploadRequestBuilder;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.okhttp.OkClient;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpUtil {

    public static Context context;

    public static Handler getMainHandler() {
        if(mainHandler==null){
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }
    private static Handler mainHandler;
    private static GlobalConfig globalConfig;


    /**
     * 全局配置的入口方法,通过GlobalConfig可以配置一系列默认情况
     * @param context 传context或者application,千万不要传activity
     * @param baseUrl
     * @return
     */
    public static GlobalConfig  init(Context context,String baseUrl ){
        HttpUtil.context = context;
        globalConfig = GlobalConfig.get();
        globalConfig.setBaseUrl(baseUrl);
        return globalConfig;
    }


    /**
     * 用于切换底层框架.比如httpclient
     * @return
     */
    public static IClient getClient() {
        return OkClient.getInstance();
    }


    /**
     * 与图片加载框架共享okhttpclient
     * @param ignoreCertificateVerify
     * @return
     */
    public static OkHttpClient getClientForImageLoader(boolean ignoreCertificateVerify){
        return ((OkClient)getClient()).getOkhttpClientForImageLoader(ignoreCertificateVerify);
    }




    public  static <E> StringRequestBuilder<E> buildStringRequest(String url) {
        return new StringRequestBuilder<>().url(url);
    }


    public static <E> JsonRequestBuilder<E> buildJsonRequest(String url, Class<E> clazz) {
        JsonRequestBuilder builder = new JsonRequestBuilder();
        builder.url(url).setJsonClazz(clazz);
        return builder;
    }


    public static <E> StandardJsonRequestBuilder<E> buildStandardJsonRequest(String url, Class<E> clazz) {
        return new StandardJsonRequestBuilder<>().url(url).setJsonClazz(clazz);
    }


    public static <E> DownloadBuilder<E> buildDownloadRequest(String url) {
        return new DownloadBuilder<>().url(url);
    }


    public static <E> UploadRequestBuilder<E> buildUpLoadRequest(String url, String fileDesc, String filePath) {
        return new UploadRequestBuilder<>().url(url).addFile(fileDesc,filePath);
    }

    public static void cancelRquest(Object tag){
        getClient().cancleRequest(tag);
    }
    public static void cancleAllRequest(){
        getClient().cancleAllRequest();
    }


}
