package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.single_music;


import com.haohaishengshi.haohaimusic.base.AppBasePresenter;
import com.haohaishengshi.haohaimusic.base.BaseSubscribeForV2;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.data.source.repository.BaseMusicRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SingleMusicListPresenter extends AppBasePresenter<SingleMusicListContract.View>
        implements SingleMusicListContract.Presenter {

    @Inject
    BaseMusicRepository mBaseMusicRepository;

    @Inject
    public SingleMusicListPresenter(SingleMusicListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mBaseMusicRepository.getMyPaidsMusicList(maxId).subscribe(new BaseSubscribeForV2<List<MusicDetaisBean>>() {
            @Override
            protected void onSuccess(List<MusicDetaisBean> data) {
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
    public boolean insertOrUpdateData(@NotNull List<MusicDetaisBean> data, boolean isLoadMore) {
        return false;
    }
}
