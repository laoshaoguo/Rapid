package com.zhiyicx.screenbage;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

/**
 * @author Jliuer
 * @Date 18/07/04 15:03
 * @Email Jliuer@aliyun.com
 * @Description 应用桌面角标设置的管理类
 */
public class BadgeNumberManager {

    private static final BadgeNumberManager.AbstractImpl IMPL;

    private Context mContext;


    private void setContext(Context context) {
        mContext = context;
    }

    private BadgeNumberManager() {
    }

    public static class SingleInstance {
        private static BadgeNumberManager sNumberManager = new BadgeNumberManager();

        static BadgeNumberManager newInstance(Context context) {
            sNumberManager.setContext(context);
            return sNumberManager;
        }

    }

    public static BadgeNumberManager from(Context context) {
        return SingleInstance.newInstance(context);
    }


    /**
     * 设置应用角标数字
     *
     * @param notification 小米手机需要，不然可以为 null
     * @param number       数量
     */
    public void setBadgeNumber(Notification notification, int number) {
        if (number < 0) {
            number = 0;
        }
        if (IMPL instanceof ImplXiaoMi && notification != null) {
            IMPL.setXiaomiBadgeNumber(notification, number);
        } else {
            IMPL.setBadgeNumber(mContext, number);
        }
    }

    public interface Impl {
        void setBadgeNumber(Context context, int number);
    }

    public static abstract class AbstractImpl implements Impl {
        void setXiaomiBadgeNumber(Notification notification, int number) {
        }
    }

    static class ImplHuaWei extends AbstractImpl {
        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerHuaWei.setBadgeNumber(context, number);
        }
    }

    static class ImplXiaoMi extends AbstractImpl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            //小米机型的桌面应用角标API跟通知绑定在一起了，所以单独做处理
            //BadgeNumberManagerXiaoMi.setBadgeNumber(context, number);
        }

        @Override
        void setXiaomiBadgeNumber(Notification notification, int number) {
            BadgeNumberManagerXiaoMi.setBadgeNumber(notification, number);
        }
    }

    static class ImplVIVO extends AbstractImpl {
        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerVIVO.setBadgeNumber(context, number);
        }
    }

    static class ImplOPPO extends AbstractImpl {
        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerOPPO.setBadgeNumber(context, number);
        }
    }

    static class ImplSUMSUNG extends AbstractImpl {
        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerSumsung.setBadgeNumber(context, number);
        }
    }

    static class ImplBase extends AbstractImpl {
        @Override
        public void setBadgeNumber(Context context, int number) {
            //do nothing
        }
    }

    static {
        String manufacturer = Build.MANUFACTURER;
        if (MobileBrand.HUAWEI.equalsIgnoreCase(manufacturer)) {
            IMPL = new ImplHuaWei();
        } else if (MobileBrand.XIAOMI.equalsIgnoreCase(manufacturer)) {
            IMPL = new ImplXiaoMi();
        } else if (MobileBrand.VIVO.equalsIgnoreCase(manufacturer)) {
            IMPL = new ImplVIVO();
        } else if (MobileBrand.OPPO.equalsIgnoreCase(manufacturer)) {
            IMPL = new ImplOPPO();
        } else if (MobileBrand.SAMSUNG.equalsIgnoreCase(manufacturer)) {
            IMPL = new ImplSUMSUNG();
        } else {
            IMPL = new ImplBase();
        }
    }
}
