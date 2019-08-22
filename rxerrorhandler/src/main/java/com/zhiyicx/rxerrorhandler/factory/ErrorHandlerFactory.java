package com.zhiyicx.rxerrorhandler.factory;

import android.content.Context;

import com.zhiyicx.rxerrorhandler.listener.ResponseErroListener;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public class ErrorHandlerFactory {
    public final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private ResponseErroListener mResponseErroListener;

    public ErrorHandlerFactory(Context mContext, ResponseErroListener mResponseErroListener) {
        this.mResponseErroListener = mResponseErroListener;
        this.mContext = mContext;
    }

    /**
     *  处理错误
     * @param throwable
     */
    public void handleError(Throwable throwable) {
        mResponseErroListener.handleResponseError(mContext,  throwable);
    }
}
