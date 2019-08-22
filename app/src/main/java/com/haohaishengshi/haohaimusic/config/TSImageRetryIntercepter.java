package com.haohaishengshi.haohaimusic.config;

import com.zhiyicx.common.utils.log.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author Jliuer
 * @Date 2017/9/21 9:50
 * @Email Jliuer@aliyun.com
 * @Description 
 */
public class TSImageRetryIntercepter implements Interceptor {

    public int maxRetry;//最大重试次数
    private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public TSImageRetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LogUtils.d("retryNum=" + retryNum);
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetry) {
            retryNum++;
            LogUtils.d("retryNum=" + retryNum);
            response = chain.proceed(request);
        }
        return response;
    }
}