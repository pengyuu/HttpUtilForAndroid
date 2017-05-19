package com.hss01248.net.okhttp.progress;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private String url;

    public ProgressResponseBody(ResponseBody responseBody, String url) {
        this.responseBody = responseBody;
        this.url = url;

    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }


    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    long timePre = 0;
    long timeNow;

    private Source source(final Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                timeNow = System.currentTimeMillis();
                if (timeNow - timePre > 300 || totalBytesRead == responseBody.contentLength()){//至少300ms才更新一次状态
                    timePre = timeNow;
                   /* EventBus.getDefault().post(new ProgressEvent(responseBody.contentLength(),totalBytesRead,
                            totalBytesRead == responseBody.contentLength(),url));*/
                }
                return bytesRead;
            }
        };
    }
}
