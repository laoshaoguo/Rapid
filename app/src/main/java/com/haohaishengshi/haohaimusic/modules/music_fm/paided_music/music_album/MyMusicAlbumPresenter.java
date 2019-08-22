package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.music_album;


import com.haohaishengshi.haohaimusic.base.AppBasePresenter;
import com.haohaishengshi.haohaimusic.base.BaseSubscribeForV2;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.source.local.MusicAlbumListBeanGreenDaoImpl;
import com.haohaishengshi.haohaimusic.data.source.repository.BaseMusicRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/08/24/18:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MyMusicAlbumPresenter extends AppBasePresenter<MyMusicAblumListContract.View>
        implements MyMusicAblumListContract.Presenter {

    @Inject
    BaseMusicRepository mBaseMusicRepository;
    @Inject
    MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;

    @Inject
    public MyMusicAlbumPresenter(MyMusicAblumListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mBaseMusicRepository.getMyPaidsMusicAlbumList(maxId).subscribe(new BaseSubscribeForV2<List<MusicAlbumListBean>>() {
            @Override
            protected void onSuccess(List<MusicAlbumListBean> data) {
                mRootView.onNetResponseSuccess(data, isLoadMore);
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.onResponseError(null, isLoadMore);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
                mRootView.onResponseError(throwable, isLoadMore);
            }
        });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void updateOneMusic(MusicAlbumListBean albumListBean) {
        mMusicAlbumListDao.updateSingleData(albumListBean);
    }
}
