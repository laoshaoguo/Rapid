package com.zhiyicx.common.net.listener;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @Describe 网络拦截回掉
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public interface RequestInterceptListener {
    /**
     * 网络请求回掉后调用
     *
     * @param httpResult
     * @param chain
     * @param response
     * @return
     */
    Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response response);

    /**
     * 网络请求发出去之前调用
     *
     *
     * @param chain
     * @param request
     * @return
     */
    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);
}
