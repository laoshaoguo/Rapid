package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import com.haohaishengshi.haohaimusic.base.AppComponent;
import com.haohaishengshi.haohaimusic.base.InjectComponent;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules ={ ShareModule.class,MusicPlayPresenterModule.class},
        dependencies = AppComponent.class)
interface MusicPlayComponent extends InjectComponent<MusicPlayActivity> {
}
