package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import android.content.Intent;

import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;

import static com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailFragment.MUSIC_INFO;

public class MusicPlayActivity extends TSActivity<MusicPlayPresenter, MusicPlayFragment> {

    @Override
    protected MusicPlayFragment getFragment() {
        return MusicPlayFragment.newInstance(getIntent().getBundleExtra(MUSIC_INFO));
    }

    @Override
    protected void componentInject() {
        DaggerMusicPlayComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .musicPlayPresenterModule(new MusicPlayPresenterModule(mContanierFragment))
//                .shareModule(new ShareModule(this))
                .shareModule(null)
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }
}
