package com.hss01248.net.okhttp;


import com.hss01248.net.builder.IClient;
import com.hss01248.net.cache.CacheStrategy;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.cookie.DiskCookieJar;
import com.hss01248.net.cookie.MemoryCookieJar;
import com.hss01248.net.okhttp.log.LogInterceptor;
import com.hss01248.net.okhttp.progress.UploadFileRequestBody;
import com.hss01248.net.threadpool.ThreadPoolFactory;
import com.hss01248.net.util.CollectionUtil;
import com.hss01248.net.util.HttpsUtil;
import com.hss01248.net.util.TextUtils;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.Tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/19 0019.
 */
public class OkClient extends IClient {
    private static OkClient instance;

    /**
     * 获取到一个client,以便与图片框架共享
     * @return
     */
    public  OkHttpClient getOkhttpClientForImageLoader(boolean ignoreCertificateVerify) {
        if(GlobalConfig.get().isIgnoreCertificateVerify() == ignoreCertificateVerify){
            return okhttpClient;
        }

        OkHttpClient.Builder builder = okhttpClient.newBuilder();
        setHttps(builder,ignoreCertificateVerify);
        OkHttpClient client = builder.build();
        tempClients.add(client);
        return client;
    }

    private static OkHttpClient okhttpClient;
   // private static OkHttpClient allCerPassClient;

    private static List<OkHttpClient> tempClients ;


   private OkClient(){

   }

    public Executor getExecutor(){
        ExecutorService executor = okhttpClient.dispatcher().executorService();
        if(executor==null || executor.isShutdown() || executor.isTerminated()){
            return ThreadPoolFactory.getMaxPool().getExecutor();
        }else {
            return executor;
        }
    }

   /*private OkHttpClient getAllCerPassClient(){
       if(allCerPassClient ==null){
           OkHttpClient.Builder builder = new OkHttpClient.Builder();
           HttpsUtil.setAllCerPass(builder);
           OkClient.allCerPassClient = builder
                   .connectTimeout(6000, TimeUnit.MILLISECONDS)
                   .readTimeout(0, TimeUnit.MILLISECONDS)
                   .writeTimeout(0, TimeUnit.MILLISECONDS)
                   .addNetworkInterceptor(new LogInterceptor())
                   .build();
       }
       return allCerPassClient;
   }*/

    public static OkClient getInstance(){
        if(instance ==null){
            instance = new OkClient();
            tempClients = new ArrayList<>();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            setGloablConfig(builder);

            //设置缓存文件夹
            File cacheFile = new File(HttpUtil.context.getCacheDir(), "okhttpcache");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb


            //HttpsUtil.setHttps(builder);
            OkClient.okhttpClient = builder

                    //.connectTimeout(GlobalConfig.get().getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                    .cache(cache)
                    .build();
           // okhttpClient.newBuilder().build()

        }
        return instance;
    }

    private static void setGloablConfig(OkHttpClient.Builder builder) {
        setCookie(builder,GlobalConfig.get().getCookieMode());
        setHttps(builder,GlobalConfig.get().isIgnoreCertificateVerify());

        setLog(builder,GlobalConfig.get().isOpenLog());

        builder.connectTimeout(GlobalConfig.get().getConnectTimeout(),TimeUnit.MILLISECONDS);
        //todo 拦截器的区分

        for(Interceptor interceptor : GlobalConfig.get().commonInterceptors){
            builder.addInterceptor(interceptor);
        }


        if(GlobalConfig.get().getCacheMode() != CacheStrategy.DEFAULT){
            builder.addInterceptor(new NoCacheInterceptor());
        }
    }

    private static void setLog(OkHttpClient.Builder builder, boolean openLog) {
        if(openLog){
            builder.addNetworkInterceptor(new LogInterceptor());
        }
    }

