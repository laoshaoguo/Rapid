package com.zhiyicx.common.utils;

import android.os.Bundle;

/**
 * @Author Jliuer
 * @Date 2018/07/13/11:41
 * @Email Jliuer@aliyun.com
 * @Description 用于 activity 传递数据
 */
public class ParcelableDataUtil {

    public static final String USEUTIL = "useutil";
    private Bundle mBundle;

    private static class SingleInstance {
        static ParcelableDataUtil sParcelableDataUtil = new ParcelableDataUtil();
    }

    private ParcelableDataUtil() {
    }

    public static ParcelableDataUtil getSingleInstance() {
        return SingleInstance.sParcelableDataUtil;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public void clear() {
        mBundle = null;
    }
}
