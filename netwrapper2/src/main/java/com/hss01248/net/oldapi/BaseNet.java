package com.hss01248.net.oldapi;

/**
 * Created by Administrator on 2016/9/21.
 */
public  abstract class BaseNet<T> implements INet {//T: 请求类  call或者是Request

   /* private ILoginManager loginManager;

    public void setLoginManager(ILoginManager loginManager){
        this.loginManager = loginManager;
    }

    protected  <E> void setKeyInfo(ConfigInfo<E> info, String url, Map map, Class<E> clazz, MyNetListener<E> listener){
        info.url = url;
        info.params = map;
        info.clazz = clazz;

        info.listener = listener;//ProxyTools.getNetListenerProxy(listener,info);//使用代理:java.lang.ClassCastException: $Proxy1 cannot be cast to com.hss01248.net.wrapper.MyNetListener
        info.client = this;
    }

    @Override
    public <E> ConfigInfo<E> getString(String url, Map map,  MyNetListener<E> listener) {
        ConfigInfo<E> info = new ConfigInfo();
        setKeyInfo(info,url,map,null,listener);
        return info;
    }

    @Override
    public <E> ConfigInfo<E> postString(String url, Map map,  MyNetListener<E> listener) {
        ConfigInfo<E> info = new ConfigInfo();
        setKeyInfo(info,url,map,null,listener);

        info.method = HttpMethod.POST;
        return info;
    }

    @Override
    public <E> ConfigInfo<E> postStandardJson(String url, Map map, Class<E> clazz, MyNetListener<E> listener) {
        ConfigInfo<E> info = new ConfigInfo();
        setKeyInfo(info,url,map,clazz,listener);
        info.type = ConfigInfo.TYPE_JSON_FORMATTED;
        info.method = HttpMethod.POST;
        return info;
    }

    @Override
    public <E> ConfigInfo<E> getStandardJson(String url, Map map, Class<E> clazz, MyNetListener<E> listener) {
        ConfigInfo<E> info = new ConfigInfo<E>();
        setKeyInfo(info,url,map,clazz,listener);
        info.type = ConfigInfo.TYPE_JSON_FORMATTED;
        return info;
    }

    @Override
    public <E> ConfigInfo<E> postCommonJson(String url, Map map, Class<E> clazz, MyNetListener<E> listener) {
        ConfigInfo<E> info = new ConfigInfo();
        setKeyInfo(info,url,map,clazz,listener);
        info.method = HttpMethod.POST;
        info.type = ConfigInfo.TYPE_JSON;
        return info;
    }

    @Override
    public <E> ConfigInfo<E> getCommonJson(String url, Map map, Class<E> clazz, MyNetListener<E> listener) {
        ConfigInfo<E> info = new ConfigInfo<E>();
        setKeyInfo(info,url,map,clazz,listener);
        info.type = ConfigInfo.TYPE_JSON;
        return info;
    }

    @Override
    public <E> ConfigInfo<E> download(String url, String savedpath, MyNetListener<E> callback) {
        ConfigInfo<E> info = new ConfigInfo();
        setKeyInfo(info,url,new HashMap(),null,callback);
        info.isAppendToken = false;
        info.type = ConfigInfo.TYPE_DOWNLOAD;
        info.filePath = savedpath;
        info.timeout = 0;
        return info;
    }

    @Override
    public <E> ConfigInfo<E> autoLogin() {
        if (loginManager != null){
          return   loginManager.autoLogin();
        }
        return null;
    }

    @Override
    public <E> ConfigInfo<E> autoLogin(MyNetListener<E> myNetListener) {
        if (loginManager != null){
            return   loginManager.autoLogin(myNetListener);
        }
        return null;

    }

    @Override
    public boolean isLogin() {
        if (loginManager != null){
            return loginManager.isLogin();
        }
        return false;
    }

    @Override
    public   abstract <E>  void cancleRequest(ConfigInfo<E> tConfigInfo);

    @Override
    public <E> ConfigInfo<E> resend(ConfigInfo<E> configInfo) {
        return start(configInfo);
    }

    @Override
    public <E> ConfigInfo<E> upLoad(String url, Map<String, String> params, Map<String, String> files, MyNetListener<E> callback) {
        ConfigInfo<E> info = new ConfigInfo();
        setKeyInfo(info,url,params,null,callback);
        info.files = files;
        info.isAppendToken = false;
        info.type = ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS;
        info.timeout = 0;

        return info;
    }


    *//**
     * 在这里组装请求,然后发出去
     * @param <E>
     * @return
     *//*
    @Override
    public <E> ConfigInfo<E> start(ConfigInfo<E> configInfo) {

        String url = Tool.appendUrl(configInfo.url, isAppendUrl());
        configInfo.url = url;
        configInfo.listener.url = url;
        configInfo.listener.configInfo = configInfo;

        MyLog.i("url:"+url);

        //todo 这里token还可能在请求头中,应加上此类情况的自定义.
        if (configInfo.isAppendToken){
            Tool.addToken(configInfo.params);
        }



        if (getCache(configInfo)){//异步,去拿缓存.
            return configInfo;
        }

        //没有网络时,直接返回错误
        if(!Tool.isNetworkAvailable()){
            configInfo.listener.onNoNetwork();
            return configInfo;
        }

       // addHeaders(configInfo);//添加请求头

        if (configInfo.loadingDialog != null && !configInfo.loadingDialog.isShowing()){
            try {//预防badtoken最简便和直接的方法
                configInfo.loadingDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        MyLog.i("really request http-----------");


        T request = generateNewRequest(configInfo);//根据类型生成/执行不同的请求对象

        *//*
        这三个方式是给volley预留的
        setInfoToRequest(configInfo,request);
        cacheControl(configInfo,request);
        addToQunue(request);*//*

        return configInfo;
    }

    private <E> void addHeaders(ConfigInfo<E> configInfo) {
        //configInfo.addHeaderOfAuthorization("");
        configInfo.addHeaderOfAcceptType("application/json");
        configInfo.addHeaderOfUserAgent(NetDefaultConfig.USER_AGENT);


        *//*int method = configInfo.method;
        if(method== HttpMethod.GET){
            int requestType = configInfo.type;
            switch (requestType){
                case ConfigInfo.TYPE_STRING:
                    break;
                case ConfigInfo.TYPE_JSON:
                    break;
                case ConfigInfo.TYPE_JSON_FORMATTED:
                    break;
                case ConfigInfo.TYPE_DOWNLOAD:
                    break;
                case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:
                    break;
                default:break;
            }
        }else if (method== HttpMethod.POST){
            int requestType = configInfo.type;
            switch (requestType){
                case ConfigInfo.TYPE_STRING:
                case ConfigInfo.TYPE_JSON:
                case ConfigInfo.TYPE_JSON_FORMATTED:
                    if(configInfo.paramsAsJson){//作为json发送
                        String jsonStr = MyJson.toJsonStr(configInfo.params);
                        configInfo.addHeaderOfContentLength(jsonStr);//okhttp是否自动计算?是的,所以无需我们外层计算.

                    }else {//普通&=发送

                    }
                    break;
                case ConfigInfo.TYPE_DOWNLOAD:
                    break;
                case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:

                    break;
                default:break;
            }
        }*//*


    }

    private <E> T generateNewRequest(ConfigInfo<E> configInfo) {
        int requestType = configInfo.type;
        switch (requestType){
            case ConfigInfo.TYPE_STRING:
            case ConfigInfo.TYPE_JSON:
            case ConfigInfo.TYPE_JSON_FORMATTED:
                return  newCommonStringRequest(configInfo);
            case ConfigInfo.TYPE_DOWNLOAD:
                return newDownloadRequest(configInfo);
            case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:
                return newUploadRequest(configInfo);
            *//*case ConfigInfo.TYPE_UPLOAD_NONE_PROGRESS:
                return newUploadRequestWithoutProgress(configInfo);*//*
            default:return null;
        }
    }

    protected abstract <E> T newUploadRequestWithoutProgress(ConfigInfo<E> configInfo);

    protected abstract <E> T newUploadRequest(ConfigInfo<E> configInfo);

    protected abstract <E> T newDownloadRequest(ConfigInfo<E> configInfo);

   *//* protected abstract <E> T newStandardJsonRequest(ConfigInfo<E> configInfo);

    protected abstract <E> T newCommonJsonRequest(ConfigInfo<E> configInfo);*//*

    protected abstract <E> T newCommonStringRequest(ConfigInfo<E> configInfo);


    protected boolean isAppendUrl() {
        return false;
    }


    *//**
     * 缓存获取. 完全的客户端控制,只针对String和Json类型.
     * @param configInfo
     * @param <E>
     * @return
     *//*
    private <E> boolean getCache(final ConfigInfo<E> configInfo) {
        switch (configInfo.type){
            case ConfigInfo.TYPE_STRING:
            case ConfigInfo.TYPE_JSON:
            case ConfigInfo.TYPE_JSON_FORMATTED:{

                //拿缓存
                if (configInfo.shouldReadCache){




                    SimpleTask<String> simple = new SimpleTask<String>() {

                        @Override
                        protected String doInBackground() {
                            return ACache.get(MyNetApi.context).getAsString(Tool.getCacheKey(configInfo));
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            MyLog.i("read cache:"+result);


                            if (TextUtils.isEmpty(result)){
                                configInfo.shouldReadCache = false;

                                start(configInfo);//没有缓存就去访问网络
                            }else {
                                configInfo.isFromCache = true;
                                Tool.parseStringByType(result,configInfo);
                            }

                        }
                    };
                    simple.execute();
                    return true;
                }else {
                    return false;
                }

            }

            case ConfigInfo.TYPE_DOWNLOAD:
            case ConfigInfo.TYPE_UPLOAD_WITH_PROGRESS:
            case ConfigInfo.TYPE_UPLOAD_NONE_PROGRESS:
                return false;
            default:return false;
        }
    }*/
}
