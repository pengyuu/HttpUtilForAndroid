package com.hss01248.net.builder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;

import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.interfaces.HttpMethod;
import com.hss01248.net.util.CollectionUtil;
import com.hss01248.net.util.MyActyManager;
import com.hss01248.net.util.TextUtils;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.net.wrapper.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class BaseNetBuilder<T> {


    public BaseNetBuilder setAppendCommonHeaders(boolean appendCommonHeaders) {
        isAppendCommonHeaders = appendCommonHeaders;
        return this;
    }

    public BaseNetBuilder setAppendCommonParams(boolean appendCommonParams) {
        isAppendCommonParams = appendCommonParams;
        return this;
    }

    public boolean isAppendCommonHeaders = GlobalConfig.get().isAppendCommonHeaders();
    public boolean isAppendCommonParams = GlobalConfig.get().isAppendCommonParams();
    public Map<String,String> params = new HashMap<>();
    public Map<String,String> headers = new HashMap<>();
    public MyNetListener<T> listener;
    public String url;
    public int method ;
    public int type ;//= ConfigInfo.TYPE_STRING;
    public String responseCharset;

    public List<Interceptor> interceptors = GlobalConfig.get().commonInterceptors;

    public BaseNetBuilder addInterceptor(Interceptor interceptor) {
        if(interceptors ==null){
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
        return this;
    }

    public BaseNetBuilder setExtraTag(Object extraTag) {
        this.extraTag = extraTag;
        return this;
    }

    public Object extraTag;

    public BaseNetBuilder(){


        headers = new HashMap<String,String>();
        params = new HashMap<String,String>();
        if(TextUtils.isNotEmpty(GlobalConfig.get().getUserAgent())){
            headers.put("User-Agent", GlobalConfig.get().getUserAgent());
        }
        // headers.put("Accept","*/*");
        headers.put("Connection","Keep-Alive");
        //headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)");
        isSync = false;
        responseCharset = "utf-8";


    }
    public boolean isSync;
    public boolean paramsAsJson = false;
    public BaseNetBuilder setParamsPostAsJson() {
        this.paramsAsJson = true;
        return this;
    }

    /**
     * 以已经成型的参数形式来设置
     * @param paramsStr
     */
    public BaseNetBuilder addParamsInString(String paramsStr) {
        this.paramsStr = paramsStr;
        return this;
    }

    public BaseNetBuilder setResponseCharset(String responseCharset) {
        this.responseCharset = responseCharset;
        return this;
    }

    public String paramsStr;



    public BaseNetBuilder setCookieMode(int cookieMode) {
        this.cookieMode = cookieMode;
        return this;
    }


    public int cookieMode;









    //todo 以下是http请求基本组成
    //todo 以下是http请求基本组成
    public BaseNetBuilder<T> addHeader(String key, String value){
        if(key!=null && value!=null){
            headers.put(key,value);
        }

        return this;
    }
    public BaseNetBuilder<T> addHeaders(Map<String,String> headers){
        if(headers !=null){
            this.headers.putAll(headers);
        }

        return this;
    }

    public BaseNetBuilder<T> addParams(Map<String,String> params){
        //todo url encode 一下


        if(params!=null){
            this.params.putAll(params);
        }
        return this;
    }


    /**
     * 在此处完成urlencode功能
     * */
    public BaseNetBuilder<T> addParam(String key, String value){

        if(key!=null && value!=null){
            params.put(Tool.urlEncode(key), Tool.urlEncode(value));
        }
        return this;
    }

    public BaseNetBuilder<T> callback(MyNetListener<T> listener){
        this.listener = listener;
        return this;
    }

    public BaseNetBuilder<T> url(String url ){
        this.url = url;
        return this;
    }

    @Deprecated
    public ConfigInfo<T> getSync(){
        method = HttpMethod.GET;
        //client.start(this);
        return   execute();

    }

    public ConfigInfo<T> getAsync(){
        method = HttpMethod.GET;
        isSync = false;
        //client.start(this);
        return   execute();

    }

    public ConfigInfo<T> getAsync(MyNetListener<T> listener){
        method = HttpMethod.GET;
        this.listener = listener;
        isSync = false;
        //client.start(this);
        return   execute();

    }
    @Deprecated
    public ConfigInfo<T> postSync(){
        method = HttpMethod.POST;
        // client.start(this);
        return  execute();

    }

    public ConfigInfo<T> postAsync(){
        method = HttpMethod.POST;
        isSync = false;
        // client.start(this);
        return  execute();

    }

    public ConfigInfo<T> postAsync(MyNetListener<T> listener){
        method = HttpMethod.POST;
        this.listener = listener;
        isSync = false;
        // client.start(this);
        return  execute();

    }

    protected ConfigInfo<T> execute(){
        if(validate()){
            return new ConfigInfo<T>(this);
        }else {
            return null;
        }
    }

    protected boolean validate() {

        CollectionUtil.filterMap(headers, new CollectionUtil.MapFilter<String, String>() {
            public boolean isRemain(Map.Entry<String, String> entry) {
                if(entry.getValue() == null){
                    entry.setValue("");
                }
                return true;
            }
        });
        CollectionUtil.filterMap(params, new CollectionUtil.MapFilter<String, String>() {
            public boolean isRemain(Map.Entry<String, String> entry) {
                if(entry.getValue() == null){
                    entry.setValue("");
                }
                return true;
            }
        });
        return true;
    }



    //TODO 以下是UI显示控制
    public Dialog loadingDialog;
    public BaseNetBuilder<T> showLoadingDialog(){
        return setShowLoadingDialog(null,null,"加载中...",false,false);
    }
    public BaseNetBuilder<T> showLoadingDialog(Activity activity){
        return setShowLoadingDialog(activity,null,"加载中...",false,false);
    }

    public BaseNetBuilder<T> showLoadingDialog(Activity activity,String loadingMsg){
        return setShowLoadingDialog(activity,null,loadingMsg,false,false);
    }
    public BaseNetBuilder<T> showLoadingDialog(String loadingMsg){
        return setShowLoadingDialog(null,null,loadingMsg,false,false);
    }
    /**
     *
     * @return
     */
    public BaseNetBuilder<T> showLoadingDialog(Dialog loadingDialog){

        return  setShowLoadingDialog(null,loadingDialog,"",false,false);
    }

    protected BaseNetBuilder<T> setShowLoadingDialog(Activity activity,Dialog loadingDialog, String msg,boolean updateProgress,boolean horizontal){

        if(activity ==null){
            activity = MyActyManager.getInstance().getCurrentActivity();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(activity.isDestroyed()){
                activity = null;
            }
        }


        if (loadingDialog == null){
            if (TextUtils.isEmpty(msg)){
                msg = "加载中...";
            }
            if (activity == null){
                this.loadingDialog = null;//todo 生成dialog,先不显示
            }else {
                try {
                    //new ProgressDialog(activity).setTitle("");
                   // this.loadingDialog = ProgressDialog.show(activity, "", msg,!updateProgress, true);
                    ProgressDialog dialog = new ProgressDialog(activity);
                    dialog.setTitle("");
                    dialog.setMessage(msg);
                    dialog.setProgressStyle(horizontal ? ProgressDialog.STYLE_HORIZONTAL:ProgressDialog.STYLE_SPINNER);
                    dialog.setIndeterminate(!updateProgress);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);
                    this.loadingDialog = dialog;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            this.loadingDialog = loadingDialog;
        }
        return this;
    }


   /* public BaseNetBuilder<T> setCacheControl(boolean shouldReadCache,boolean shouldCacheResponse,long cacheTimeInSeconds){
        this.shouldReadCache = shouldReadCache;
        this.shouldCacheResponse = shouldCacheResponse;
        this.cacheMaxAge = cacheTimeInSeconds;
        return this;

    }*/




    //todo 以下是超时以及重试策略
    public int retryCount = GlobalConfig.get().getRetryCount();
    public int timeout = GlobalConfig.get().getConnectTimeout();

    public void setTagForCancle(Object tagForCancle) {
        this.tagForCancle = tagForCancle;
    }

    public Object tagForCancle;

    public BaseNetBuilder<T> setRetryCount(int retryCount){
        this.retryCount = retryCount;
        return this;
    }

    public BaseNetBuilder<T> setTimeout(int timeoutInMills){
        this.timeout = timeoutInMills;
        return this;
    }

    //TODO https自签名证书的处理策略:单个请求是否忽略
    public boolean ignoreCertificateVerify = false;

    public BaseNetBuilder<T> setIgnoreCertificateVerify() {
        this.ignoreCertificateVerify = true;
        return this;
    }

    public boolean isIgnoreCertificateVerify() {
        return ignoreCertificateVerify;
    }



    //TODO token身份验证字段的拼接
    /*public boolean isAppendToken = false;//默认没有token验证
    public boolean isInHeaderOrParam = false;//默认在参数体中传递
    public BaseNetBuilder<T> setIsAppendToken(boolean isAppendToken,boolean isInHeaderOrParam){
        this.isAppendToken = isAppendToken;
        this.isInHeaderOrParam = isInHeaderOrParam;
        return this;
    }*/

    public BaseNetBuilder<T> setTag(Object object){
        return this;
    }




}
