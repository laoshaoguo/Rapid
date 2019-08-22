package com.haohaishengshi.haohaimusic.data.source.remote;


import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

@Singleton
public class ServiceManager {
    private CommonClient mCommonClient;
//    private LoginClient mLoginClient;
//    private RegisterClient mRegisterClient;
//    private PasswordClient mPasswordClient;
//    private UserInfoClient mUserInfoClient;
//    private ChatInfoClient mChatInfoClient;
    private MusicClient mMusicClient;
//    private InfoMainClient mInfoMainClient;
//    private FollowFansClient mFollowFansClient;
//    private DynamicClient mDynamicClient;
//    private ChannelClient mChannelClient;
//    private CommonCommentClient mCommonCommentClient;
//    private WalletClient mWalletClient;
//    private QAClient mQAClient;
//    private RankClient mRankClien;
//    private CircleClient mCircleClient;
//    private EasemobClient mEasemobClient;
//    private TopicClient mTopicClient;

    /**
     * 如果需要添加 service 只需在构造方法中添加对应的 service,在提供 get 方法返回出去,只要在 ServiceModule 提供了该 service
     * Dagger2 会自行注入
     *
     * @param commonClient
     */
    @Inject
    public ServiceManager(CommonClient commonClient
//            , LoginClient loginClient
//            , RegisterClient registerClient
//            , PasswordClient passwordClient
//            , UserInfoClient userInfoClient
//            , ChatInfoClient chatInfoClient
            , MusicClient musicClient
//            , InfoMainClient infoMainClient
//            , FollowFansClient followFansClient
//            , DynamicClient mDynamicClient
//            , ChannelClient mChannelClient
//            , WalletClient walletClient
//            , QAClient qAClient
//            , CommonCommentClient commonCommentClient
//            , RankClient rankClient
//            , CircleClient circleClient
//            , EasemobClient easemobClient
//            , TopicClient topicClient
            ) {
        this.mCommonClient = commonClient;
//        this.mLoginClient = loginClient;
//        this.mQAClient = qAClient;
//        this.mRegisterClient = registerClient;
//        this.mUserInfoClient = userInfoClient;
//        this.mChatInfoClient = chatInfoClient;
//        this.mPasswordClient = passwordClient;
        this.mMusicClient = musicClient;
//        this.mInfoMainClient = infoMainClient;
//        this.mFollowFansClient = followFansClient;
//        this.mDynamicClient = mDynamicClient;
//        this.mChannelClient = mChannelClient;
//        this.mCommonCommentClient = commonCommentClient;
//        this.mWalletClient = walletClient;
//        this.mRankClien = rankClient;
//        this.mCircleClient = circleClient;
//        this.mEasemobClient = easemobClient;
//        this.mTopicClient = topicClient;
    }

    public CommonClient getCommonClient() {
        return mCommonClient;
    }

//    public CommonCommentClient getCommonCommentClient() {
//        return mCommonCommentClient;
//    }
//
//    public LoginClient getLoginClient() {
//        return mLoginClient;
//    }
//
//    public RegisterClient getRegisterClient() {
//        return mRegisterClient;
//    }
//
//    public UserInfoClient getUserInfoClient() {
//        return mUserInfoClient;
//    }
//
//    public ChatInfoClient getChatInfoClient() {
//        return mChatInfoClient;
//    }
//
//    public PasswordClient getPasswordClient() {
//        return mPasswordClient;
//    }
//
    public MusicClient getMusicClient() {
        return mMusicClient;
    }
//
//    public InfoMainClient getInfoMainClient() {
//        return mInfoMainClient;
//    }
//
//    public FollowFansClient getFollowFansClient() {
//        return mFollowFansClient;
//    }
//
//    public DynamicClient getDynamicClient() {
//        return mDynamicClient;
//    }
//
//    public ChannelClient getChannelClient() {
//        return mChannelClient;
//    }
//
//    public WalletClient getWalletClient() {
//        return mWalletClient;
//    }
//
//    public QAClient getQAClient() {
//        return mQAClient;
//    }
//
//    public RankClient getRankClien() {
//        return mRankClien;
//    }
//
//    public CircleClient getCircleClient() {
//        return mCircleClient;
//    }
//
//    public EasemobClient getEasemobClient() {
//        return mEasemobClient;
//    }
//
//    public TopicClient getTopicClient() {
//        return mTopicClient;
//    }
}
