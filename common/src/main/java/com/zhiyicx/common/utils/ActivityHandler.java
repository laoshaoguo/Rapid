package com.zhiyicx.common.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @Describe Activity 管理类
 * @Author Jungle68
 * @Date 2017/1/18
 * @Contact master.jungle68@gmail.com
 */

public class ActivityHandler {

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    private static Stack<Activity> activityStack = new Stack<>();


    private volatile static ActivityHandler instance;

    private ActivityHandler() {

    }

    /**
     * 单一实例
     */
    public static ActivityHandler getInstance() {
        if (instance == null) {
            synchronized (ActivityHandler.class) {
                if (instance == null) {
                    instance = new ActivityHandler();
                }
            }
        }
        return instance;
    }

    /**
     * 添加 Activity 到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前 Activity （堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 通过 class 获取Activity
     *
     * @param cls
     * @return
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void removeActivity() {
        Activity activity = activityStack.lastElement();
        removeActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }
    /**
     * 结束指定的Activity
     */
    public void removeActivityNotFinish(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }
    /**
     * 结束指定类名的 Activity
     */
    public void removeActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                removeActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束指定类名activity ，如果有多个实例，全部结束
     * @param cls
     */
    public void removeAllSameActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
//                removeActivity(activity);
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void removeCurrentTopActivity() {
        Activity activity = activityStack.lastElement();
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i) && activityStack.get(i) != activity) {
                activityStack.get(i).finish();
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束非最后一个 Activity
     */
    public void finishAllActivityEcepteCurrent() {
        if(activityStack.isEmpty()){
            return;
        }
        Activity activity = activityStack.lastElement();
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i) && activityStack.get(i) != activity) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     * 这里关闭的是所有的 Activity，没有关闭 Activity 之外的其他组件;
     * android.os.Process.killProcess(android.os.Process.myPid())
     * 杀死进程关闭了整个应用的所有资源，有时候是不合理的，通常是用
     * 堆栈管理 Activity;System.exit(0) 杀死了整个进程，这时候活动所占的
     * 资源也会被释放,它会执行所有通过 Runtime.addShutdownHook 注册的 shutdown hooks.
     * 它能有效的释放 JVM 之外的资源,执行清除任务，运行相关的 finalizer 方法终结对象，
     * 而 finish 只是退出了 Activity。
     */
    public void AppExitWithSleep() {
        // app主线程等待3秒，让用户处理好崩溃异常后，杀死进程
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AppExit();
    }

    public void AppExit() {
        try {
            finishAllActivity();
            //DalvikVM的本地方法
            // 杀死该应用进程
//            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}


