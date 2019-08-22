package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.single_music;

import com.haohaishengshi.haohaimusic.base.AppComponent;
import com.haohaishengshi.haohaimusic.base.InjectComponent;
import com.zhiyicx.common.dagger.scope.FragmentScoped;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = SingleMusicPresenterModule.class)
public interface SingleMusicLIstComponent extends InjectComponent<MySingleMusicListFragment> {
}
