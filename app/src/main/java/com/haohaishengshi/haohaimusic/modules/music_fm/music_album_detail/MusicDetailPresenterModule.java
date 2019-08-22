package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail;


import com.haohaishengshi.haohaimusic.data.source.repository.MusicDetailRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
class MusicDetailPresenterModule {

    private MusicDetailContract.View mView;

    public MusicDetailPresenterModule(MusicDetailContract.View view) {
        mView = view;
    }

    @Provides
    MusicDetailContract.View provideMusicContractView() {
        return mView;
    }

    @Provides
    MusicDetailContract.Repository provideMusicRepository(MusicDetailRepository musicDetailRepository) {
        return musicDetailRepository;
    }

}
