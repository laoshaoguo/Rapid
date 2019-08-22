package com.zhiyicx.rxerrorhandler.functions;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Describe 操作失败，RX 重试类封装
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public class RetryWithDelay implements
        Func1<Observable<? extends Throwable>, Observable<?>> {
    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    /**
     * @param maxRetries  the max retry nums
     * @param retryDelaySecond Retry after { retryDelaySecond } seconds
     */
    public RetryWithDelay(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (++retryCount <= maxRetries) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            Log.d(TAG, "get error, it will try after " + retryDelaySecond
                                    + " second, retry count " + retryCount);
                            return Observable.timer(retryDelaySecond,
                                    TimeUnit.SECONDS);
                        }
                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }
}