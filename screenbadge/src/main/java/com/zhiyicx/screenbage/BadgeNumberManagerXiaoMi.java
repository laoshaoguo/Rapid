package com.zhiyicx.screenbage;

import android.app.Notification;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Jliuer
 * @Date 18/07/04 15:12
 * @Email Jliuer@aliyun.com
 * @Description 小米机型的桌面角标设置管理类
 */
public class BadgeNumberManagerXiaoMi {

    public static void setBadgeNumber(Notification notification, int number) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
