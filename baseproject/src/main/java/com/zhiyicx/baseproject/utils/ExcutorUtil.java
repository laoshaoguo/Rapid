package com.zhiyicx.baseproject.utils;

import android.view.View;

import com.zhiyicx.common.utils.DeviceUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Jliuer
 * @Date 2018/02/07/14:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ExcutorUtil {

    private ExcutorUtil() {
    }

    private static class MySingleCustomExecutor {

        private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
        private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        private static final long KEEP_ALIVE_TIME = 10L;

        private static final ThreadFactory TYM_THREAD_FACTORY = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                //TODO
                return new Thread(r, "TYM_ImageLoader##" + mCount.getAndIncrement());
            }
        };

        public static final Executor TYM_IMAGELOADER_EXECUTOR = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), TYM_THREAD_FACTORY);
    }

    public static Executor getSingleCustomExecutor() {
        return MySingleCustomExecutor.TYM_IMAGELOADER_EXECUTOR;
    }

    public static void startRunInThread(Runnable doSthRunnable) {
        getSingleCustomExecutor().execute(doSthRunnable);
    }

    public static void startRunInNewThread(Runnable doSthRunnable) {
        Executors.newSingleThreadExecutor().execute(doSthRunnable);
    }

}
