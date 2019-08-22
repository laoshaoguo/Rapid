package com.zhiyicx.screenbage;

import android.content.Context;
import android.content.Intent;

import static com.zhiyicx.screenbage.Utils.getLauncherClassName;

/**
 * @Author Jliuer
 * @Date 2018/07/04/16:50
 * @Email Jliuer@aliyun.com
 * @Description 三星
 */
public class BadgeNumberManagerSumsung {

    public static void setBadgeNumber(Context context, int count) {
        try {
            String launcherClassName = getLauncherClassName(context);
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        } catch (Exception ignore) {
        }

    }
}
