package com.hss01248.net.builder;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public interface IBuilder {

    // 新api,最简化,其他个性化设置由
    public <E> StringRequestBuilder<E> buildStringRequest(String url);

    public <E> JsonRequestBuilder<E> buildJsonRequest(String url, Class<E> clazz);

    public <E> StandardJsonRequestBuilder<E> buildStandardJsonRequest(String url, Class<E> clazz);

    public<E> DownloadBuilder<E> buildDownloadRequest(String url);

    <E> UploadRequestBuilder<E> buildUpLoadRequest(String url,  String fileDesc,String filePath);
}
