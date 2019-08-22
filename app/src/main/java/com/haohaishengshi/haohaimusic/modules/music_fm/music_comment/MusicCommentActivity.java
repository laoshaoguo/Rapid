package com.haohaishengshi.haohaimusic.modules.music_fm.music_comment;


import android.content.Intent;

import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.zhiyicx.baseproject.base.TSActivity;

import static com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;

public class MusicCommentActivity extends TSActivity<MusicCommentPresenter, MusicCommentFragment> {

    @Override
    protected MusicCommentFragment getFragment() {
        return MusicCommentFragment.newInstance(getIntent().getBundleExtra(CURRENT_COMMENT));
    }

    @Override
    protected void componentInject() {
        DaggerMusicCommentComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .musicCommentPresenterModule(new MusicCommentPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }
}
