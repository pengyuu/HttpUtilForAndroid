package com.hss01248.net.builder;

/**
 * Created by Administrator on 2017/1/16 0016.
 */
public class ApiBuilder implements IBuilder {

    @Override
    public <E> StringRequestBuilder<E> buildStringRequest(String url) {
        return new StringRequestBuilder<>().url(url);
    }

    @Override
    public <E> JsonRequestBuilder<E> buildJsonRequest(String url, Class<E> clazz) {
        JsonRequestBuilder builder = new JsonRequestBuilder();
        builder.url(url)
                .setJsonClazz(clazz);
        return builder;
    }

    @Override
    public <E> StandardJsonRequestBuilder<E> buildStandardJsonRequest(String url, Class<E> clazz) {
        return new StandardJsonRequestBuilder<>().url(url).setJsonClazz(clazz);
    }

    @Override
    public <E> DownloadBuilder<E> buildDownloadRequest(String url) {
        return new DownloadBuilder<>().url(url);
    }

    @Override
    public <E> UploadRequestBuilder<E> buildUpLoadRequest(String url, String fileDesc, String filePath) {
        return new UploadRequestBuilder<>().url(url).addFile(fileDesc,filePath);
    }
}