    private static <E> void setCacheStrategy(Request.Builder builder, ConfigInfo<E> info) {
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        CacheControl cacheControl;

        switch (info.cacheMode){
            case CacheStrategy.NO_CACHE:{
                //CacheControl
                cacheBuilder = new CacheControl.Builder();
                cacheBuilder.noCache();
            }
                break;
            case CacheStrategy.DEFAULT:{


            }
            break;
            case CacheStrategy.REQUEST_FAILED_READ_CACHE:{
                cacheControl = CacheControl.FORCE_NETWORK;


            }
            break;
            case CacheStrategy.IF_NONE_CACHE_REQUEST:{
                cacheControl = CacheControl.FORCE_NETWORK;

            }
            break;
            case CacheStrategy.FIRST_CACHE_THEN_REQUEST:{
                cacheControl = CacheControl.FORCE_NETWORK;

            }
            break;
            default:
            break;
        }

         cacheControl = cacheBuilder.build();
       builder.cacheControl(cacheControl);
    }

    private static void setHttps(OkHttpClient.Builder builder, boolean ignoreCertificateVerify) {
        if(ignoreCertificateVerify){
            HttpsUtil.setAllCerPass(builder);
        }else {
            HttpsUtil.setHttps(builder);
        }
    }

    private static void setCookie(OkHttpClient.Builder builder, int cookieMode) {
        CookieJar cookieJar ;
        if(cookieMode == GlobalConfig.COOKIE_MEMORY){
            cookieJar = new MemoryCookieJar();
        }else if (cookieMode == GlobalConfig.COOKIE_DISK){
            cookieJar = new DiskCookieJar();
        }else if(cookieMode == GlobalConfig.COOKIE_NONE){
            cookieJar = CookieJar.NO_COOKIES;
        }else {
            cookieJar = new MemoryCookieJar();
        }
        builder.cookieJar(cookieJar);
    }


    public <E> ConfigInfo<E> getString(final ConfigInfo<E> info) {
         /* 如果需要参数 , 在url后边拼接 :  ?a=aaa&b=bbb..... */
        Request.Builder builder = new Request.Builder();
        addTag(builder,info);

       // setCacheStrategy(builder,info.cacheMode);

        String url = Tool.generateUrlOfGET(info);
       MyLog.e("url:"+url);
        builder.url(url);
        //builder.
        addHeaders(builder,info.headers);

        cacheControl(builder,info);


        handleStringRequest(info, builder);
        return info;
    }

    /**
     * 完全安装http的缓存规范来操作缓存,只在CacheStrategy.DEFAULT时起作用,只对get请求起作用.
     * @param builder
     * @param info
     * @param <E>
     */
    private <E> void cacheControl(Request.Builder builder, ConfigInfo<E> info) {
        if(info.cacheMode != CacheStrategy.DEFAULT){
            return;
        }
        final CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge((int) info.cacheMaxAge, TimeUnit.MILLISECONDS);
        CacheControl cache = cacheBuilder.build();
        builder.cacheControl(cache);

    }

    private <E> void addTag(Request.Builder builder, ConfigInfo<E> info) {
        if(info.tagForCancle!=null){
            builder.tag(info.tagForCancle);
        }
    }

    public <E> ConfigInfo<E> postString(ConfigInfo<E> info) {

        Request.Builder builder = new Request.Builder();
        addTag(builder,info);
        builder.url(info.url);
        addHeaders(builder,info.headers);
        addPostBody(builder,info);

        handleStringRequest(info, builder);
        return info;
    }

