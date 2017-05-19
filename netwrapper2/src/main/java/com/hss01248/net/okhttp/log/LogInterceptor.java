package com.hss01248.net.okhttp.log;


import com.hss01248.net.util.TextUtils;
import com.hss01248.net.wrapper.MyLog;

import java.io.IOException;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class LogInterceptor implements Interceptor {

    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        MyLog.i("------------------------------------------request-----------------------------------------");
        MyLog.i( request.method()+"  "+request.url() );
        MyLog.i(  request.headers().toString()+"\n");
        //MyLog.i( request.body());

        RequestBody body = request.body();
        if(body instanceof FormBody){
            FormBody formBody = (FormBody) body;
            int size = formBody.size();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                stringBuilder.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
            }
            MyLog.i(stringBuilder.toString());

        }else if(body instanceof MultipartBody){
            MultipartBody body1 = (MultipartBody) body;
            MyLog.i("文件上传");
            //body1.
        }else if (body instanceof RequestBody){//json形式传输

            MediaType mediaType = body.contentType();
            if(mediaType!=null) {
                String type0 = mediaType.toString();
                String type = mediaType.type();
                if ("text".equals(type)) {


                } else if (TextUtils.isNotEmpty(type0) && type0.contains("json")){

                }



            }

        }



        long t1 = System.nanoTime();
        okhttp3.Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        MyLog.i("------------------------------------------response-----------------------------------------");
        MyLog.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms code:%s%n%s ",
                response.request().url(), (t2 - t1) / 1e6d,response.code(), response.headers()));

        MediaType mediaType = response.body().contentType();
        if(mediaType!=null){
            String type0 = mediaType.toString();
            String type = mediaType.type();
            if(response.body().contentLength()<0){//todo 需要排除gzip的情况
                return response;
            }

            if("text".equals(type)){

                String content = response.body().string();
                MyLog.i(content);
                response = response.newBuilder()
                        .body(okhttp3.ResponseBody.create(mediaType, content))
                        .build();

                //Scanner scanner = new Scanner(response.body().byteStream(), "UTF-8");
               /* String text = response.body().source().readString(Charset.forName("UTF-8"));//scanner.useDelimiter(" ").next();
                response = response.newBuilder().body(new RealResponseBody(response.headers(),response.body().source())).build();

                MyLog.i(text);*/

            }else if(TextUtils.isNotEmpty(type0) && type0.contains("json")){
                String content = response.body().string();
                MyLog.i(content);
                response = response.newBuilder()
                        .body(okhttp3.ResponseBody.create(mediaType, content))
                        .build();
               // MyLog.i(response.body().string());
                //Scanner scanner = new Scanner(response.body().byteStream(), "UTF-8");
               /* String text = response.body().source().readString(Charset.forName("UTF-8"));
                response = response.newBuilder().body(new RealResponseBody(response.headers(),response.body().source())).build();
                MyLog.i(text);*/
            }
        }


        //okhttp3.MediaType mediaType = response.body().contentType();


       // String type = response.header("Content-Encoding");

       // MyLog.e("Content-Encoding:"+type);


        //byte[] responseBytes=response.body().bytes();

        //String content = new String(responseBytes,"UTF-8");

        //String content = response.body().string();
        //String content =  response.body().source().readString(Charset.forName("gb2312"));
       // GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(content.getBytes("utf-8")));

        //MyLog.i( "response body:\n" + content);
        return response;
    }
}