package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
class MusicPlayPresenterModule {
    private  MusicPlayContract.View mView;

    public MusicPlayPresenterModule(MusicPlayContract.View view) {
        mView = view;
    }

    @Provides
    MusicPlayContract.View provideMusicContractView() {
        return mView;
    }

}
