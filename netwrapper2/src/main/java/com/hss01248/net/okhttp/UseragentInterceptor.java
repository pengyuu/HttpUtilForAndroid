package com.hss01248.net.okhttp;


import com.hss01248.net.config.GlobalConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class UseragentInterceptor implements Interceptor {

    public Response intercept(Chain chain) throws IOException {
       Request request =  chain.request();
      request =   request.newBuilder().addHeader("User-Agent", GlobalConfig.get().getUserAgent()).build();
        return chain.proceed(request);
    }
}
