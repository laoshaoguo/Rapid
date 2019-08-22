package com.zhiyicx.common.utils.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class LogUtils {
    private static final String APPLICATION_TAG = "LogUtils";
    private static final int LOGGER_METHODCOUNT = 8;
    private static final int LOGGER_METHODOFFSET = 2;

    public static void init() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(LOGGER_METHODCOUNT)         // (Optional) How many method line to show. Default 2
                .methodOffset(LOGGER_METHODOFFSET)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(new LogCatStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(APPLICATION_TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return com.zhiyicx.common.BuildConfig.USE_LOG;
            }
        });
    }

    public static void d(String tag, Object object) {
        Logger.t(tag).d(object);
    }

    public static void d(String tag, String message, Object... args) {
        Logger.t(tag).d(message, args);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(String tag, String json) {
        Logger.t(tag).json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static void xml(String tag, String xml) {
        Logger.t(tag).xml(xml);
    }

    public static class LogCatStrategy implements LogStrategy {

        private int last;

        private String randomKey() {
            int random = (int) (10 * Math.random());
            if (random == last) {
                random = (random + 1) % 10;
            }
            last = random;
            return String.valueOf(random);
        }

        private Handler handler;
        private long lastTime = SystemClock.uptimeMillis();
        private long offset = 1;

        public LogCatStrategy() {
            HandlerThread thread = new HandlerThread("thread_print");
            thread.start();
            handler = new Handler(thread.getLooper());
        }

        @Override
        public void log(final int priority, final String tag, @NonNull final String message) {

            lastTime += offset;
            if (lastTime < SystemClock.uptimeMillis()) {
                lastTime = SystemClock.uptimeMillis() + offset;
            }
            final long tmp = lastTime;
            handler.postAtTime(new Runnable() {
                @Override
                public void run() {
                    Log.println(priority, randomKey() + tag, message);
                }
            }, tmp);
        }
    }
}
