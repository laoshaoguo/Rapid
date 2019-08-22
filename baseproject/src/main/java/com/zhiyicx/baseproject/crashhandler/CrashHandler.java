package com.zhiyicx.baseproject.crashhandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe 处理程序异常崩溃的情况
 * @date 2017/1/14 CrashHandler
 * @contact email:450127106@qq.com
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    public static String CRASH_FILE_FOLDER = "/crash/";// crash文件保存路径"/sdcard/crash/";
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    private static CrashHandler INSTANCE = new CrashHandler();

    private CrashHandler() {
        try {
            CRASH_FILE_FOLDER = Environment.getExternalStorageDirectory().getPath() + CRASH_FILE_FOLDER;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Observable.just(throwable)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MobclickAgent.reportError(BaseApplication.getContext(), throwable);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        MobclickAgent.reportError(BaseApplication.getContext(), throwable);
                    }
                });
        // 如果用户没有自己处理这些异常，就让系统自己来
        if (!handleException(throwable) && mUncaughtExceptionHandler != null) {
            mUncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            ActivityHandler.getInstance().AppExitWithSleep();
        }

    }

    public void init() {
        //获取系统默认的UncaughtException处理器
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        LogUtils.e(ex, TAG);
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(BaseApplication.getContext());
        //保存日志文件
        saveCrashInfo2File(ex);
        //使用Toast来显示异常信息,也可以做些其他的
        new Thread() {
            @SuppressLint("MyToastHelper")
            @Override
            public void run() {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Toast.makeText(BaseApplication.getContext(), R.string.app_crash_tip, Toast.LENGTH_SHORT).show();
                LogUtils.e("-----------------2--------------end--");
                Looper.loop();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }.start();
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                // app版本号
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        // 获取系统信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(CRASH_FILE_FOLDER);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(CRASH_FILE_FOLDER + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

}
