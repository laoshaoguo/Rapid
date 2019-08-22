package com.zhiyicx.baseproject.impl.imageloader.glide.progress;

import android.os.Handler;
import android.os.Message;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class ProgressDataFetcher implements DataFetcher<InputStream> {
    private String photoUrl;
    private String token;
    private Call progressCall;
    private InputStream resultStream;
    private boolean isCancel;
    private Handler mHandler;

    public ProgressDataFetcher(String photoUrl, Handler handler,String token) {
        this.photoUrl = photoUrl;
        mHandler = handler;
        this.token=token;
    }

    @Override
    public InputStream loadData(Priority priority) {
        // 重写Glide图片加载方法
        Request requst = new Request.Builder().url(photoUrl)
                .header("Authorization", token)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ProgressIntercept(getProgressListener()))
                .build();
        try {
            progressCall = okHttpClient.newCall(requst);
            Response response = progressCall.execute();
            if (isCancel) {
                return null;
            }
            if (!response.isSuccessful()) {
                throw new IOException("unexpected error" + response);
            }
            resultStream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStream;
    }

    @Override
    public void cleanup() {
        // 关闭相应资源
        if (resultStream != null) {
            try {
                resultStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    @Override
    public String getId() {
        return photoUrl;
    }

    @Override
    public void cancel() {
        isCancel = true;
    }

    private ProgressListener getProgressListener() {
        return new ProgressListener() {
            @Override
            public void progress(long readBytes, long length, boolean done) {
                // 通过handler持续发送消息，直到完成
                if (mHandler != null && !done) {
                    Message message = Message.obtain();
                    message.what = SEND_LOAD_PROGRESS;
                    message.arg1 = (int) readBytes;
                    message.arg2 = (int) length;
                    mHandler.sendMessage(message);
                }
            }
        };
    }
}
