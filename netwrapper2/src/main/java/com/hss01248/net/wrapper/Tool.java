package com.hss01248.net.wrapper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.hss01248.net.cache.ACache;
import com.hss01248.net.cache.CacheStrategy;
import com.hss01248.net.config.ConfigInfo;
import com.hss01248.net.config.GlobalConfig;
import com.hss01248.net.util.FileUtils;
import com.hss01248.net.util.LoginManager;
import com.hss01248.net.util.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/9/21.
 */
public class Tool {


    public static String urlEncode(String string)  {
        String str = "";
        try {
            str=  URLEncoder.encode(string,"UTF-8");
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }

    public static void updateProgress(ConfigInfo info,long progress,long max){
        if(info.loadingDialog instanceof ProgressDialog && info.loadingDialog.isShowing()){
            ProgressDialog dialog = (ProgressDialog) info.loadingDialog;
            dialog.setProgress((int) progress);
            dialog.setMax((int) max);
        }
    }


    public static boolean writeResponseBodyToDisk(final Call call, ResponseBody body, final ConfigInfo info) throws IOException{
       // try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(info.filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                long oldTime = 0L;
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    //MyLog.d( "file download: " + fileSizeDownloaded + " of " + fileSize);//todo 控制频率

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - oldTime > 300 || fileSizeDownloaded == fileSizeDownloaded) {//每300ms更新一次进度
                        oldTime = currentTime;
                        final long finalFileSizeDownloaded = fileSizeDownloaded;
                        callbackOnMainThread(new Runnable() {
                            @Override
                            public void run() {

                                    updateProgress(info,finalFileSizeDownloaded,fileSize);
                                info.listener.onProgressChange(finalFileSizeDownloaded,fileSize);

                                if(finalFileSizeDownloaded == fileSize){
                                    //文件校验
                                    if(info.isVerify){
                                        String str = "";
                                        if(info.verfyByMd5OrShar1){//md5
                                            str = fileToMD5(info.filePath);
                                        }else {//sha1
                                            str = fileToSHA1(info.filePath);
                                        }

                                        Tool.dismiss(info.loadingDialog);
                                        if(TextUtils.isEmpty(str)){//md算法失败
                                            info.listener.onError("文件下载失败:校验失败");
                                            return;
                                        }
                                        MyLog.e("real md:"+str+" --- expect md:"+info.verifyStr);
                                        if(str.equalsIgnoreCase(info.verifyStr)){//校验通过
                                            info.listener.onSuccess(info.filePath,info.filePath,info.isFromCache);
                                            handleMedia(info);
                                        }else {
                                            info.listener.onError("文件下载失败:校验不一致");
                                        }
                                    }else {
                                        Tool.dismiss(info.loadingDialog);
                                        info.listener.onSuccess(info.filePath,info.filePath,info.isFromCache);
                                        handleMedia(info);
                                    }




                                }
                            }
                        });

                    }


                }

