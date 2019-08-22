package com.zhiyicx.common.net.intercept;

import android.support.annotation.NonNull;

import com.zhiyicx.common.net.listener.RequestInterceptListener;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public class RequestIntercept implements Interceptor {
    private static final String TAG = "RequestIntercept";
    private RequestInterceptListener mListener;
    private static final boolean USE_ERROR_LOG = false;

    public RequestIntercept(RequestInterceptListener listener) {
        this.mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (mListener != null)//在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不做操作则返回参数中的request
        {
            request = mListener.onHttpRequestBefore(chain, request);
        }

        Buffer requestbuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestbuffer);
        } else {
            LogUtils.d(TAG, "request.body() == null");
        }
        //打印url信息
        String logUrl = request.url() + "";
        String method = request.method();
        logUrl = URLDecoder.decode(logUrl, "utf-8");
        try {
            LogUtils.d(TAG, "Sending " + method + " Request %s on %n formdata --->  %s%n Connection ---> %s%n Headers ---> %s", logUrl
                    , request.body() != null ? parseParams(request.body(), requestbuffer) : "null"
                    , chain.connection()
                    , request.headers());
        } catch (Exception e) {
            e.printStackTrace();
        }

        long t1 = System.nanoTime();
        Response originalResponse = chain.proceed(request);
        long t2 = System.nanoTime();
        //打印响应时间
        LogUtils.d(TAG, "Received response code %d in %.1fms%n%s", originalResponse.code(), (t2 - t1) / 1e6d, originalResponse.headers());

        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        //获取content的压缩类型
        String encoding = originalResponse
                .headers()
                .get("Content-Encoding");

        Buffer clone = buffer.clone();
        String bodyString = ConvertUtils.praseBodyString(responseBody, encoding, clone);
        // 打印返回的json结果
        LogUtils.json(TAG, bodyString);
        if (USE_ERROR_LOG) {
            // 服務器出錯時候打印
            try {
                LogUtils.d(TAG, bodyString);
            } catch (Exception ignored) {
            }
        }

        //这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
        if (mListener != null) {
            return mListener.onHttpResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    @NonNull
    public static String parseParams(RequestBody body, Buffer requestbuffer) throws UnsupportedEncodingException {
        boolean canPrint = false;
        if (body.contentType() != null) {
            boolean isMultipart = body.contentType().toString().contains("multipart");
            boolean isImage = body.contentType().toString().contains("image/jpeg") || body.contentType().toString().contains("image/png");
            boolean isVideo = body.contentType().toString().contains("video/mp4");
            canPrint = !isImage && !isMultipart && !isVideo;
        }
        if (canPrint && requestbuffer.size() < 1000) {
            String data = requestbuffer.readUtf8();
            try {
                data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                data = data.replaceAll("\\+", "%2B");
                data = URLDecoder.decode(data, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }
        return "can not print";
    }


}
