package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_list;


import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MusicPresenterModule {
    private MusicContract.View view;

    public MusicPresenterModule(MusicContract.View view) {
        this.view = view;
    }

    @Provides
    MusicContract.View provideMusicContractView() {
        return view;
    }

}
