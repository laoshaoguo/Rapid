package com.haohaishengshi.haohaimusic.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicCommentListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.data.source.local.MusicAlbumListBeanGreenDaoImpl;
import com.haohaishengshi.haohaimusic.data.source.remote.MusicClient;
import com.haohaishengshi.haohaimusic.data.source.remote.ServiceManager;
import com.haohaishengshi.haohaimusic.data.source.repository.i.IMusicRepository;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @Author Jliuer
 * @Date 2017/08/24/17:12
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BaseMusicRepository implements IMusicRepository {

    protected MusicClient mMusicClient;
    @Inject
    protected Application mContext;
//    @Inject
//    protected UserInfoRepository mUserInfoRepository;
    @Inject
    protected MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;

    @Inject
    public BaseMusicRepository(ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
    }

    @Override
    public Observable<List<MusicCommentListBean>> getMusicCommentList(String music_id,
                                                                      long max_id) {
        return mMusicClient.getMusicCommentList(music_id, max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .flatMap((Func1<List<MusicCommentListBean>, Observable<List<MusicCommentListBean>>>) commentedBeens -> {
                    if (commentedBeens.isEmpty()) {
                        return Observable.just(commentedBeens);
                    } else {
                        final List<Object> user_ids = new ArrayList<>();
                        for (MusicCommentListBean commentListBean : commentedBeens) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_user());
                        }

//                        return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
//                            // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
//                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new
//                                    SparseArray<>();
//                            for (UserInfoBean userInfoBean : userinfobeans) {
//                                userInfoBeanSparseArray.put(userInfoBean.getUser_id()
//                                        .intValue(), userInfoBean);
//                            }
//                            for (MusicCommentListBean commentListBean : commentedBeens) {
//                                commentListBean.setFromUserInfoBean(
//                                        (userInfoBeanSparseArray.get((int) commentListBean
//                                                .getUser_id())));
//                                if (commentListBean.getReply_user() == 0) { // 如果
//                                    // reply_user_id = 0 回复动态
//                                    UserInfoBean userInfoBean = new UserInfoBean();
//                                    userInfoBean.setUser_id(0L);
//                                    commentListBean.setToUserInfoBean(userInfoBean);
//                                } else {
//                                    commentListBean.setToUserInfoBean(userInfoBeanSparseArray.get((int) commentListBean.getReply_user()));
//                                }
//
//                            }
//                            return commentedBeens;
//                        });
                        return null;
                    }
                });

    }

    @Override
    public Observable<List<MusicCommentListBean>> getAblumCommentList(String special_id, Long max_id) {
        return mMusicClient.getAblumCommentList(special_id, max_id,
                TSListFragment.DEFAULT_PAGE_SIZE)
                .flatMap((Func1<List<MusicCommentListBean>, Observable<List<MusicCommentListBean>>>) listBaseJson -> {

                    if (listBaseJson.isEmpty()) {
                        return Observable.just(listBaseJson);
                    } else {
                        final List<Object> user_ids = new ArrayList<>();
                        for (MusicCommentListBean commentListBean : listBaseJson) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_user());
                        }

//                        return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
//                            // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
//                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new
//                                    SparseArray<>();
//                            for (UserInfoBean userInfoBean : userinfobeans) {
//                                userInfoBeanSparseArray.put(userInfoBean.getUser_id()
//                                        .intValue(), userInfoBean);
//                            }
//                            for (MusicCommentListBean commentListBean : listBaseJson) {
//                                commentListBean.setFromUserInfoBean(
//                                        (userInfoBeanSparseArray.get((int) commentListBean
//                                                .getUser_id())));
//                                if (commentListBean.getReply_user() == 0) { // 如果
//                                    // reply_user_id = 0 回复动态
//                                    UserInfoBean userInfoBean = new UserInfoBean();
//                                    userInfoBean.setUser_id(0L);
//                                    commentListBean.setToUserInfoBean(userInfoBean);
//                                } else {
//                                    commentListBean.setToUserInfoBean(userInfoBeanSparseArray.get((int) commentListBean.getReply_user()));
//                                }
//
//                            }
//                            return listBaseJson;
//                        });
                        return null;
                    }
                });
    }

    @Override
    public void deleteComment(int music_id, int comment_id) {

    }

    @Override
    public void deleteAblumComment(int special, int comment_id) {

    }

//    @Override
//    public void sendComment(int music_id, int reply_id, String content, String path, Long comment_mark,
//                            BackgroundTaskHandler.OnNetResponseCallBack callBack) {
//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("comment_content", content);
//        params.put("reply_to_user_id", reply_id);
//        params.put(NET_CALLBACK, callBack);
//        // 后台处理
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                (BackgroundTaskRequestMethodConfig.POST, params);
//        backgroundRequestTaskBean.setPath(String.format(path,
//                music_id));
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                (backgroundRequestTaskBean);
//    }

