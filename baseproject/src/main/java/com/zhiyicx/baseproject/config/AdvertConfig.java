package com.zhiyicx.baseproject.config;

/**
 * @Author Jliuer
 * @Date 2017/08/10/15:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AdvertConfig {

    /**
     * 图片广告类型
     */
    public static final String APP_IMAGE_TYPE_ADVERT = "image";

    /**
     * 动态广告类型
     */
    public static final String APP_DYNAMIC_TYPE_ADVERT = "feed:analog";

    /**
     * 资讯广告类型
     */
    public static final String APP_INFO_TYPE_ADVERT = "news:analog";

    /**
     * 启动广告 type  boot
     */
    public static final String APP_BOOT_ADVERT = "boot";


    /*-------------------------动态---------------------------------------------------------------------------*/
    /**
     * 动态顶部广告 type  image
     */
    public static final String APP_DYNAMIC_BANNER_ADVERT = "feed:list:top";

    /**
     * 动态列表广告 type  feed:analog
     */
    public static final String APP_DYNAMIC_LIST_ADVERT = "feed:list:analog";

    /**
     * 动态详情广告 type  image
     */
    public static final String APP_DYNAMIC_DETAILS_ADVERT = "feed:single";

    /*-------------------------资讯---------------------------------------------------------------------------*/
    /**
     * 资讯顶部广告 type  image
     */
    public static final String APP_INFO_BANNER_ADVERT = "news:list:top";

    /**
     * 资讯列表广告 type  news:analog
     */
    public static final String APP_INFO_LIST_ADVERT = "news:list:analog";

    /**
     * 资讯详情广告 type  image
     */
    public static final String APP_INFO_DETAILS_ADVERT = "news:single";


    /*-------------------------圈子---------------------------------------------------------------------------*/
    /**
     * 圈子首页顶部广告 type  image
     */
    public static final String APP_GROUP_TOP_ADVERT = "group:index:top";

    /**
     * 圈子详情广告 type  image
     */
    public static final String APP_GROUP_DETAIL_ADVERT = "group:single";
    /**
     * 积分页广告
     */
    public static final String APP_WALLET_INTEGRATION_ADVERT = "currency";

}
