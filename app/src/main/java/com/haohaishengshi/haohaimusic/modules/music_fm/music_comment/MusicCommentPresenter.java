package com.haohaishengshi.haohaimusic.modules.music_fm.music_comment;

import com.google.gson.Gson;
import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.base.AppBasePresenter;
import com.haohaishengshi.haohaimusic.base.BaseSubscribe;
import com.haohaishengshi.haohaimusic.base.BaseSubscribeForV2;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicCommentListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.data.source.local.MusicCommentListBeanGreenDaoImpl;
import com.haohaishengshi.haohaimusic.data.source.repository.BaseMusicRepository;
import com.haohaishengshi.haohaimusic.data.source.repository.CommentRepository;
import com.haohaishengshi.haohaimusic.data.source.repository.MusicCommentRepositroty;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicCommentPresenter extends AppBasePresenter<
        MusicCommentContract.View> implements MusicCommentContract.Presenter {

    @Inject
    MusicCommentRepositroty mMusicCommentRepositroty;

    @Inject
    MusicCommentListBeanGreenDaoImpl mCommentListBeanGreenDao;

    @Inject
    CommentRepository mCommentRepository;
    @Inject
    BaseMusicRepository mBaseMusicRepository;

//    private TCommonMetadataProvider mCommonMetadataProvider;

    @Inject
    public MusicCommentPresenter(MusicCommentContract
            .View rootView) {
        super( rootView);
//        mCommonMetadataProvider = new TCommonMetadataProvider(null);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void requestNetData(final String music_id, Long maxId, final boolean isLoadMore) {
        Subscription subscription;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            if (!isLoadMore) {
                getMusicDetails(music_id);
            }
            subscription = mMusicCommentRepositroty.getMusicCommentList(music_id, maxId)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribeForV2<List<MusicCommentListBean>>() {
                        @Override
                        protected void onSuccess(List<MusicCommentListBean> data) {
                            if (!data.isEmpty()) {
                                mCommentListBeanGreenDao.saveMultiData(data);
                            }

//                            TCommonMetadataProvider addBtnAnimation = new TCommonMetadataProvider(data);
//                            addBtnAnimation.convertAndSave();

                            mRootView.onNetResponseSuccess(data, isLoadMore);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            mRootView.showMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
        } else {
            if (!isLoadMore) {
                getMusicAblum(music_id);
            }
            subscription = mMusicCommentRepositroty.getAblumCommentList(music_id, maxId)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribeForV2<List<MusicCommentListBean>>() {
                        @Override
                        protected void onSuccess(List<MusicCommentListBean> data) {
                            if (!data.isEmpty()) {
                                mCommentListBeanGreenDao.saveMultiData(data);
                            }

//                            TCommonMetadataProvider addBtnAnimation = new TCommonMetadataProvider(data);
//                            addBtnAnimation.convertAndSave();

//                            List<MusicCommentListBean> localComment = mCommentListBeanGreenDao.getMyAblumComment(Integer.valueOf(music_id));
//                            if (!localComment.isEmpty()) {
//                                for (int i = 0; i < localComment.size(); i++) {
//                                    localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
//                                            .getSingleDataFromCache((long) localComment.get(i).getUser_id()));
//                                    if (localComment.get(i).getReply_user() != 0) {
//                                        localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
//                                                .getSingleDataFromCache((long) localComment.get(i)
//                                                        .getReply_user()));
//                                    }
//                                }
//                                localComment.addAll(data);
//                                data.clear();
//                                data.addAll(localComment);
//                            }
                            mRootView.onNetResponseSuccess(data, isLoadMore);

                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            mRootView.showMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
        }
        addSubscrebe(subscription);
    }


    @Override
    public void sendComment(long reply_id, String content) {
        String path;
        final MusicCommentListBean createComment = new MusicCommentListBean();
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            path = APP_PATH_MUSIC_COMMENT_FORMAT;
            createComment.setId(mRootView.getCommentId());
        } else {
            path = APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
            createComment.setId(mRootView.getCommentId());
        }
//        createComment.setState(DynamicCommentBean.SEND_ING);
        createComment.setReply_user(reply_id);
        createComment.setComment_content(content);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));
        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        if (reply_id == 0) {// 回复资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(reply_id);
            createComment.setFromUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mCommentListBeanGreenDao.insertOrReplace(createComment);
        if (mRootView.getListDatas()!=null&&mRootView.getListDatas().get(0).getComment_content() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().add(0, createComment);
        mRootView.refreshData();
        path = String.format(path, mRootView.getCommentId());

        // 更新的评论模块
//        new CommentCore_.Builder()
//                .buildCommentEvent(CommentCore_.CommentState.SEND)
//                .buildCommonMetadataAndProvider(mCommonMetadataProvider,createComment)
//                .build()
//                .handleCommentInBackGroud();

        // 新的评论模块
//        CommentCore.getInstance(CommentCore.CommentState.SEND,
//                new BackgroundTaskHandler.OnNetResponseCallBack() {
//                    @Override
//                    public void onSuccess(Object data) {
//                        CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(createComment.getComment_mark());
//                        commonMetadataBean.setComment_id(((Double) data).intValue());
//                        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_SUCCESS);
//                        mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
//                    }
//
//                    @Override
//                    public void onFailure(String message, int code) {
//                        CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(createComment.getComment_mark());
//                        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
//                        mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
//                    }
//
//                    @Override
//                    public void onException(Throwable throwable) {
//                        CommonMetadataBean commonMetadataBean = mCommonMetadataProvider.getCommentByCommentMark(createComment.getComment_mark());
//                        commonMetadataBean.setComment_state(CommonMetadataBean.SEND_ERROR);
//                        mCommonMetadataProvider.insertOrReplaceOne(commonMetadataBean);
//                    }
//                }).set$$Comment_(createComment, mCommonMetadataProvider)
//                .handleCommentInBackGroud();

        Subscription subscription = mCommentRepository.sendCommentV2(content, reply_id, 0L, path)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.comment_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(data));
                            createComment.setState(MusicCommentListBean.SEND_SUCCESS);
                            createComment.setId(jsonObject.getLong("id"));
                            mCommentListBeanGreenDao.insertOrReplace(createComment);
                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_success));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        createComment.setState(MusicCommentListBean.SEND_ERROR);
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        createComment.setState(MusicCommentListBean.SEND_ERROR);
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void reSendComment(final MusicCommentListBean createComment) {
        String path;
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            path = APP_PATH_MUSIC_COMMENT_FORMAT;
        } else {
            path = APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT;
        }
        mCommentListBeanGreenDao.insertOrReplace(createComment);
        if (mRootView.getListDatas()!=null&&mRootView.getListDatas().get(0).getComment_content() == null) {
            mRootView.getListDatas().remove(0);// 去掉占位图
        }
        mRootView.getListDatas().set(0, createComment);
        mRootView.refreshData();
        path = String.format(path, mRootView.getCommentId());
        Subscription subscription = mCommentRepository.sendComment(createComment.getComment_content(), createComment.getReply_user(),
                0L, path).doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.comment_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(data));
                            createComment.setId(jsonObject.getJSONObject("comment").getLong("id"));
                            mCommentListBeanGreenDao.insertOrReplace(createComment);
                            mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_success));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.getListDatas().set(0, createComment);
                        mRootView.refreshData();
                        mCommentListBeanGreenDao.insertOrReplace(createComment);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getMusicDetails(String music_id) {
        Subscription subscribe = mBaseMusicRepository.getMusicDetails(music_id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<MusicDetaisBean>() {
                    @Override
                    protected void onSuccess(MusicDetaisBean data) {
                        MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                        headerInfo.setCommentCount(data.getComment_count());
                        headerInfo.setId(data.getId().intValue());
                        headerInfo.setLitenerCount(data.getTaste_count() + "");
                        headerInfo.setImageUrl(ImageUtils.imagePathConvertV2(data.getSinger().getCover().getId()
                                , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                , ImageZipConfig.IMAGE_70_ZIP));
                        headerInfo.setTitle(data.getTitle());
                        mRootView.setHeaderInfo(headerInfo);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void getMusicAblum(String id) {
        Subscription subscribe = mBaseMusicRepository.getMusicAblum(id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<MusicAlbumDetailsBean>() {
                    @Override
                    protected void onSuccess(MusicAlbumDetailsBean data) {
                        MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                        headerInfo.setCommentCount(data.getComment_count());
                        headerInfo.setId(data.getId().intValue());
                        headerInfo.setLitenerCount(data.getTaste_count() + "");
                        headerInfo.setImageUrl(ImageUtils.imagePathConvertV2(data.getStorage().getId()
                                , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                                , ImageZipConfig.IMAGE_70_ZIP));
                        headerInfo.setTitle(data.getTitle());
                        mRootView.setHeaderInfo(headerInfo);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void deleteComment(MusicCommentListBean data) {
        mCommentListBeanGreenDao.deleteSingleCache(data);

        // 新的评论模块
//        CommentCore.getInstance(CommentCore.CommentState.DELETE, new CommentCore.CallBack())
//                .set$$Comment_(data,new TCommonMetadataProvider(null))
//                .handleComment();
        if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
            mBaseMusicRepository.deleteComment((int) mRootView.getCommentId(), data.getId().intValue());
        }else{
            mBaseMusicRepository.deleteAblumComment((int) mRootView.getCommentId(), data.getId().intValue());
        }
        mRootView.getListDatas().remove(data);
        if (mRootView.getListDatas().size() == 0) {// 占位
            MusicCommentListBean emptyData = new MusicCommentListBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        rx.Observable.just(1)
                .observeOn(Schedulers.io())
                .map(integer -> {
                    List<MusicCommentListBean> localComment;
                    if (mRootView.getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
                        localComment = mCommentListBeanGreenDao.getAblumCommentsCacheDataByType(ApiConfig
                                .APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS, mRootView
                                .getCommentId());
                    } else {
                        localComment = mCommentListBeanGreenDao.getAblumCommentsCacheDataByType(ApiConfig.APP_COMPONENT_MUSIC, mRootView
                                .getCommentId());
                    }

                    if (!localComment.isEmpty()) {
                        for (int i = 0; i < localComment.size(); i++) {
                            localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
                                    .getSingleDataFromCache(localComment.get(i).getUser_id()));
                            if (localComment.get(i).getReply_user() != 0) {
                                localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
                                        .getSingleDataFromCache(localComment.get(i)
                                                .getReply_user()));
                            }
                        }
                    }
                    return localComment;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musicCommentListBeans -> mRootView.onCacheResponseSuccess(musicCommentListBeans, isLoadMore), Throwable::printStackTrace);

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicCommentListBean> data, boolean isLoadMore) {
        return false;
    }

}
