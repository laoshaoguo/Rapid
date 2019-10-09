package com.zhiyicx.baseproject.config;

import com.zhiyicx.common.BuildConfig;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */
public class ApiConfig {
    /**
     * 添加平台名称，用于接口
     * 1:pc 2:h5 3:ios 4:android 5:其他
     */
    public static final int ANDROID_PLATFORM = 4;

    /**
     * 接口请求失败后，最大重复请求次数
     */
    public static final int DEFAULT_MAX_RETRY_COUNT = 5;

    public static final String API_VERSION_2 = "v2";
    /**
     * 网络根地址  http://192.168.10.222/
     * 测试服务器：http://192.168.2.222:8080/mockjs/2/test-get-repose-head-normal?
     */

    //public static final String APP_DOMAIN = "http://192.168.2.222:8080/mockjs/2/";// rap 测试服务器

    public static final boolean APP_IS_NEED_SSH_CERTIFICATE = true;// 自定义证书时使用false
    //    public static final String APP_DOMAIN_DEV = "https://dev.zhibocloud.cn/";// 模拟在线正式服务器
    public static final String APP_DOMAIN_TEST = "http://test-plus.zhibocloud.cn/";// 在线测试服务器
    public static final String APP_DOMAIN_FORMAL = "https://tsplus.zhibocloud.cn/";// 正式服务器
    public static final String APP_DOMAIN_FOR_TEARCHER_QIAO = "http://192.168.2.152/";// 乔老师本地服务器
    public static final String APP_DOMAIN_DEV = APP_DOMAIN_FOR_TEARCHER_QIAO;// 模拟在线正式服务器


    public static String APP_DOMAIN = BuildConfig.USE_DOMAIN_SWITCH ? APP_DOMAIN_FORMAL : APP_DOMAIN_FORMAL;

    public static final String URL_ABOUT_US = "api/" + API_VERSION_2 + "/aboutus";// 关于我们网站

    // 图片地址 V2
    public static final String IMAGE_PATH_V2_ORIGIN = "api/" + API_VERSION_2 + "/files/%s";
    public static final String IMAGE_PATH_V2 = IMAGE_PATH_V2_ORIGIN + "?w=%d&h=%d&q=%d";
    public static final String IMAGE_PATH_NQ = IMAGE_PATH_V2_ORIGIN + "?w=%d&h=%d";

    // 头像地址
    public static final String IMAGE_AVATAR_PATH_V2 = APP_DOMAIN + "api/" + API_VERSION_2 + "/users/%s/avatar";

    // 音乐等文件地址 V2
    public static final String FILE_PATH = "api/" + API_VERSION_2 + "/files/%s";
    // 对一条动态或一条动态评论进行评论
    public static final String APP_PATH_DYNAMIC_SEND_COMMENT_V2 = "api/" + API_VERSION_2 + "/feeds/%s/comments";

    public static final String APP_PATH_MUSIC_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/music/%s/comments";
    public static final String APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT = "api/" + API_VERSION_2 + "/music/specials/%s/comments";

    /**
     * 评论问题
     */
    public static final String APP_PATH_SEND_QUESTION_COMMENT_S = "api/" + API_VERSION_2 + "/questions/%s/comments";

    /**
     * 评论答案
     */
    public static final String APP_PATH_COMMENT_QA_ANSWER_FORMAT = "api/" + API_VERSION_2 + "/question-answers/%d/comments";


    /**
     * **************************通用 CommonClient
     **************************/
    public static final String APP_PATH_REFRESH_TOKEN = "api/" + API_VERSION_2 + "/auth/refresh";// 刷新 token
    public static final String APP_PATH_HANDLE_BACKGROUND_TASK = "{path}";// 处理后台任务

    public static final String APP_PATH_SYSTEM_FEEDBACK = "api/" + API_VERSION_2 + "/user/feedback";// 意见反馈
    public static final String APP_PATH_GET_SYSTEM_CONVERSATIONS = "api/" + API_VERSION_2 + "/conversations";// 获取系统会话列表

