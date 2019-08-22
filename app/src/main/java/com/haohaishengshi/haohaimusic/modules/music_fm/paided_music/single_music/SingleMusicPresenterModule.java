package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.single_music;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class SingleMusicPresenterModule {
    SingleMusicListContract.View mView;

    public SingleMusicPresenterModule(SingleMusicListContract.View view) {
        mView = view;
    }

    @Provides
    SingleMusicListContract.View provideSingleMusicContractView(){
        return mView;
    }

}