                outputStream.flush();
                return true;
            } catch (final IOException e) {
                e.printStackTrace();
                //这里如果返回socket closed,可能是取消下载,也可能是网络断了,怎么判断?还是用call来判断
                Tool.callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if(call.isCanceled()){
                            info.listener.onCancel();
                        }else {
                            info.listener.onError(e.getMessage());
                        }

                    }
                });

                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        /*} catch (IOException e) {
            e.printStackTrace();
            return false;
        }*/
    }

    private static void handleMedia(ConfigInfo configInfo) {
        if(configInfo.isNotifyMediaCenter){
            FileUtils.refreshMediaCenter(HttpUtil.context,configInfo.filePath);
        }else {
            if(configInfo.isHideFolder){
                FileUtils.hideFile(new File(configInfo.filePath));
            }
        }

        if(configInfo.isOpenAfterSuccess){
            FileUtils.openFile(HttpUtil.context,new File(configInfo.filePath));
        }

    }



    public static void  callbackOnMainThread(Runnable runnable){
        HttpUtil.getMainHandler().post(runnable);
    }

    public static boolean isJsonEmpty(String data){
        if (data== null || "".equals(data) || "[]".equals(data)
                || "{}".equals(data) || "null".equals(data)) {
            return true;
        }
        return false;
    }

   /* public static void addToken(Map map) {
        if (map != null){
            map.put(NetDefaultConfig.TOKEN, NetDefaultConfig.getToken());//每一个请求都传递sessionid
        }else {
            map = new HashMap();
            map.put(NetDefaultConfig.TOKEN, NetDefaultConfig.getToken());//每一个请求都传递sessionid
        }

    }*/

    public static String generateUrlOfGET(ConfigInfo info){
        StringBuilder stringBuilder= new StringBuilder();
        if((!info.url.startsWith("http")) && (!info.url.startsWith("https"))){
            stringBuilder.append(GlobalConfig.get().getBaseUrl());
        }
        stringBuilder.append(info.url);


        String parms = Tool.getKeyValueStr(info.params);
        if(TextUtils.isNotEmpty(parms) || TextUtils.isNotEmpty(info.paramsStr)){
            if(!info.url.contains("?")){
                stringBuilder.append("?");
            }else if(!info.url.endsWith("&")){
                stringBuilder.append("&");
            }
            if(TextUtils.isNotEmpty(parms)){
                stringBuilder.append(parms);
            }

            if(TextUtils.isNotEmpty(info.paramsStr)){
                stringBuilder.append(info.paramsStr);
                if(!info.paramsStr.endsWith("&")){
                    stringBuilder.append("&");
                }
            }

        }

        return stringBuilder.toString();
    }

    public static String getKeyValueStr(Map<String,String> params) {
        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String,String> param   : params.entrySet()){
            stringBuilder.append(param.getKey())
                    .append("=")
                    .append(param.getValue())
                    .append("&");
        }
        return stringBuilder.toString();
    }


    public static void handleError(Throwable t,ConfigInfo configInfo){
        if(t != null){
            t.printStackTrace();
        }
        dismiss(configInfo.loadingDialog);
        String str = t.toString();
        if(str.contains("timeout")){
            configInfo.listener.onTimeout();
        }else {
            configInfo.listener.onError(str);
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) HttpUtil.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null && connectivity.getActiveNetworkInfo() != null) {

            boolean isAvialable = connectivity.getActiveNetworkInfo().isAvailable();
            /*if(isAvialable){

                if(lastPingTime==0){
                    lastPingStatus =  ping();
                    lastPingTime = System.currentTimeMillis();
                    isAvialable &= lastPingStatus;
                }else {
                    if(System.currentTimeMillis() -lastPingTime >60000){//请求间隔超过1min,重新ping,探测网络状态
                        lastPingStatus =  ping();
                        lastPingTime = System.currentTimeMillis();
                        isAvialable &= lastPingStatus;
                    }else {//没有超过1min,就沿用之前的一个状态
                        isAvialable &= lastPingStatus;
                    }
                }
            }*/
            return isAvialable;
        }else {
            return false;
        }
    }

    /**
     *
     * @return 返回值: 2G,3G,4G,WIFI,""
     */
    public static String getNetworkType()
    {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) HttpUtil.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.e("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }


    private static long lastPingTime =0;
    private static boolean lastPingStatus = false;

    /** @author sichard
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * ping 是网络请求,不能在主线程,所以这里没有意义
            * @return
            */
    public static final boolean ping() {

        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }


    public static String appendUrl(String urlTail,boolean isToAppend) {
        String url ;
        if (!isToAppend || urlTail.contains("http:")|| urlTail.contains("https:")){
            url =  urlTail;
        }else {
            url = GlobalConfig.get().getBaseUrl()+  urlTail;
        }

        return url;
    }


    public static String getMD(String str,String algorithm) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    public static String getMD(File file,String algorithm){
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance(algorithm);
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }*/

    /**
     * Get the md5 value of the filepath specified file
     * @param filePath The filepath of the file
     * @return The md5 value
     */
    public static String fileToMD5(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath); // Create an FileInputStream instance according to the filepath
            byte[] buffer = new byte[1024]; // The buffer to read the file
            MessageDigest digest = MessageDigest.getInstance("MD5"); // Get a MD5 instance
            int numRead = 0; // Record how many bytes have been read
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead); // Update the digest
            }
            byte [] md5Bytes = digest.digest(); // Complete the hash computing
            return convertHashToString(md5Bytes); // Call the function to convert to hex digits
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // Close the InputStream
                } catch (Exception e) { }
            }
        }
    }

    /**
     * Get the sha1 value of the filepath specified file
     * @param filePath The filepath of the file
     * @return The sha1 value
     */
    public static String fileToSHA1(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath); // Create an FileInputStream instance according to the filepath
            byte[] buffer = new byte[1024]; // The buffer to read the file
            MessageDigest digest = MessageDigest.getInstance("SHA-1"); // Get a SHA-1 instance
            int numRead = 0; // Record how many bytes have been read
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead); // Update the digest
            }
            byte [] sha1Bytes = digest.digest(); // Complete the hash computing
            return convertHashToString(sha1Bytes); // Call the function to convert to hex digits
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // Close the InputStream
                } catch (Exception e) { }
            }
        }
    }

    /**
     * Convert the hash bytes to hex digits string
     * @param hashBytes
     * @return The converted hex digits string
     */
    private static String convertHashToString(byte[] hashBytes) {
        String returnVal = "";
        for (int i = 0; i < hashBytes.length; i++) {
            returnVal += Integer.toString(( hashBytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toLowerCase();
    }




    public static String getCacheKey(ConfigInfo configInfo){
        String url = configInfo.url;
        Map<String,String> map = configInfo.params;
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append(url);
        int size = map.size();
        Set<Map.Entry<String,String>> set = map.entrySet();
        if (size>0){
            for (Map.Entry<String,String> entry: set){
                stringBuilder.append(entry.getKey()).append(entry.getValue());
            }

        }
        String str = stringBuilder.toString();
        return getMD(str,"MD5");

    }





    /**
     * 没必要加callbackonUithread,因为都是自己内部用,能确保都在主线程调用
     * @param dialog
     */
    public static void dismiss(Dialog dialog) {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }







    private static void cacheResponse(final String string, final ConfigInfo configInfo) {
        if (configInfo.shouldCacheResponse && !configInfo.isFromCache && configInfo.cacheMaxAge >0){

            HttpUtil.getClient().getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ACache.get(HttpUtil.context).put(getCacheKey(configInfo),string, (int) configInfo.cacheMaxAge);
                    MyLog.d("key is "+getCacheKey(configInfo)+ "---caching resonse:\n"+string);
                }
            });
        }else {
            configInfo.isFromCacheSuccess = true;
            if(configInfo.cacheMode== CacheStrategy.IF_NONE_CACHE_REQUEST){
                dismiss(configInfo.loadingDialog);
            }
        }
    }




    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file){
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    public static String getMimeType(String fileUrl){


        String suffix = getSuffix(new File(fileUrl));
        if (suffix == null) {
            return "file";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file";
    }



    public  static  <E> void parseStandJsonStr(String string, final ConfigInfo<E> configInfo, boolean isFromCache)  {
        if (isJsonEmpty(string)){//先看是否为空

            callbackOnMainThread(new Runnable() {
                @Override
                public void run() {
                    configInfo.listener.onEmpty();
                }
            });

        }else {

            // final BaseNetBean<E> bean = MyJson.parseObject(string,BaseNetBean.class);//如何解析内部的字段?
          /*  Gson gson = new Gson();z这样也不行
            Type objectType = new TypeToken<BaseNetBean<E>>() {}.getType();
            final BaseNetBean<E> bean = gson.fromJson(string,objectType);*/



            JSONObject object = null;
            try {
                object = new JSONObject(string);
            } catch (com.alibaba.fastjson.JSONException e) {
                e.printStackTrace();

                callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        configInfo.listener.onError("json 格式异常");
                    }
                });
                return;
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            String key_data = TextUtils.isEmpty(configInfo.key_data) ? GlobalConfig.get().getStandardJsonKeyData() : configInfo.key_data;
            String key_code = TextUtils.isEmpty(configInfo.key_code) ? GlobalConfig.get().getStandardJsonKeyCode() : configInfo.key_code;
            String key_msg = TextUtils.isEmpty(configInfo.key_msg) ? GlobalConfig.get().getStandardJsonKeyMsg() : configInfo.key_msg;

            final String dataStr = object.optString(key_data);
            final int code = object.optInt(key_code);
            final String msg = object.optString(key_msg);

            final String finalString1 = string;

            parseStandardJsonObj(finalString1,dataStr,code,msg,configInfo,isFromCache);
            //todo 将时间解析放到后面去

        }
    }

    /**
     * 解析标准json的方法

     * @param configInfo

     * @param <E>
     */
    private static <E> void parseStandardJsonObj(final String response, final String data, final int code,

                                                 final String msg, final ConfigInfo<E> configInfo, final boolean isFromCache){

        int codeSuccess = configInfo.isCustomCodeSet ? configInfo.code_success : GlobalConfig.get().getCodeSuccess();
        int codeUnFound = configInfo.isCustomCodeSet ? configInfo.code_unFound :  GlobalConfig.get().getCodeUnfound();
        int codeUnlogin = configInfo.isCustomCodeSet ? configInfo.code_unlogin :  GlobalConfig.get().getCodeUnlogin();

        if (code == codeSuccess){
            if (isJsonEmpty(data)){
                callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if(configInfo.isResponseJsonArray){
                            configInfo.listener.onEmpty();
                        }else {
                            if(configInfo.isTreatEmptyDataAsSuccess){
                                configInfo.listener.onSuccess(null,TextUtils.isEmpty(msg)? "请求成功!" :msg,isFromCache);
                            }else {
                                configInfo.listener.onError("数据为空");
                            }
                        }
                    }
                });

            }else {
                try{
                    if (data.startsWith("{")){
                        final E bean =  MyJson.parseObject(data,configInfo.clazz);
                        callbackOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                configInfo.listener.onSuccessObj(bean ,response,data,code,msg,isFromCache);
                            }
                        });


                        cacheResponse(response, configInfo);
                    }else if (data.startsWith("[")){
                        final List<E> beans =  MyJson.parseArray(data,configInfo.clazz);
                        callbackOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                configInfo.listener.onSuccessArr(beans,response,data,code,msg,isFromCache);
                            }
                        });



                        cacheResponse(response, configInfo);
                    }else {//如果data的值是一个字符串,而不是标准json,那么直接返回
                        if (String.class.equals(configInfo.clazz) ){//此时,E也是String类型.如果有误,会抛出到下面catch里
                            callbackOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    configInfo.listener.onSuccess((E) data,data,isFromCache);
                                }
                            });



                        }else {
                            callbackOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    configInfo.listener.onError("不是标准的json数据");
                                }
                            });

                        }
                    }
                }catch (final Exception e){
                    e.printStackTrace();
                    callbackOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            configInfo.listener.onError(e.toString());
                        }
                    });

                    return;
                }
            }
        }else if (code == codeUnFound){
            callbackOnMainThread(new Runnable() {
                @Override
                public void run() {
                    configInfo.listener.onUnFound();
                }
            });

        }else if (code == codeUnlogin){
            LoginManager loginManager = GlobalConfig.get().getLoginManager();
            if(loginManager==null){
                callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        configInfo.listener.onUnlogin();
                    }
                });

                return;
            }
            loginManager.autoLogin(new MyNetListener() {
                @Override
                public void onSuccess(Object response, String resonseStr, boolean isFromCache) {
                    configInfo.start();
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                     configInfo.listener.onUnlogin();
                }
            });
        }else {
            callbackOnMainThread(new Runnable() {
                @Override
                public void run() {
                    configInfo.listener.onCodeError(msg,"",code);
                }
            });

        }
    }

    public static  void parseStringByType(final String string, final ConfigInfo configInfo,final boolean isFromCache) {
        switch (configInfo.type){
            case ConfigInfo.TYPE_STRING:
                //缓存
                cacheResponse(string, configInfo);

                //处理结果
                callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        configInfo.listener.onSuccess(string, string,isFromCache);
                    }
                });

                break;
            case ConfigInfo.TYPE_JSON:
                parseCommonJson(string,configInfo,isFromCache);
                break;
            case ConfigInfo.TYPE_JSON_FORMATTED:
                parseStandJsonStr(string, configInfo,isFromCache);
                break;
        }
    }

    public static void showDialog(final Dialog dialog){
        callbackOnMainThread(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null && !dialog.isShowing()){
                    try {
                        dialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private static <E> void parseCommonJson(final String string, final ConfigInfo<E> configInfo, final boolean isFromCache) {
        if (isJsonEmpty(string)){
            callbackOnMainThread(new Runnable() {
                @Override
                public void run() {
                    configInfo.listener.onEmpty();
                }
            });

        }else {
            try{
                if (string.startsWith("{")){
                    final E bean =  MyJson.parseObject(string,configInfo.clazz);
                    callbackOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            configInfo.listener.onSuccessObj(bean ,string,string,0,"",isFromCache);
                        }
                    });

                    cacheResponse(string, configInfo);
                }else if (string.startsWith("[")){
                    final List<E> beans =  MyJson.parseArray(string,configInfo.clazz);

                    callbackOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if(beans!=null && beans.size()>0){
                                configInfo.listener.onSuccessArr(beans,string,string,0,"",isFromCache);
                                cacheResponse(string, configInfo);
                            }else {
                                configInfo.listener.onEmpty();
                            }

                        }
                    });


                }else {
                    if(string.startsWith("\"")&& string.endsWith("\"")){
                        String str = string.substring(1,string.length()-1);
                        parseCommonJson(str,configInfo,isFromCache);
                    }else {
                        callbackOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                configInfo.listener.onError("不是标准json格式");
                            }
                        });
                    }
                }
            }catch (final Exception e){
                e.printStackTrace();
                callbackOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        configInfo.listener.onError(e.toString());
                    }
                });
            }
        }
    }


}
