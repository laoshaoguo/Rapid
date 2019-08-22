package com.zhiyicx.common.net.intercept;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Describe 公共请求参数，Query Parameters 如 token\veresion 等
 * @Author Jungle68
 * @Date 2016/11/30
 */

public class CommonRequestIntercept implements Interceptor {
    private Map<String, String> mQueryParameterMap;// 公共请求信息，get 形式传递

    public CommonRequestIntercept(Map<String, String> queryParameterMap) {
        this.mQueryParameterMap = queryParameterMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
        if (mQueryParameterMap != null) {
            for (Map.Entry<String, String> entry : mQueryParameterMap.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(urlBuilder.build());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