    public ConfigInfo download(final ConfigInfo info) {
        Request.Builder builder = new Request.Builder();
        addTag(builder,info);
        String url = Tool.generateUrlOfGET(info);
        builder.url(url);
        addHeaders(builder,info.headers);
        //info.listener.registEventBus();
        requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException {
                Tool.writeResponseBodyToDisk(call,response.body(),info);
            }
        });

        return info;
    }

    public ConfigInfo upload(final ConfigInfo info) {
        Request.Builder builder = new Request.Builder();
        addTag(builder,info);
        builder.url(info.url);
        addHeaders(builder,info.headers);
        //info.listener.registEventBus();
        addUploadBody(builder,info);

               requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, final Response response) throws IOException {
                final String str = response.body().string();

                Tool.callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Tool.dismiss(info.loadingDialog);
                        info.listener.onSuccess(str,str,false);
                    }
                });
            }
        });
        return info;
    }




    private void addHeaders(Request.Builder builder, Map<String, String> headers) {

        Headers.Builder headBuilder = new Headers.Builder();
        for(Map.Entry<String,String> header   : headers.entrySet()){

            headBuilder.set(header.getKey(),header.getValue());
        }
        builder.headers(headBuilder.build());
    }

    private FormBody getFormBody(ConfigInfo info) {
        if(TextUtils.isNotEmpty(info.paramsStr)){
            Map<String,String> paramsInStr = new HashMap<>();
            String[] kvs = info.paramsStr.split("&");
            if(kvs!=null && kvs.length>0){
                for(String kv : kvs){
                    String[] kvarr = kv.split("=");
                    if(kvarr !=null && kvarr.length==2){
                        paramsInStr.put(kvarr[0],kvarr[1]);
                    }
                }
            }
            info.paramsStr="";
            info.params.putAll(paramsInStr);
        }

        Map<String,String> params = info.params;
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> param   : params.entrySet()){
            builder.add(param.getKey(),param.getValue());
        }
        return builder.build();
    }

    private void addPostBody(Request.Builder builder, ConfigInfo info) {
        RequestBody body = null;
        if(info.paramsAsJson){
            if(TextUtils.isNotEmpty(info.paramsStr) ){
                if(info.paramsStr.startsWith("{")&& info.paramsStr.endsWith("}")){
                    body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), info.paramsStr);
                }else if(info.paramsStr.startsWith("[")&& info.paramsStr.endsWith("]")){
                    body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), info.paramsStr);
                }else {
                    String jsonStr = MyJson.toJsonStr(info.params);
                    body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);
                }
            }else {
                String jsonStr = MyJson.toJsonStr(info.params);
                body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonStr);
            }




        }else {
            body=   getFormBody(info);
        }
        builder.post(body);
    }

    private <E> void addUploadBody(Request.Builder builder0, ConfigInfo<E> configInfo) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (configInfo.params != null && configInfo.params.size() >0){
            Map<String,String> params = configInfo.params;
            int count = params.size();
            if (count>0){
                Set<Map.Entry<String,String>> set = params.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addFormDataPart(key,value);
                }
            }
        }

        if (configInfo.files != null && configInfo.files.size() >0){
            Map<String,String> files = configInfo.files;
            int count = files.size();
            int index=0;

            if (count>0){
                Set<Map.Entry<String,String>> set = files.entrySet();
                for (Map.Entry<String,String> entry : set){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    File file = new File(value);
                    UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, Tool.getMimeType(value),configInfo,index);
                    builder.addFormDataPart(key,file.getName(),fileRequestBody);
                    index++;
                }
            }
        }

        //MultipartBody body = builder.build();
        builder0.post(builder.build());
    }

    private <E> void handleStringRequest(final ConfigInfo<E> info, Request.Builder builder) {

        requestAndHandleResoponse(info, builder, new ISuccessResponse() {
            public void handleSuccess(Call call, Response response) throws IOException {
               /* String type = response.header("Content-Encoding");
                byte[] bytes = response.body().bytes();
                String str = new String(bytes,"utf-8");*/
               // String str =  response.body().source().readString(Charset.forName("gb2312"));

               /* if("gzip".equals(type)){
                    str = GZipUtil.uncompress(str);
                }*/
                String str = response.body().string();
                if(info.cacheMode== CacheStrategy.DEFAULT){
                    if(response.cacheResponse()==null || response.cacheResponse().body()==null
                            || TextUtils.isEmpty(response.cacheResponse().body().string())){
                        Tool.parseStringByType(str,info,false);//说明不是从缓存来的
                    }else {
                        Tool.parseStringByType(str,info,true);//说明是从缓存来的
                    }
                }else {
                    Tool.parseStringByType(str,info,false);
                }

                Tool.dismiss(info.loadingDialog);
            }
        });
    }

    private <E> void requestAndHandleResoponse(final ConfigInfo<E> info, Request.Builder builder,
                                               final ISuccessResponse successResponse) {
        final Request request = builder.build();
        final OkHttpClient theClient = getInstance(info);
        /*if(info.ignoreCer){
            if(allCerPassClient==null){
                allCerPassClient = getAllCerPassClient();
            }
            theClient = allCerPassClient;
        }else {
            theClient = okhttpClient;
        }*/
        Call call = theClient.newCall(request);
        info.request = call;
        if(info.isSync){//同步请求
            try {
              final Response response =   call.execute();
                if(response.isSuccessful()){
                    successResponse.handleSuccess(call,response);
                }else if(call.isCanceled()){
                    info.listener.onCancel();
                }else {
                    info.listener.onCodeError("http错误码:"+response.code(),response.message(),response.code());
                }

            } catch (IOException e) {

                info.listener.onError(e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        //异步请求
        call.enqueue(new Callback() {
            public void onFailure(final Call call, final IOException e) {
                if(tempClients.contains(theClient)){//将临时client移除
                    tempClients.remove(theClient);
                }

                Tool.callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {

                        if(call.isCanceled()){
                            Tool.dismiss(info.loadingDialog);
                            info.listener.onCancel();

                        }else {
                            if(info.cacheMode == CacheStrategy.REQUEST_FAILED_READ_CACHE){
                                info.shouldReadCache = true;
                                start(info);
                            }else {
                                Tool.dismiss(info.loadingDialog);
                                info.listener.onError(e.getMessage());
                            }

                        }

                    }
                });

                e.printStackTrace();
            }
            public void onResponse(Call call, final Response response) throws IOException {
                if(tempClients.contains(theClient)){//将临时client移除
                    tempClients.remove(theClient);
                }
                if(response.isSuccessful()){
                    successResponse.handleSuccess(call,response);
                }else {
                    Tool.callbackOnMainThread(new Runnable() {
                        @Override
                        public void run() {

                            if(info.cacheMode == CacheStrategy.REQUEST_FAILED_READ_CACHE){
                                info.shouldReadCache = true;
                                start(info);
                            }else {
                                Tool.dismiss(info.loadingDialog);
                                info.listener.onCodeError("http错误码:"+response.code(),response.message(),response.code());
                            }



                        }
                    });

                }
            }
        });
    }

    interface ISuccessResponse{
        void handleSuccess(Call call, Response response) throws IOException;
    }

    @Override
    public void cancleRequest(Object tag) {
        cancel(okhttpClient,tag);

        for(OkHttpClient client1: tempClients){
            cancel(client1,tag);
        }
    }

    private void cancel(OkHttpClient client, Object tag) {
        if(client==null || tag==null){
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    @Override
    public void cancleAllRequest() {
        if(okhttpClient !=null){
            okhttpClient.dispatcher().cancelAll();
        }

        for(OkHttpClient client1: tempClients){
            client1.dispatcher().cancelAll();
        }



    }

    //@Override
    private  OkHttpClient getInstance(final ConfigInfo info) {
        if(differentWithGlobal(info)){
            return getInstance().okhttpClient;
        }

        OkHttpClient.Builder builder = getInstance().okhttpClient.newBuilder();
        setCookie(builder,info.cookieMode);
        setHttps(builder,info.isVerify);

        if(info.interceptors.size() > GlobalConfig.get().commonInterceptors.size()){
            int size = GlobalConfig.get().commonInterceptors.size();
            for(int i=size; i<info.interceptors.size();i++){
                builder.addInterceptor((Interceptor) info.interceptors.get(i));
            }
        }



        //更换缓存控制头
        if(info.cacheMode!= CacheStrategy.DEFAULT){
            builder.addInterceptor(new NoCacheInterceptor());
        }else {
            CollectionUtil.filter(builder.interceptors(), new CollectionUtil.Filter<Interceptor>() {
                @Override
                public boolean isRemain(Interceptor item) {
                    if(item instanceof  NoCacheInterceptor){
                        return false;
                    }
                    return true;
                }
            });
        }

        //setLog(builder,info.lo);

        builder.connectTimeout(info.timeout,TimeUnit.MILLISECONDS);

        OkHttpClient client = builder.build();
        tempClients.add(client);

        return client;
    }

    private  boolean differentWithGlobal(ConfigInfo info){

        if(info.cookieMode >0 && info.cookieMode!= GlobalConfig.get().getCookieMode()){
            return true;
        }
        if(info.ignoreCer != GlobalConfig.get().isIgnoreCertificateVerify() ){
            return true;
        }

        if(info.timeout >0 && info.timeout != GlobalConfig.get().getConnectTimeout() ){
            return true;
        }

        if(info.cacheMode != GlobalConfig.get().getCacheMode()){
            return true;
        }

        return false;
    }


}
