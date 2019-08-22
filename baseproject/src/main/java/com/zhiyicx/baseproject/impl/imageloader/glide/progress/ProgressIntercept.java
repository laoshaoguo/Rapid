package com.zhiyicx.baseproject.impl.imageloader.glide.progress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class ProgressIntercept implements Interceptor {
    private ProgressListener mProgressListener;

    public ProgressIntercept(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        // 拦截器替换response Body对象
        return response.newBuilder().body(new ProgressResponceBody(response.body(),mProgressListener)).build();
    }
}
