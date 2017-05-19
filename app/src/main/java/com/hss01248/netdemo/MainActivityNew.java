package com.hss01248.netdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hss01248.net.cache.CacheStrategy;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.util.LoginManager;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyJson;
import com.hss01248.net.wrapper.MyLog;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.netdemo.bean.GetCommonJsonBean;
import com.hss01248.netdemo.bean.GetStandardJsonBean;
import com.hss01248.netdemo.bean.PostStandardJsonArray;
import com.hss01248.netdemo.bean.UserInfo;
import com.hss01248.netdemo.bean.VersionInfo;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityNew extends Activity {


    @Bind(R.id.get_string)
    Button getString;
    @Bind(R.id.post_string)
    Button postString;
    @Bind(R.id.get_json)
    Button getJson;
    @Bind(R.id.post_json)
    Button postJson;
    @Bind(R.id.get_standard_json)
    Button getStandardJson;
    @Bind(R.id.post_standard_json)
    Button postStandardJson;
    @Bind(R.id.download)
    Button download;
    @Bind(R.id.upload)
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
       Logger.init("netapi");
        //HttpUtil.initAddHttps(R.raw.srca);//添加12306的证书
        HttpUtil.init(getApplicationContext(),"http://api.qxinli.com:9005/api/")
                .setStandardJsonKeys("data","code","message")
                .setStandardJsonCodes(0,5,2)
                .setTokenInfo("sessionId",0)
               // .addCrtificateRaw(R.raw.srca)
                //.addCrtificateAssert("srca.cer")
                .openLog("okhttp")
                .setLoginManager(new LoginManager() {
                    @Override
                    public void autoLogin(@Nullable final MyNetListener listener) {

                        //注意: 应从sp中读取,加密存取,加密传输.这里的demo省略了这些安全性措施
                        login("15989366579965", "123456", new MyNetListener<UserInfo>() {
                            @Override
                            public void onSuccess(UserInfo response, String resonseStr, boolean isFromCache) {
                                GlobalConfig.get().updateToken(response.sessionId);

                                if(listener!=null)
                                listener.onSuccess(response,resonseStr,isFromCache);

                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                if(listener!=null)
                                listener.onError(msgCanShow);

                            }
                        });

                    }
                });




        //HttpUtil.initAppDefault("session_id","data","code","msg",0,5,2);










    }

    public MyNetListener getListener(final MyNetListener listener){
        return listener;
        /*return ProxyUtil.getProxy(listener, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(!"isResponseFromCache".equals(method.getName())){
                    if(listener.isResponseFromCache()){
                        MyLog.e("---from cache-----listener: method:"+method.getName());
                    }else {
                        MyLog.e("---from net  -----listener: method:"+method.getName());
                    }
                }
                Object obj = method.invoke(proxy,args);
                return obj;
            }
        });*/
    }


    public void login(String username,String pw,MyNetListener listener){
        HttpUtil.buildStandardJsonRequest("xxxx/login/v1.json",UserInfo.class)
                .addParam("account",username)
                .addParam("password",pw)
                .addParam("platform", "Android")
                .addParam("imei","352203065209543543037")
                .setCacheMode(CacheStrategy.NO_CACHE)
                .postAsync(listener);
    }




    @OnClick({R.id.get_string, R.id.post_string, R.id.get_json, R.id.post_json, R.id.get_standard_json,
            R.id.post_standard_json, R.id.download, R.id.upload,R.id.postbyjson,R.id.testvoice,R.id.testvoice2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_string:
                //测试自签名/未被android系统承认的的https
                HttpUtil.buildStringRequest("https://kyfw.12306.cn/otn/regist/init")
                        .setCacheMode(CacheStrategy.DEFAULT)
                        .callback(getListener(new MyNetListener<String>() {
                                    @Override
                                    public void onSuccess(String response, String resonseStr,boolean isFromCache) {
                                        Logger.e(response);
                                        if(isFromCache){
                                            MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                        }else {
                                            MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        super.onError(error);
                                        Logger.e(error);
                                        if(isResponseFromCache()){
                                            MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                        }else {
                                            MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                        }
                                    }
                            }))
                        //.setIgnoreCertificateVerify()
                        .getAsync();
                break;
            case R.id.post_string:

                HttpUtil.buildStringRequest("article/getArticleCommentList/v1.json")
                        .addParam("pageSize","30")
                        .addParam("articleId","1738")
                        .setCacheMode(CacheStrategy.IF_NONE_CACHE_REQUEST)
                        .addParam("pageIndex","1")
                        .callback(getListener(new MyNetListener<String>() {
                            @Override
                            public void onSuccess(String response, String resonseStr,boolean isFromCache) {
                                Logger.e(response);
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                                if(isResponseFromCache()){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        })).postAsync();
                break;
            case R.id.get_json:

                HttpUtil.buildJsonRequest("version/latestVersion/v1.json",GetCommonJsonBean.class)
                        .showLoadingDialog()
                        .setCacheMode(CacheStrategy.DEFAULT)
                        .callback(getListener(new MyNetListener<GetCommonJsonBean>() {
                            @Override
                            public void onSuccess(GetCommonJsonBean response, String resonseStr,boolean isFromCache) {
                                Logger.json(MyJson.toJsonStr(response));
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                                if(isResponseFromCache()){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        }))
                        .getAsync();
                break;
            case R.id.post_json:

                login("159893675769965","123456",new MyNetListener<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo response, String resonseStr,boolean isFromCache) {
                        Logger.json(MyJson.toJsonStr(response));
                        GlobalConfig.get().updateToken(response.sessionId);


                        if(isFromCache){
                            MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                        }else {
                            MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                        }
                    }
                    @Override
                    public void onError(String msgCanShow) {
                        super.onError(msgCanShow);
                        Logger.e(msgCanShow);
                        if(isResponseFromCache()){
                            MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                        }else {
                            MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                        }
                    }
                });



                /*HttpUtil.buildJsonRequest("article/getArticleCommentList/v1.json",PostCommonJsonBean.class)
                        .addParam("pageSize","30")
                        .addParam("articleId","1738")
                        .setCacheMode(CacheStrategy.FIRST_CACHE_THEN_REQUEST)
                        .addParam("pageIndex","1")
                        .callback(getListener(new MyNetListener<PostCommonJsonBean>() {
                            @Override
                            public void onSuccess(PostCommonJsonBean response, String resonseStr,boolean isFromCache) {
                                    Logger.json(MyJson.toJsonStr(response));
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                                if(isResponseFromCache()){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        })).postAsync();*/

                break;
            case R.id.get_standard_json:

                /*	聚合api:笑话大全
                    sort	string	是	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
                    page	int	否	当前页数,默认1
                    pagesize	int	否	每次返回条数,默认1,最大20
                    time	string	是	时间戳（10位），如：1418816972
                    key 	string  您申请的key*/
                Map<String,String> map4 = new HashMap<>();
                map4.put("sort","desc");
                map4.put("page","1");
                map4.put("pagesize","4");
                map4.put("time",System.currentTimeMillis()/1000+"");
                map4.put("key","fuck you");


                HttpUtil.buildStandardJsonRequest("http://japi.juhe.cn/joke/content/list.from",GetStandardJsonBean.class)
                        .addParam("sort","desc")
                        .addParam("page","1")
                        .addParam("pagesize","4")
                        .addParam("time",System.currentTimeMillis()/1000+"")
                        .addParam("key","fuck you")
                        .setStandardJsonKey("result","error_code","reason")
                        .setCustomCodeValue(0,2,-1)
                        .setCacheMode(CacheStrategy.FIRST_CACHE_THEN_REQUEST)
                        .showLoadingDialog()
                        .callback(getListener(new MyNetListener<GetStandardJsonBean>() {
                            @Override
                            public void onSuccess(GetStandardJsonBean response, String resonseStr,boolean isFromCache) {
                                Logger.json(MyJson.toJsonStr(response));
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                            @Override
                            public void onError(String error) {
                                super.onError(error);
                                Logger.e(error);
                                if(isResponseFromCache()){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        }))
                        .getAsync();
                break;
            case R.id.post_standard_json:

                HttpUtil.buildStandardJsonRequest("article/getArticleCommentList/v1.json",PostStandardJsonArray.class)
                        .addParam("pageSize","30")
                        .addParam("articleId","1738")
                        .addParam("pageIndex","1")
                        .setCacheMode(CacheStrategy.REQUEST_FAILED_READ_CACHE)
                        .setResponseJsonArray()
                        .postAsync(getListener(new MyNetListener<PostStandardJsonArray>() {
                            @Override
                            public void onSuccess(PostStandardJsonArray response, String resonseStr,boolean isFromCache) {
                               // Logger.json(MyJson.toJsonStr(response));

                            }

                            @Override
                            public void onSuccessArr(List<PostStandardJsonArray> response, String responseStr, String data, int code, String msg,boolean isFromCache) {
                                super.onSuccessArr(response, responseStr, data, code, msg,isFromCache);
                                Logger.json(MyJson.toJsonStr(response));
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        }));
                break;
            case R.id.download:
                /*File dir = Environment.getExternalStorageDirectory();
                final File file = new File(dir,"2.jpg");
                if (file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                String url = "https://travel.12306.cn/imgs/resources/uploadfiles/images/fed7d5b4-37d3-4f32-bacc-e9b942cb721d_product_W572_H370.jpg";
                String url2 = "http://www.qxinli.com/download/qxinli.apk";
                HttpUtil.buildDownloadRequest(url2)
                        .showLoadingDialog()//显示下载进度dialog
                        //.savedPath(path)
                        .setOpenAfterSuccess()//下载完成后打开
                        .setHideFile()//隐藏该文件
                        .setNotifyMediaCenter(false)
                        .verifyShar1("76DAB206AE43FB81A15E9E54CAC87EA94BB5B384")//下载完后校验md5,如果djso8d89dsjd9s7dsfj
                        .getAsync(getListener(new MyNetListener() {
                            @Override
                            public void onSuccess(Object response, String onSuccess,boolean isFromCache) {
                                Logger.e("onSuccess:"+onSuccess);
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }

                            @Override
                            public void onProgressChange(long fileSize, long downloadedSize) {
                                super.onProgressChange(fileSize, downloadedSize);
                                Logger.e("progress:"+downloadedSize+"--filesize:"+fileSize);
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                                if(isResponseFromCache()){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        }));
                break;
            case R.id.upload:


                HttpUtil.buildUpLoadRequest("http://192.168.1.100:8080/gm/file/q_uploadAndroidApk.do","uploadFile","/storage/emulated/0/qxinli.apk")
                        .addFile("uploadFile","/storage/emulated/0/Download/retrofit/qxinli.apk")
                        .addParam("uploadFile555","1474363536041.jpg")
                        .addParam("api_secret777","898767hjk")
                        .showLoadingDialog()
                        .postAsync(getListener(new MyNetListener<String>() {
                                            @Override
                                            public void onSuccess(String response, String resonseStr,boolean isFromCache) {
                                                Logger.e(resonseStr);
                                            }

                                            @Override
                                            public void onError(String error) {
                                                super.onError(error);
                                                Logger.e("error:"+error);
                                            }

                                            @Override
                                            public void onProgressChange(long downloadedSize, long fileSize) {
                                                super.onProgressChange(fileSize, downloadedSize);
                                                Logger.e("upload onProgressChange:"+downloadedSize + "  total:"+ fileSize +"  progress:"+downloadedSize*100/fileSize);
                                            }
                                        }));
                break;

            case R.id.postbyjson:


                HttpUtil.buildStandardJsonRequest("http://app.cimc.com:9090/app/appVersion/getLatestVersion",VersionInfo.class)
                        .addParam("versionName","1.0.0")
                        .addParam("appType","0")
                        .setParamsAsJson()
                        .showLoadingDialog("jiaxxx")
                        .setCustomCodeValue(1,2,3)
                        .callback(getListener(new MyNetListener<VersionInfo>() {


                            @Override
                            public void onSuccess(VersionInfo response, String resonseStr,boolean isFromCache) {
                                Logger.e(resonseStr);
                                if(isFromCache){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }

                            @Override
                            public void onEmpty() {
                                super.onEmpty();
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                                if(isResponseFromCache()){
                                    MyLog.e("---from cache-----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }else {
                                    MyLog.e("---from net  -----listener: method:"+Thread.currentThread() .getStackTrace()[0].getMethodName());
                                }
                            }
                        }))
                        .postAsync();
                break;
            case R.id.testvoice:


               /* HttpUtil.buildJsonRequest("version/latestVersion/v1.json",GetCommonJsonBean.class)
                        .setCacheControl(true,true,60)
                        .showLoadingDialog(MainActivityNew.this,"")
                        .callback(new MyNetListener<GetCommonJsonBean>() {
                            @Override
                            public void onSuccess(GetCommonJsonBean response, String resonseStr) {
                                Logger.json(MyJson.toJsonStr(response));
                            }
                        })
                        .get();*/

                HttpUtil.buildStandardJsonRequest("article/getArticleCommentList/v1.json",PostStandardJsonArray.class)
                        .addParam("pageSize","30")
                        .addParam("articleId","1738")
                        .addParam("pageIndex","1")
                        .setResponseJsonArray()
                        .callback(getListener(new MyNetListener<PostStandardJsonArray>() {
                            @Override
                            public void onSuccess(PostStandardJsonArray response, String resonseStr,boolean isFromCache) {
                                //Logger.json(MyJson.toJsonStr(response));
                            }

                            @Override
                            public void onSuccessArr(List<PostStandardJsonArray> response, String responseStr, String data, int code, String msg,boolean isFromCache) {
                                super.onSuccessArr(response, responseStr, data, code, msg,isFromCache);
                                Logger.json(MyJson.toJsonStr(response));
                            }
                        }))
                        .postAsync();
                break;
            case R.id.testvoice2:{


                 /*
                $sign = get_sign($appkey, $params, $secret, $time);
                签名算法：使用MD5加密 MD5（appkey + interfaces +cti + act+ params+appSecret+time） 注”+”不包含.

                http://api.mixcom.cn/v2/?m=interfaces&c=virt&a=index&act=bindnumber&appkey=d6906c470a7886edaa99802cb87fd465&sign=e4842570f261ff1571ae541371f1e809&time=1480327411
                   &virtualnumber=86170****0673&aparty=86153****2774&bparty=86183****9530&recording=0&endDate=2016-01-01 00:00:00*/

                String smallNum = "8617092580665";
                String aparty = "8615989369965";
                String bparty = "8617722810218";
                String appkey = "8b575f9208f4181d974b72a71ca3ad24";
                String appSecret = "ebvbBE";
                String timeStamp2 = System.currentTimeMillis()/1000+"";
                Logger.e("time:"+timeStamp2);

                String act = "bindnumber";
                String c = "virt";
                String m = "interfaces";

                String params = "";
                params=smallNum + aparty + bparty;

                String str = appkey + m +c + act + params + appSecret + timeStamp2;
                Logger.e("str:"+str);
                String sign = "";
                Logger.e("签名后的:"+sign);

                Map map11 = new HashMap<>();
                map11.put("m",m);
                map11.put("c",c);
                map11.put("a","index");
                map11.put("act",act);
                map11.put("appkey",appkey);
                map11.put("sign",sign);
                map11.put("time",timeStamp2);

                map11.put("virtualnumber",smallNum);
                map11.put("aparty",aparty);
                map11.put("bparty",bparty);

               /* MyNetApi.getStandardJson("http://api.mixcom.cn/v2/",
                        map11, VersionInfo.class, new MyNetListener<VersionInfo>() {
                            @Override
                            public void onSuccess(VersionInfo response, String resonseStr) {
                                Logger.e(resonseStr);
                            }

                            @Override
                            public void onEmpty() {
                                super.onEmpty();
                            }

                            @Override
                            public void onError(String msgCanShow) {
                                super.onError(msgCanShow);
                                Logger.e(msgCanShow);
                            }
                        })
                        .setIsAppendToken(false)
                        .setCustomCodeValue(200,-1,-1)
                        .start();*/
            }


                break;


        }
    }

    String app_key = "4d3d1f40e7a841316084b64c0c4575b1";
    String app_secert = "VQ8bciAjkUl4fiTTvafdvTLnBNGlSS";

  /*  @OnClick({R.id.button, R.id.button2, R.id.button3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
               *//* RetrofitAdapter.getInstance().getString("api/msg/unreadMsg/count/v1.json", new HashMap(), "dd", new MyNetCallback() {
                    @Override
                    public void onSuccess(Object response, String resonseStr) {
                        Log.e("baidu",response.toString());
                    }
                });*//*

                String obj1 = "{\"data\":null,\"code\":0}";
                String obj2 = "{\"data\":{},\"code\":0}";
                String obj3 = "{\"data\":[],\"code\":0}";

                BaseNetBean<TestBean> netBean1 = MyJson.parseObject(obj1,BaseNetBean.class);
                BaseNetBean<TestBean> netBean2 = MyJson.parseObject(obj2,BaseNetBean.class);
                BaseNetBean<List<TestBean>> netBean3 = MyJson.parseObject(obj3,BaseNetBean.class);

                break;
            case R.id.button2:
                Map<String, String> map = new HashMap<String, String>();
               // map.put("id", "145");
                RetrofitAdapter.getInstance().postStandardJson("api/voice/categoryList/v1.json",
                        map, "kk", new MyNetListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response, String resonseStr) {
                        Log.e("postStandardJson","onSuccess");
                    }

                    @Override
                    public void onSuccess(JSONObject response, String responseStr, String data, int code, String msg) {
                       // super.onSuccess(response, responseStr, data, code, msg);
                        Log.e("postStandardJson","onSuccess long "+ "code:"+code + "--msg:"+ msg + "--data:"+data);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        Log.e("postStandardJson","onError:"+error);
                    }
                });
                break;
            case R.id.button3:
                File file = new File(Environment.getExternalStorageDirectory(),"qxinli.apk");
                if (!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String path = file.getAbsolutePath();
                RetrofitAdapter.getInstance().download("http://www.qxinli.com/download/qxinli.apk", path, new MyNetListener<String>() {
                    @Override
                    public void onSuccess(String response, String resonseStr) {
                        Log.e("download","onSuccess:"+ response);
                    }

                    @Override
                    public void onProgressChange(long fileSize, long downloadedSize) {
                        super.onProgressChange(fileSize, downloadedSize);
                        Log.e("download","onProgressChange:"+downloadedSize+ "--totalsize:"+ fileSize);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        Log.e("download","error:"+error);
                    }
                });
                break;
        }
    }*/
}
