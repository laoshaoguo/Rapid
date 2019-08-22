package com.haohaishengshi.haohaimusic.modules.music_fm.music_comment;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MusicCommentPresenterModule {

    MusicCommentContract.View mView;

    public MusicCommentPresenterModule(MusicCommentContract.View view) {
        mView = view;
    }

    @Provides
    MusicCommentContract.View provideMusicCommentView() {
        return mView;
    }
}
