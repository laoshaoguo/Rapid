package com.zhiyicx.screenbage;

import android.content.Context;

/**
 * @Author Jliuer
 * @Date 2018/07/04/17:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class Utils {
    public static String getLauncherClassName(Context context) throws NullPointerException{
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
    }
}
