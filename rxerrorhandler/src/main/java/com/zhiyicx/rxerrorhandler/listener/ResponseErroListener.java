package com.zhiyicx.rxerrorhandler.listener;

import android.content.Context;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public interface ResponseErroListener {
    /**
     *
     * @param context
     * @param throwable
     */
    void handleResponseError(Context context, Throwable throwable);
}