    ////////////////////////////////////////// 以下是通用 V2 接口
    public static final String APP_PATH_STORAGE_HASH = "api/" + API_VERSION_2 + "/files/uploaded/{hash}";// 校检文件hash V2

    public static final String APP_PATH_GET_CHECK_NOTE = "api/" + API_VERSION_2 + "/purchases/{note}";// 节点付费相关
    public static final String APP_PATH_CHECK_NOTE = "api/" + API_VERSION_2 + "/currency/purchases/{note}";// 节点付费相关

    public static final String APP_PATH_STORAGE_UPLAOD_FILE = "api/" + API_VERSION_2 + "/files";// 文件上传 V2

    /**
     * 创建上传任务 2018-9-6 15:12:06
     */
    public static final String APP_PATH_STORAGE_UPLAOD = "api/" + API_VERSION_2 + "/storage";

    /**
     * 上传文件标识头信息
     */
    public static final String APP_PATH_STORAGE_HEADER_FLAG = "tsplus_upload";

    // 标签
    public static final String APP_PATH_GET_ALL_TAGS = "api/" + API_VERSION_2 + "/tags";// 获取全部标签标签
    // 地区
    public static final String APP_PATH_SEARDCH_LOCATION = "api/" + API_VERSION_2 + "/locations/search";// 搜索位置
    public static final String APP_PATH_SGET_HOT_CITY = "api/" + API_VERSION_2 + "/locations/hots";// 热门城市
    /**
     * 举报评论通用，除圈子评论
     */
    public static final String APP_PATH_REPORT_COMMON_COMMENT = "api/" + API_VERSION_2 + "/report/comments/{comment_id}";

    /**
     * 仅仅测试使用
     */
    public static final String APP_PATH_TOKEN_EXPIERD = "api/music_window_rotate-token";// token过期处理

    public static final String APP_PATH_COMMENT_GROUP_DYNAMIC_FORMAT = "api/" + API_VERSION_2 + "/plus-group/group-posts/%d/comments";// 创建圈子动态评论

    public static final String APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS = "music_special";
    public static final String APP_LIKE_FEED = "feeds";
    public static final String APP_LIKE_MUSIC = "musics";
    public static final String APP_LIKE_NEWS = "news";
    public static final String APP_LIKE_GROUP_POST = "group-posts";
    public static final String APP_QUESTIONS = "questions";
    public static final String APP_QUESTIONS_ANSWER = "question-answers";

    /*******************************************  API V2  *********************************************/
    /**
     * 系统相关
     */
    // 获取启动信息
    public static final String APP_PATH_GET_BOOTSTRAPERS_INFO = "api/" + API_VERSION_2 + "/bootstrappers";
    // 获取所有广告
    public static final String APP_PATH_GET_ADVERT_INFO = "api/" + API_VERSION_2 + "/advertisingspace";
    // 获取一个广告位的广告列表
    public static final String APP_PATH_GET_SINGLE_ADVERT_INFO = "api/" + API_VERSION_2 + "/advertisingspace/{advert_id}/advertising";
    // 获取批量广告位的广告列表
    public static final String APP_PATH_GET_All_ADVERT_INFO = "api/" + API_VERSION_2 + "/advertisingspace/advertising";
    // 版本更新
    public static final String APP_PATH_GET_APP_NEW_VERSION = "api/" + API_VERSION_2 + "/plus-appversion";

    /**
     * 通用 CommonClient
     */
    // 非会员短信 ，用于发送不存在于系统中的用户短信，使用场景如注册等。
    public static final String APP_PATH_GET_NON_MEMBER_VERTIFYCODE = "api/" + API_VERSION_2 + "/verifycodes/register";
    // 获取会员短信验证码，使用场景如登陆、找回密码，其他用户行为验证等。
    public static final String APP_PATH_GET_MEMBER_VERTIFYCODE = "api/" + API_VERSION_2 + "/verifycodes";
    public static final String APP_PATH_GET_APP_VERSION = "api/" + API_VERSION_2 + "/plus-appversion";
}
