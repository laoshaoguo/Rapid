package com.zhiyicx.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Describe SharePreference 工具类
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public class SharePreferenceUtils {
    private static SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "sp_name";
    public static final String SP_DOMAIN = "sp_domain";
    /**视频动态暂存标识*/
    public static final String VIDEO_DYNAMIC = "video_dynamic";

    /**
     * 存储重要信息到 sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 返回存在 sharedPreferences 的信息
     *
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key, null);
    }

    /**
     * 存储重要信息到 sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String key, Long value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * 返回存在 sharedPreferences 的信息
     *
     * @param key
     * @return
     */
    public static Long getLong(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        try {
            return mSharedPreferences.getLong(key, 0L);

        } catch (Exception e) {
        }
        return 0L;
    }

    /**
     * 存储重要信息到 sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 返回存在 sharedPreferences 的信息
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key, false);
    }

    /**
     * 返回存在 sharedPreferences 的信息
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 存储重要信息到 sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void setInterger(Context context, String key, int value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 返回存在 sharedPreferences 的信息
     *
     * @param key
     * @return
     */
    public static int getInterger(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, -1);
    }

    /**
     * 清除某个内容
     */
    public static boolean remove(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.edit().remove(key).commit();
    }

    /**
     * 清除 shareprefrence
     */
    public static void clearShareprefrence(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * 将对象储存到 sharepreference
     *
     * @param key
     * @param device
     * @param <T>
     */
    public static <T> boolean saveObject(Context context, String key, T device) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        String oAuth_Base64 = ConvertUtils.object2Base64Str(device);
        if (oAuth_Base64 == null) {
            return false;
        }
        mSharedPreferences.edit().putString(key, oAuth_Base64).commit();
        return true;
    }

    /**
     * 将对象从 shareprerence 中取出来
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObject(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        String productBase64 = mSharedPreferences.getString(key, null);
        return ConvertUtils.base64Str2Object(productBase64);
    }

}
