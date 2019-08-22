package com.haohaishengshi.haohaimusic.data.source.repository;

import android.app.Application;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.base.BaseSubscribeForV2;
import com.haohaishengshi.haohaimusic.config.SharePreferenceTagConfig;
import com.haohaishengshi.haohaimusic.data.beans.AuthBean;
import com.haohaishengshi.haohaimusic.data.source.remote.CommonClient;
import com.haohaishengshi.haohaimusic.data.source.remote.ServiceManager;
import com.haohaishengshi.haohaimusic.data.source.repository.i.IAuthRepository;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;

import org.simple.eventbus.EventBus;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public class AuthRepository implements IAuthRepository {
    public static final int MAX_RETRY_COUNTS = 2;//重试次数
    public static final int RETRY_DELAY_TIME = 1;// 重试间隔时间,单位 s

    private CommonClient mCommonClient;

    @Inject
    Application mContext;


//    @Inject
//    CommentedBeanGreenDaoImpl mCommentedBeanGreenDao;
//    @Inject
//    SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;
//
//    @Inject
//    RechargeSuccessBeanGreenDaoImpl mRechargeSuccessBeanGreenDao;
//    @Inject
//    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;
//    @Inject
//    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
//    @Inject
//    QAPublishBeanGreenDaoImpl mQAPublishBeanGreenDaoImpl;
//    @Inject
//    AnswerDraftBeanGreenDaoImpl mAnswerDraftBeanGreenDaoImpl;
//    @Inject
//    UserTagBeanGreenDaoImpl mUserTagBeanGreenDaoimpl;
//    @Inject
//    CirclePostListBeanGreenDaoImpl mCirclePostListBeanGreenDao;
//    @Inject
//    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;
//    @Inject
//    CircleInfoGreenDaoImpl mCircleInfoGreenDao;
//    @Inject
//    MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;
//    @Inject
//    PostDraftBeanGreenDaoImpl mPostDraftBeanGreenDao;
//    @Inject
//    InfoDraftBeanGreenDaoImpl mInfoDraftBeanGreenDao;


    @Inject
    public AuthRepository(ServiceManager serviceManager) {
//        mUserInfoClient = serviceManager.getUserInfoClient();
        mCommonClient = serviceManager.getCommonClient();
    }


    @Override
    public boolean saveAuthBean(AuthBean authBean) {
        authBean.setToken_request_time(System.currentTimeMillis());
        AppApplication.setmCurrentLoginAuth(authBean);
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN, authBean);
    }

    @Override
    public AuthBean getAuthBean() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            AppApplication.setmCurrentLoginAuth(SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN));
        }
        return AppApplication.getmCurrentLoginAuth();
    }


    /**
     * 刷新token
     */
    @Override
    public void refreshToken() {
        if (!isNeededRefreshToken()) {
            return;
        }
        AuthBean authBean = getAuthBean();
        mCommonClient.refreshToken()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        authBean.setToken(data.getToken());
                        authBean.setExpires(data.getExpires());
                        authBean.setRefresh_token(data.getRefresh_token());
                        // 获取了最新的token，将这些信息保存起来
                        saveAuthBean(authBean);
                        // 刷新im信息
//                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean
//                                (BackgroundTaskRequestMethodConfig.GET_IM_INFO));
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    /**
     * 同步刷新 token
     */
    @Override
    public Call<AuthBean> refreshTokenSyn() {
        return mCommonClient.refreshTokenSyn();

    }


    /**
     * 删除认证信息
     *
     * @return
     */
    @Override
    public boolean clearAuthBean() {
        // 异步清理glide 缓存
        Observable.create(subscriber -> {
            Glide.get(mContext).clearDiskCache();
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, Throwable::printStackTrace);
        WindowUtils.hidePopupWindow();
        if (AppApplication.getPlaybackManager() != null) { // 释放音乐播放器
            AppApplication.getPlaybackManager().handleStopRequest(null);
        }
//        BackgroundTaskManager.getInstance(mContext).closeBackgroundTask();// 关闭后台任务
//        new JpushAlias(mContext, "").setAlias(); // 注销极光
//        MessageDao.getInstance(mContext).delDataBase();// 清空聊天信息、对话
//        TSEMHyphenate.getInstance().signOut(null);
//        mDynamicBeanGreenDao.clearTable();
//        mAnswerDraftBeanGreenDaoImpl.clearTable();
//        mQAPublishBeanGreenDaoImpl.clearTable();
//        mDynamicCommentBeanGreenDao.clearTable();
//        mGroupInfoBeanGreenDao.clearTable();
//        mInfoListDataBeanGreenDao.clearTable();
//        mRechargeSuccessBeanGreenDao.clearTable();
//        mDynamicDetailBeanV2GreenDao.clearTable();
//        mDynamicDetailBeanGreenDao.clearTable();
//        mDynamicToolBeanGreenDao.clearTable();
//        mTopDynamicBeanGreenDao.clearTable();
//        mDigedBeanGreenDao.clearTable();
//        mCirclePostListBeanGreenDao.clearTable();
//        mCirclePostCommentBeanGreenDao.clearTable();
//        mCircleInfoGreenDao.clearTable();
//        mCommentedBeanGreenDao.clearTable();
//        mPostDraftBeanGreenDao.clearTable();
//        mInfoDraftBeanGreenDao.clearTable();
//        mSystemConversationBeanGreenDao.clearTable();
//        MessageDao.getInstance(mContext).delDataBase();
//        mMusicAlbumListDao.clearTable();
        AppApplication.setmCurrentLoginAuth(null);
        //处理 Ts 助手
//        SystemRepository.resetTSHelper(mContext);
        return SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IMCONFIG)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_LAST_FLUSHMESSAGE_TIME);
    }

    @Override
    public void clearThridAuth() {
        UMShareAPI mShareAPI = UMShareAPI.get(mContext);

        try {
            mShareAPI.deleteOauth(ActivityHandler.getInstance()
                    .currentActivity(), SHARE_MEDIA.QQ, umAuthListener);
            mShareAPI.deleteOauth(ActivityHandler.getInstance()
                    .currentActivity(), SHARE_MEDIA.WEIXIN, umAuthListener);
            mShareAPI.deleteOauth(ActivityHandler.getInstance()
                    .currentActivity(), SHARE_MEDIA.SINA, umAuthListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearThridAuth(SHARE_MEDIA share_media) {
        UMShareAPI mShareAPI = UMShareAPI.get(mContext);

        try {
            mShareAPI.deleteOauth(ActivityHandler.getInstance()
                    .currentActivity(), share_media, umAuthListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    /**
     * 是否登录过成功了，Token 并未过期
     *
     * @return
     */
    @Override
    public boolean isLogin() {
        return getAuthBean() != null;
    }

    /**
     * 是否是游客
     *
     * @return true
     */
    @Override
    public boolean isTourist() {
        return getAuthBean() == null;
    }

    @Override
    public void loginIM() {

    }


    /**
     * 是否需要刷新token
     *
     * @return
     */
    @Override
    public boolean isNeededRefreshToken() {
        AuthBean authBean = getAuthBean();

        // 没有token，不需要刷新
        return authBean != null && authBean.getToken_is_expired() && !authBean.getRefresh_token_is_expired();
    }

}
