package com.zhiyicx.baseproject.share;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/20
 * @Contact master.jungle68@gmail.com
 */

public interface OnShareCallbackListener {
    void onStart(Share share);

    void onSuccess(Share share);

    void onError(Share share, Throwable throwable);

    void onCancel(Share share);

}
