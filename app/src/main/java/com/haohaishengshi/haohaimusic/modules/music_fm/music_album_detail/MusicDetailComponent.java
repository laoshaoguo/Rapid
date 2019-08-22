package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail;

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
@Component(dependencies = AppComponent.class, modules ={ShareModule.class,MusicDetailPresenterModule.class} )
interface MusicDetailComponent extends InjectComponent<MusicDetailActivity> {
}
