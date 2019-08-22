package com.zhiyicx.rxerrorhandler;

import com.zhiyicx.rxerrorhandler.factory.ErrorHandlerFactory;

import rx.Subscriber;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public abstract class ErrorHandleSubscriber<T> extends Subscriber<T> {
    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler){
        this.mHandlerFactory = rxErrorHandler.getmHandlerFactory();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }

}

