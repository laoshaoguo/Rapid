package com.zhiyicx.screenbage;

import android.content.Context;
import android.content.Intent;

import static com.zhiyicx.screenbage.Utils.getLauncherClassName;

/**
 * @author Jliuer
 * @Date 18/07/04 15:12
 * @Email Jliuer@aliyun.com
 * @Description vivo机型的桌面角标设置管理类
 */
public class BadgeNumberManagerVIVO {

    public static void setBadgeNumber(Context context, int number) {
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            String launcherClassName = getLauncherClassName(context);
            intent.putExtra("className", launcherClassName);
            intent.putExtra("notificationNum", number);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