//    @Override
//    public void deleteComment(int music_id, int comment_id) {
//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        HashMap<String, Object> params = new HashMap<>();
//        // 后台处理
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                (BackgroundTaskRequestMethodConfig.DELETE, params);
//        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig
//                .APP_PATH_MUSIC_DELETE_COMMENT_FORMAT, music_id, comment_id));
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                (backgroundRequestTaskBean);
//    }

//    @Override
//    public void deleteAblumComment(int special, int comment_id) {
//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        HashMap<String, Object> params = new HashMap<>();
//        // 后台处理
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                (BackgroundTaskRequestMethodConfig.DELETE, params);
//        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig
//                .APP_PATH_MUSIC_DELETE_ABLUM_COMMENT_FORMAT, special, comment_id));
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                (backgroundRequestTaskBean);
//    }

    @Override
    public Observable<MusicAlbumDetailsBean> getMusicAblum(String id) {
        return mMusicClient.getMusicAblum(id);
    }

    @Override
    public Observable<MusicDetaisBean> getMusicDetails(String music_id) {
        return mMusicClient.getMusicDetails(music_id);
    }

    @Override
    public void handleCollect(boolean isCollected, final String special_id) {
//        Observable.just(isCollected)
//                .observeOn(Schedulers.io())
//                .subscribe(aBoolean -> {
//                    BackgroundRequestTaskBean backgroundRequestTaskBean;
//                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("special_id", special_id);
//                    // 后台处理
//                    if (aBoolean) {
//                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                                (BackgroundTaskRequestMethodConfig.POST_V2, params);
//                    } else {
//                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
//                    }
//                    backgroundRequestTaskBean.setPath(String.format(ApiConfig
//                            .APP_PATH_MUSIC_ABLUM_COLLECT_FORMAT, special_id));
//                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                            (backgroundRequestTaskBean);
//                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void shareAblum(String special_id) {
//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("special_id", special_id);
//        // 后台处理
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                (BackgroundTaskRequestMethodConfig.PATCH, params);
//        backgroundRequestTaskBean.setPath(String.format(ApiConfig
//                .APP_PATH_MUSIC_ABLUM_SHARE, special_id));
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                (backgroundRequestTaskBean);
    }

    @Override
    public Observable<List<MusicAlbumListBean>> getMusicAblumList(long max_id) {
        return mMusicClient.getMusicList(max_id, TSListFragment.DEFAULT_PAGE_SIZE,null);
    }

    @Override
    public List<MusicAlbumListBean> getMusicAlbumFromCache(long maxId) {
        return mMusicAlbumListDao.getMultiDataFromCache();
    }

    @Override
    public void handleLike(boolean isLiked, final String music_id) {
//        Observable.just(isLiked)
//                .observeOn(Schedulers.io())
//                .subscribe(aBoolean -> {
//                    BackgroundRequestTaskBean backgroundRequestTaskBean;
//                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("music_id", music_id);
//                    // 后台处理
//                    if (aBoolean) {
//                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                                (BackgroundTaskRequestMethodConfig.POST_V2, params);
//                    } else {
//                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
//                    }
//                    backgroundRequestTaskBean.setPath(String.format(ApiConfig
//                            .APP_PATH_MUSIC_DIGG_FORMAT, music_id));
//                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                            (backgroundRequestTaskBean);
//                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void shareMusic(String music_id) {
//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("music_id", music_id);
//        // 后台处理
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean
//                (BackgroundTaskRequestMethodConfig.PATCH, params);
//        backgroundRequestTaskBean.setPath(String.format(ApiConfig
//                .APP_PATH_MUSIC_SHARE, music_id));
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
//                (backgroundRequestTaskBean);
    }

    @Override
    public Observable<List<MusicDetaisBean>> getMyPaidsMusicList(long max_id) {
        return mMusicClient.getMyPaidsMusicList(max_id,  TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<MusicAlbumListBean>> getMyPaidsMusicAlbumList(long max_id) {
        return mMusicClient.getMyPaidsMusicAlbumList(max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<MusicAlbumListBean>> getCollectMusicList(Long max_id, Integer limit) {
        return mMusicClient.getCollectMusicList(max_id, limit);
    }

    @Override
    public List<MusicAlbumListBean> getMusicCollectAlbumFromCache(long maxId) {
        return mMusicAlbumListDao.getMyCollectAlbum();
    }
}
