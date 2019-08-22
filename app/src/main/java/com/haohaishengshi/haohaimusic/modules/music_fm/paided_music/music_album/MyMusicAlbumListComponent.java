package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.music_album;

import com.haohaishengshi.haohaimusic.base.AppComponent;
import com.haohaishengshi.haohaimusic.base.InjectComponent;
import com.zhiyicx.common.dagger.scope.FragmentScoped;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/24/18:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = MyMusicAlbumListPresenterModule.class)
public interface MyMusicAlbumListComponent extends InjectComponent<MyMusicAblumListFragment> {
}
