package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.music_album;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MyMusicAlbumListPresenterModule {
    MyMusicAblumListContract.View mView;

    public MyMusicAlbumListPresenterModule(MyMusicAblumListContract.View view) {
        mView = view;
    }

    @Provides
    MyMusicAblumListContract.View provideMyMusicAblumListContractView() {
        return mView;
    }

}
