package com.zhiyicx.common.dagger.module;

import android.app.Application;
import android.text.TextUtils;

import com.zhiyicx.common.net.intercept.RequestIntercept;
import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.rxerrorhandler.RxErrorHandler;
import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLSocketFactory;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

@Module
public class HttpClientModule {
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;// 缓存文件最大值为 10Mb
    private static final String DEFAULT_BASEURL = "https://api.github.com/";
    private static final int CONNECTED_TOME_OUT = 15;
    private static final int WRITE_TOME_OUT = 15;
    private static final int READE_TOME_OUT = 15;

    private HttpUrl mApiUrl;
    private RequestInterceptListener mHandler;
    private Set<Interceptor> mInterceptorSet;
    private ResponseErroListener mErroListener;
    private SSLSocketFactory mSSLSocketFactory;// 配置安全证书
    private Authenticator mAuthenticator;

    /**
     * 设置 baseurl
     *
     * @param buidler
     */
    protected HttpClientModule(Buidler buidler) {
        this.mApiUrl = buidler.apiUrl;
        this.mHandler = buidler.handler;
        this.mInterceptorSet = buidler.mInterceptorSet;
        this.mErroListener = buidler.responseErroListener;
        this.mSSLSocketFactory = buidler.mSSLSocketFactory;
        this.mAuthenticator = buidler.mAuthenticator;
    }

    public static Buidler buidler() {
        return new Buidler();
    }

    /**
     * 提供OkhttpClient
     *
     * @param cache     缓存
     * @param intercept 拦截器
     * @return
     */
    @Singleton
    @Provides
    public OkHttpClient provideClient(Cache cache, Interceptor intercept) {
        final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        return configureClient(okHttpClient, cache, intercept);
    }

    /**
     * 提供 retrofit
     *
     * @param client
     * @param httpUrl
     * @return
     */
    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient client, HttpUrl httpUrl) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        return configureRetrofit(builder, client, httpUrl);
    }

    /**
     * 提供 baseUrl
     *
     * @return
     */
    @Singleton
    @Provides
    public HttpUrl provideBaseUrl() {
        return mApiUrl;
    }

    /**
     * 提供文件缓存器，设置缓存路径和大小
     *
     * @param cacheFile
     * @return
     */
    @Singleton
    @Provides
    public Cache provideCache(File cacheFile) {
        return new Cache(cacheFile, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }

    /**
     * 提供缓存地址
     */
    @Singleton
    @Provides
    public File provideCacheFile(Application application) {
        return FileUtils.getCacheFile(application, false);
    }

    /**
     * 打印请求信息的拦截器
     *
     * @return
     */
    @Singleton
    @Provides
    public Interceptor provideIntercept() {
        return new RequestIntercept(mHandler);
    }

    /**
     * 提供 RXCache 客户端
     *
     * @param cacheDir 缓存路径
     * @return
     */
    @Singleton
    @Provides
    public RxCache provideRxCache(File cacheDir) {
        return new RxCache
                .Builder()
                .persistence(cacheDir, new GsonSpeaker());
    }


    /**
     * 提供处理 Rxjava 错误的管理器
     *
     * @return
     */
    @Singleton
    @Provides
    public RxErrorHandler proRxErrorHandler(Application application) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErroListener(mErroListener)
                .build();
    }

    /**
     * 配置retrofit
     *
     * @param builder
     * @param client
     * @param httpUrl
     * @return
     */
    private Retrofit configureRetrofit(Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl) {

        return builder
                .baseUrl(httpUrl)// 域名
                .client(client)// 设置 okhttp
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用 rxjava
                .addConverterFactory(ScalarsConverterFactory.create())// 使用Scalars
                .addConverterFactory(GsonConverterFactory.create())// 使用 Gson
                .build();
    }

    /**
     * 配置 okhttpclient
     *
     * @param okHttpClient
     * @return
     */
    private OkHttpClient configureClient(OkHttpClient.Builder okHttpClient, Cache cache, Interceptor intercept) {

        OkHttpClient.Builder builder = okHttpClient
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECTED_TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(READE_TOME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TOME_OUT, TimeUnit.SECONDS)
                .cache(cache)//设置缓存
                .addNetworkInterceptor(intercept);
        if (mSSLSocketFactory != null) {
            builder.sslSocketFactory(mSSLSocketFactory);
        }
        // 如果外部提供了 interceptor 则遍历添加
        if (mInterceptorSet != null) {
            for (Interceptor interceptor : mInterceptorSet) {
                builder.addInterceptor(interceptor);
            }
        }
        // 401 认证失败处理
        if (mAuthenticator != null) {
            builder.authenticator(mAuthenticator);
        }
        return builder
                .build();
    }


    public static final class Buidler {
        private HttpUrl apiUrl = HttpUrl.parse(DEFAULT_BASEURL);
        private RequestInterceptListener handler;
        private Set<Interceptor> mInterceptorSet;
        private ResponseErroListener responseErroListener;
        private SSLSocketFactory mSSLSocketFactory;
        private Authenticator mAuthenticator;

        private Buidler() {
        }

        /**
         * @param baseurl 基础 url
         * @return
         */
        public Buidler baseurl(String baseurl) {
            if (TextUtils.isEmpty(baseurl)) {
                throw new IllegalArgumentException("baseurl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseurl);
            return this;
        }

        /**
         * 用来处理http响应结果
         *
         * @param handler
         * @return
         */
        public Buidler globeHttpHandler(RequestInterceptListener handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 用来处理http响应 401 认证失败处理
         *
         * @param authenticator
         * @return
         */
        public Buidler authenticator(Authenticator authenticator) {
            this.mAuthenticator = authenticator;
            return this;
        }

        /**
         * 动态添加任意个 interceptor
         *
         * @param interceptorSet
         * @return
         */
        public Buidler interceptors(Set<Interceptor> interceptorSet) {
            this.mInterceptorSet = interceptorSet;
            return this;
        }

        /**
         * 处理所有 Rxjava 的 onError 逻辑
         *
         * @param listener
         * @return
         */
        public Buidler responseErroListener(ResponseErroListener listener) {
            this.responseErroListener = listener;
            return this;
        }

        /**
         * 获取证书id
         *
         * @return
         */
        public Buidler sslSocketFactory(SSLSocketFactory mSSLSocketFactory) {
            this.mSSLSocketFactory = mSSLSocketFactory;
            return this;

        }


        public HttpClientModule build() {
            if (apiUrl == null) {
                throw new IllegalStateException("baseurl is required");
            }
            return new HttpClientModule(this);
        }


    }

}
