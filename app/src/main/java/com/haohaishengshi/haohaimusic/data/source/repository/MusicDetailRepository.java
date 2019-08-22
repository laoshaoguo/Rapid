package com.haohaishengshi.haohaimusic.data.source.repository;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.data.source.remote.ServiceManager;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailContract;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicDetailRepository extends BaseMusicRepository implements MusicDetailContract.Repository {

    @Inject
    public MusicDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

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
}
