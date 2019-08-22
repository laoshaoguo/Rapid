package com.zhiyicx.baseproject.impl.imageloader.glide.progress;

import android.os.Handler;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class ProgressModelLoader implements StreamModelLoader<String> {
    private Handler mHandler;
    private String token;
    public ProgressModelLoader(Handler handler,String token) {
        mHandler = handler;
        this.token=token;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
        return new ProgressDataFetcher(model,mHandler,token);
    }
}
