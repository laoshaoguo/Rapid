package com.haohaishengshi.haohaimusic.config;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/16
 * @Contact master.jungle68@gmail.com
 */

public class SharePreferenceTagConfig {
    /**
     * 认证信息
     */
    public static final String SHAREPREFERENCE_TAG_AUTHBEAN = "authBean";
    /**
     * im 配置
     */
    public static final String SHAREPREFERENCE_TAG_IMCONFIG = "imConfig";
    public static final String SHAREPREFERENCE_TAG_MUSIC = "music";
    /**
     * 系统启动配置信息
     */
    public static final String SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS = "system_bootstrappers";
    /**
     * 记录上一次请求成功的时间
     */
    public static final String SHAREPREFERENCE_TAG_LAST_FLUSHMESSAGE_TIME = "last_flushmessage_time";
    /**
     * 记录是否是第一次查看钱包
     */
    public static final String SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET = "is_not_first_look_wallet";
    /**
     * 记录是否是第一次付费查看
     */
    public static final String SHAREPREFERENCE_TAG_IS_NOT_FIRST_PAY_DYNAMIC = "is_not_first_pay_dynamic";
    /**
     * 记录是否是第一次投稿
     */
    public static final String SHAREPREFERENCE_TAG_IS_NOT_FIRST_SEND_INFO = "is_not_first_send_info";

    /**
     * PhotoViewActivity
     * 用于存储 intent 传递数据过大
     */
    public static final String SHAREPREFERENCE_TAG_PHOTO_VIEW_INTNET_CACHE = "photo_view_intnet_cache";


}
