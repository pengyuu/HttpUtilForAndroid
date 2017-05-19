package com.hss01248.net.wrapper;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MyNetApi {

    /*public static Context context;
    public static BaseNet adapter;


    *//**
     * 添加证书.如果有,一定要在init方法前面调用:在init方法后面调用无效
     * @param cerFileInRaw  证书要放在raw目录下
     *//*
    public static void addCer(int cerFileInRaw){
        if(HttpsConfig.certificates == null ){
            HttpsConfig.certificates = new ArrayList<>();
        }
        HttpsConfig.certificates.add(cerFileInRaw);
    }

    *//**
     * 注意:如果要添加https的自签名证书,一定要在此方法之前调用addcer方法
     * @param context
     * @param baseUrl
     * @param loginManager
     *//*
    public static void init(Context context,String baseUrl,ILoginManager loginManager){
        MyNetApi.context = context;
        NetDefaultConfig.baseUrl = baseUrl;
        MyNetApi.adapter = RetrofitClient.getInstance();//如果要使用rxjava,将RetrofitClient改成RxRetrofitClient即可.
        if (loginManager instanceof  BaseNet){
            throw  new RuntimeException("please implement ILoginManager independently");
            //避免可能的无限循环调用
        }
        MyNetApi.adapter.setLoginManager(loginManager);
        NetDefaultConfig.USER_AGENT = System.getProperty("http.agent");
        Log.e("e","user-agent:"+ NetDefaultConfig.USER_AGENT);

    }


    *//**
     * 指定标准格式json的三个字段.比如聚合api的三个字段分别是error_code(但有的又是resultcode),reason,result,error_code
     * 如果几个code没有,可以设为负值
     * @param tokenName
     * @param data
     * @param code
     * @param msg
     * @param codeSuccess
     * @param codeUnlogin
     * @param codeUnfound
     *//*
    public static void initAppDefault(String tokenName,String data,String code,String msg,int codeSuccess,int codeUnlogin,int codeUnfound){
        NetDefaultConfig.TOKEN = tokenName;
        NetDefaultConfig.KEY_DATA = data;
        NetDefaultConfig.KEY_CODE = code;
        NetDefaultConfig.KEY_MSG = msg;
        BaseNetBean.CODE_SUCCESS = codeSuccess;
        BaseNetBean.CODE_UNLOGIN = codeUnlogin;
        BaseNetBean.CODE_UN_FOUND = codeUnfound;
    }




    public static ConfigInfo getString(String url, Map map, MyNetListener listener) {
       return adapter.getString(url,map,listener);
    }

    public static ConfigInfo postString( String url,  Map map,  MyNetListener listener) {
        return  adapter.postString(url,map,listener);
    }


    public static ConfigInfo postStandardJson(String url, Map map, Class clazz, MyNetListener listener) {
        return adapter.postStandardJson(url,map,clazz,listener);
    }


    public static ConfigInfo getStandardJson(String url, Map map, Class clazz, MyNetListener listener) {
        return  adapter.getStandardJson(url,map,clazz,listener);
    }


    public static ConfigInfo postCommonJson(String url, Map map, Class clazz, MyNetListener listener) {
        return  adapter.postCommonJson(url,map,clazz,listener);
    }


    public static ConfigInfo getCommonJson(String url, Map map, Class clazz, MyNetListener listener) {
        return  adapter.getCommonJson(url,map,clazz,listener);
    }


    public static ConfigInfo autoLogin() {
        return  adapter.autoLogin();
    }

    public static ConfigInfo autoLogin(MyNetListener myNetListener) {
        return  adapter.autoLogin(myNetListener);
    }

    public static boolean isLogin(){
        return adapter.isLogin();
    }


    public static void cancleRequest(ConfigInfo info) {
        adapter.cancleRequest(info);
    }


    public static ConfigInfo download(String url, String savedpath, MyNetListener callback) {
       return adapter.download(url,savedpath,callback);
    }

    public static ConfigInfo upLoad(String url, Map<String,String> params,Map<String,String> files, MyNetListener callback){
      return   adapter.upLoad(url,params,files,callback);
    }

    *//**
     * 上传接口没有其他字段,而且文件对应的key为默认("file")时,可使用此简化接口
     * @param url
     * @param filePath
     * @param callback
     * @return
     *//*
    public static ConfigInfo upLoad(String url, String filePath, MyNetListener callback){
        Map<String,String> params = new HashMap<String, String>();
        Map<String,String> files = new HashMap<String, String>();
        files.put("file",filePath);
        return   adapter.upLoad(url,params,files,callback);
    }*/
}
