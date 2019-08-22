package com.haohaishengshi.haohaimusic.config;

/**
 * @Describe 存储所有android-eventbus Tag 标识
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class EventBusTagConfig {

    /**
     * 后台任务启动成功
     */
    public static final String EVENT_BACKGROUND_TASK_START_SUCCESS = "event_background_task_start_success";

    /**
     * 后台任务处理
     */
    public static final String EVENT_BACKGROUND_TASK = "event_background_task";
    /**
     * 暂停后台任务处理
     */
    public static final String EVENT_STOP_BACKGROUND_TASK = "event_stop_background_task";

    /**
     * 后台任务处理
     */
    public static final String EVENT_BACKGROUND_TASK_CANT_NOT_DEAL = "event_background_task_cant_not_deal";

    /**
     * 后台刷新用户信息
     */
    public static final String EVENT_USERINFO_UPDATE = "event_userinfo_update";

    /**
     * 发送动态到动态列表
     */
    public static final String EVENT_SEND_DYNAMIC_TO_LIST = "event_send_dynamic_to_List";

    /**
     * 发送动态到话题动态列表
     */
    public static final String EVENT_SEND_DYNAMIC_TO_TOPIC_LIST = "event_send_dynamic_to_topic";

    /**
     * 发送动态到频道动态列表
     */
    public static final String EVENT_SEND_DYNAMIC_TO_CHANNEL = "event_send_dynamic_to_channel";

    /**
     * 动态详情页收藏通知
     */
    public static final String EVENT_COLLECT_DYNAMIC = "event_collect_dynamic";

    /**
     * 收藏圈子动态
     */
    public static final String EVENT_COLLECT_GROUP_DYNAMIC = "event_collect_group_dynamic";

    /**
     * 点赞圈子动态
     */
    public static final String EVENT_DIG_GROUP_DYNAMIC = "event_dig_group_dynamic";

    /**
     * 更新动态列表
     */
    public static final String EVENT_UPDATE_DYNAMIC = "event_update_dynamic";
    /**
     * 更新圈子动态列表
     */
    public static final String EVENT_UPDATE_GROUP_DYNAMIC = "event_update_group_dynamic";

    /**
     * 更新圈子收藏
     */
    public static final String EVENT_UPDATE_GROUP_COLLECTION = "event_update_group_collection";

    /**
     * 动态详情界面删除内容通知
     */
    public static final String DYNAMIC_LIST_DELETE_UPDATE = "dynamic_list_delete_update";

    /**
     * 圈子更新帖子
     */
    public static final String EVENT_UPDATE_CIRCLE_POST = "event_update_circle_post";

    /**
     * 圈子更新
     */
    public static final String EVENT_UPDATE_CIRCLE = "event_update_circle";

    /**
     * 帖子详情界面删除内容通知
     */
    public static final String POST_LIST_DELETE_UPDATE = "post_list_delete_update";

    /**
     * 帖子详情界面收藏通知
     */
    public static final String POST_LIST_COLLECT_UPDATE = "post_list_collect_update";

    /**
     * 动态列表发送评论
     */
    public static final String EVENT_SEND_COMMENT_TO_DYNAMIC_LIST = "event_send_dynamic_comment_to_List";

    /**
     * 圈子的动态发送评论
     */
    public static final String EVENT_SEND_COMMENT_TO_GROUOP_DYNAMIC = "event_send_comment_to_grouop_dynamic";

    /**
     * 帖子詳情頁評論
     */
    public static final String EVENT_SEND_COMMENT_TO_CIRCLE_POST = "event_send_comment_to_circle_post";

    // 动态图片付费
    public static final String EVENT_DYNAMIC_TOLL_PICTRUE = "event_dynamic_toll_pictrue";

    // 对某人进行关注或者取消关注，需要改变个人主页关注数量
    public static final String EVENT_FOLLOW_AND_CANCEL_FOLLOW = "event_follow_and_cancle_follow";

    // 频道进行订阅或者取消订阅
    public static final String EVENT_CHANNEL_SUBSCRIB = "event_channel_subscrib";

    // 加入/退出圈子
    public static final String EVENT_GROUP_JOIN = "event_group_join";

    /**
     * 资讯相关
     */
    // 资讯列表发送评论
    public static final String EVENT_SEND_COMMENT_TO_INFO_LIST = "event_send_info_to_List";

    /**
     * 资讯收藏
     */
    public static final String EVENT_SEND_INFO_LIST_COLLECT = "event_send__info_list_collect";

    // 删除资讯
    public static final String EVENT_UPDATE_LIST_DELETE = "event_update_list_delete";

    // 资讯详情界面删除内容通知
    public static final String EVENT_SEND_INFO_LIST_DELETE_UPDATE = "event_send_info_list_delete_update";

    /**
     * 音乐相关
     */
    // 音乐播放缓冲进度
    public static final String EVENT_SEND_MUSIC_CACHE_PROGRESS = "event_send_music_cache_progress";
    // 音乐加载
    public static final String EVENT_SEND_MUSIC_LOAD = "event_send_music_load";
    // 当前音乐播放完成
    public static final String EVENT_SEND_MUSIC_COMPLETE = "event_send_music_complete";
    // 音乐改变
    public static final String EVENT_SEND_MUSIC_CHANGE = "event_send_music_change";
    // 音乐专辑收藏
    public static final String EVENT_ABLUM_COLLECT = "event_ablum_collect";

    // 音乐单曲喜欢
    public static final String EVENT_MUSIC_LIKE = "event_music_like";

    // 音乐单曲收费信息
    public static final String EVENT_MUSIC_TOLL = "event_music_toll";

    // 音乐单曲评论
    public static final String EVENT_MUSIC_COMMENT_COUNT = "event_music_comment_count";

    // 音乐单曲切换
    public static final String EVENT_MUSIC_CHANGE = "event_music_change";

    /**
     * 钱包相关
     */
    // 钱包余额充值成功后更新钱包界面
    public static final String EVENT_WALLET_RECHARGE = "event_wallet_recharge";
    public static final String EVENT_INTEGRATION_RECHARGE = "event_integration_recharge";

    // 认证相关
    public static final String EVENT_SEND_CERTIFICATION_SUCCESS = "event_send_certification_success";
    public static final String EVENT_UPDATE_CERTIFICATION_SUCCESS = "event_update_certification";

    /**
     * IM 相关
     */
    public static final String EVENT_IM_ONMESSAGERECEIVED = "onMessageReceived";
    public static final String EVENT_IM_ONMESSAGEACKRECEIVED = "onMessageACKReceived";
    public static final String EVENT_IM_ONCONNECTED = "onConnected";
    public static final String EVENT_IM_AUTHSUCESSED = "onauthSucessed";
    public static final String EVENT_IM_ONDISCONNECT = "onDisconnect";
    public static final String EVENT_IM_ONERROR = "onError";
    public static final String EVENT_IM_ONMESSAGETIMEOUT = "onMessageTimeout";
    public static final String EVENT_IM_ONCONVERSATIONCRATED = "onConversationCrated";

    public static final String EVENT_IM_ONMESSAGERECEIVED_V2 = "onMessageReceivedV2";
    /**
     * 重新登录
     */
    public static final String EVENT_IM_RELOGIN_V2 = "imReLoginV2";
    public static final String EVENT_IM_GET_GROUP_INFO = "getChatGroupInfo";
    public static final String EVENT_IM_GROUP_EDIT_NAME = "group_edit_name";
    public static final String EVENT_IM_DELETE_QUIT = "group_quit";
    public static final String EVENT_IM_GROUP_CHANGE_OWNER = "group_change_owner";
    public static final String EVENT_IM_GROUP_DATA_CHANGED = "group_data_changed";
    public static final String EVENT_IM_GROUP_ADD_MEMBER = "group_add_member";
    public static final String EVENT_IM_GROUP_REMOVE_MEMBER = "group_remove_member";
    public static final String EVENT_IM_GROUP_CREATE_FROM_SINGLE = "group_create_from_single";
    public static final String EVENT_IM_GROUP_UPDATE_GROUP_INFO = "group_update_group_info";

    /**
     * 消息底部提示信息相关
     */
    public static final String EVENT_IM_SET_MESSAGE_TIP_VISABLE = "setMessageTipVisable";
    public static final String EVENT_IM_SET_NOTIFICATION_TIP_VISABLE = "setNotificationTipVisable";
    public static final String EVENT_IM_SET_MINE_TIP_VISABLE = "setMineTipVisable";
    public static final String EVENT_IM_SET_MINE_FANS_TIP_VISABLE = "setMineFansTipVisable";
    /**
     * Jpush  推送相关
     */
    public static final String EVENT_JPUSH_RECIEVED_MESSAGE_UPDATE_MESSAGE_LIST = "onJpushMessageRecievedUpdateMessageList";

    /**
     * 消息通知
     */
    public static final String EVENT_UNREAD_NOTIFICATION_LIMIT = "unread-notification-limit";
    /**
     * 问答相关
     */
    public static final String EVENT_CHANGE_EXPERT = "event_change_expert";
    // 答案发送评论
    public static final String EVENT_SEND_COMMENT_TO_ANSWER_LIST = "event_send_answer_comment_to_List";
    // 问答-话题
    public static final String EVENT_QA_SUBSCRIB = "event_qatopic_subscrib";
    // 删除答案
    public static final String EVENT_UPDATE_ANSWER_LIST_DELETE = "event_update_answer_list_delete";
    // 删除问题
    public static final String EVENT_UPDATE_QUESTION_DELETE = "event_update_question_delete";
    // 评论问题
    public static final String EVENT_SEND_COMMENT_TO_QUESTION_LIST = "event_send_question_comment_to_List";
    // 评论点赞
    public static final String EVENT_UPDATE_ANSWER_LIST_LIKE = "event_update_answer_list_like";
    // 更新答案或者问题
    public static final String EVENT_UPDATE_ANSWER_OR_QUESTION = "event_update_answer_or_question";
    // 发布答案
    public static final String EVENT_PUBLISH_ANSWER = "event_publish_answer";
    // 发布问题
    public static final String EVENT_PUBLISH_QUESTION = "event_publish_question";
    // 围观答案
    public static final String EVENT_ONLOOK_ANSWER = "event_onlook_answer";
    // 采纳答案
    public static final String EVENT_ADOPT_ANSWER = "event_adopt_answer";

    /**
     * 签到
     */
    public static final String EVENT_CHECK_IN_CLICK = "check_in_click";
    public static final String EVENT_WX_PAY_RESULT = "event_wx_pay_result";

    /**
     * 附近的人
     */
    public static final String EVENT_NEARBY_LOCATION = "nearby_location";
    public static final String EVENT_NEARBY_LOCATION_UPDATE = "nearby_location_update";

    public static final String EVENT_NETSTATE_CHANGE = "netstate_change";

    public static final String EVENT_SEND_DYNAMIC_PHOT_FIRST_OPEN_SEND_DYNAMIC_PAGE = "sendDynamicPhotFirstOpenSendDynamicPage";

    // 黑名单中移除用户
    public static final String EVENT_USER_REMOVE_FROM_BLACK_LIST = "user_remove_from_balck_list";

    public static final String EVENT_USER_HEADPIC_UPDATE = "event_user_update_headpic";

}
