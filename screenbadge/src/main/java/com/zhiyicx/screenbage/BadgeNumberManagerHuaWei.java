package com.zhiyicx.screenbage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import static com.zhiyicx.screenbage.Utils.getLauncherClassName;

/**
 * @author Jliuer
 * @Date 18/07/04 15:12
 * @Email Jliuer@aliyun.com
 * @Description 华为机型的桌面角标设置管理类
 */
public class BadgeNumberManagerHuaWei {

    /**
     * 设置应用的桌面角标，已在一些华为手机上测试通过,但是无法保证在所有华为手机上都生效
     *
     * @param context context
     * @param number  角标显示的数字
     */
    public static void setBadgeNumber(Context context, int number) {
        try {
            if (number < 0) {
                number = 0;
            }
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            String launcherClassName = getLauncherClassName(context);
            bundle.putString("class", launcherClassName);
            bundle.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
