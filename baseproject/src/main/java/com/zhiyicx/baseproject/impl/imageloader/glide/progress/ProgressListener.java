package com.zhiyicx.baseproject.impl.imageloader.glide.progress;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public interface ProgressListener {
    int SEND_LOAD_PROGRESS = 100;

    void progress(long readBytes, long length, boolean done);
}
