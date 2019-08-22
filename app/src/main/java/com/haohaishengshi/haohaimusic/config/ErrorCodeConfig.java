package com.haohaishengshi.haohaimusic.config;

/**
 * @author LiuChao
 * @describe 错误码定义 ，具体错误信息，查看文档{@see document/app/ERROR_MESSAGE_CODE.md}
 * @date 2017/1/19
 * @contact email:450127106@qq.com
 */

public class ErrorCodeConfig {
    /**
     * 认证错误
     */
    public static final int TOKEN_EXPIERD = 1012;// token 过期
    public static final int NEED_RELOGIN = 1013;// token 刷新失败，需要重新登录
    public static final int NEED_NO_DEVICE = 1014;// 移动端设备登录/注册未传递设备号
    public static final int OTHER_DEVICE_LOGIN = 1015;// token 在其他设备登陆
    public static final int TOKEN_NOT_EXIST = 1016;// token 不存在
    public static final int USER_AUTH_FAIL = 1099;// 用户认证失败

    /**
     * 操作失败，需要重新操作
     */
    public static final int STOREAGE_UPLOAD_FAIL = 2002;// 上传失败
    public static final int IM_CREATE_CHAT_AUTH_FAIL = 3002;// 创建聊天授权失败
    public static final int IM_CREATE_CONVERSATION_FAIL = 3005;// 创建对话失败
    public static final int IM_UPDATE_AUTH_FAIL = 3008;// 刷新授权失败
    public static final int IM_DELETE_CONVERSATION_FAIL = 3009;// 对话删除失败
    public static final int IM_HANDLE_CONVERSATION_MEMBER_FAIL = 3012;// 操作限制对话成员失败
    public static final int IM_QUIT_CONVERSATION_FAIL = 3013;// 退出对话操作失败
    public static final int IM_DELDETE_CONVERSATION_FAIL = 3014;// 移除对话成员失败

    /**
     * 需要手动处理
     */
    public static final int AUTH_FAIL = 401;// 	账号过期或者被挤下线
    public static final int DATA_HAS_BE_DELETED = 404;// 	查询的数据不存在或已删除
    public static final int DATA_HAS_BE_FORBIDDEN  = 403;// 	查询的数据禁止访问
    public static final int REPEAT_OPERATION = 422;// 	重复操作
    public static final int SYSTEM_MAINTENANCE = 503; // 系统维护

}
