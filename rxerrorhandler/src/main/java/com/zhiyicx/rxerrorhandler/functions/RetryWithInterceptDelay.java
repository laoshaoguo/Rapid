package com.zhiyicx.rxerrorhandler.functions;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Describe 操作失败，RX 重试类封装 ,可以固定间隔 和 起始时间 ，如：
 * 开始时间是1  间隔 2  执行 3 次， 效果就是 1  3  5
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public class RetryWithInterceptDelay implements
        Func1<Observable<? extends Throwable>, Observable<?>> {
    public static final int RETRY_MAX_COUNT = 3; // 最大重试次
    public static final int RETRY_INTERVAL_TIME = 5; // 循环间隔时间 单位 s
    private static final int DEFATULT_START_TIME = 1;
    public final String TAG = this.getClass().getSimpleName();
    private final int mMaxRetries;
    private final int mStartDelaySecond;
    private final int mInterceptSecond;
    private int mCurrentRetryCount = 0;

    /**
     * @param maxRetries      the max retry nums
     * @param interceptSecond Retry after { #mInterceptSecond } seconds
     */
    public RetryWithInterceptDelay(int maxRetries, int interceptSecond) {
        this.mMaxRetries = maxRetries;
        this.mStartDelaySecond = DEFATULT_START_TIME;
        this.mInterceptSecond = interceptSecond;
    }

    /**
     * @param maxRetries       the max retry nums
     * @param startDelaySecond the retry start time
     * @param interceptSecond  Retry after { #mInterceptSecond } seconds
     */
    public RetryWithInterceptDelay(int maxRetries, int startDelaySecond, int interceptSecond) {
        this.mMaxRetries = maxRetries;
        this.mStartDelaySecond = startDelaySecond;
        this.mInterceptSecond = interceptSecond;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (++mCurrentRetryCount <= mMaxRetries && extraReTryCondition(throwable)) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            int time = mStartDelaySecond + mInterceptSecond * (mCurrentRetryCount - 1);
                            Log.d(TAG, "get error, it will try after " + time
                                    + " second, retry count " + mCurrentRetryCount);
                            Log.d(TAG, throwable.toString());
                            return Observable.timer(time,
                                    TimeUnit.SECONDS);
                        }
                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }

    /**
     * 需要进行 retry 的额外条件
     * @param throwable
     * @return
     */
    protected boolean extraReTryCondition(Throwable throwable) {
        return true;
    }
}