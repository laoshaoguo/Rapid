package com.zhiyicx.baseproject.crashhandler;

import android.os.Handler;
import android.os.Looper;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/20
 * @Contact master.jungle68@gmail.com
 */
public class CrashClient {

    private CrashListener mCrashListener;

    private static CrashClient mInstance;

    private CrashClient() {

    }

    private static CrashClient getInstance() {
        if (mInstance == null) {
            synchronized (CrashClient.class) {
                if (mInstance == null) {
                    mInstance = new CrashClient();
                }
            }
        }

        return mInstance;
    }

    public static void init(CrashListener crashListener) {
        getInstance().setCrashListener(crashListener);
    }

    private void setCrashListener(CrashListener crashListener) {

        mCrashListener = crashListener;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.loop();
                } catch (Throwable e) {
                    if (mCrashListener != null) {//捕获异常处理
                        mCrashListener.uncaughtException(Looper.getMainLooper().getThread(), e);
                    }
                }
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (mCrashListener != null) {//捕获异常处理
                    mCrashListener.uncaughtException(t, e);
                }
            }
        });

    }

    public interface CrashListener {
        void uncaughtException(Thread t, Throwable e);
    }
}