package com.haohaishengshi.haohaimusic.modules.music_fm.music_comment;

import com.haohaishengshi.haohaimusic.base.AppComponent;
import com.haohaishengshi.haohaimusic.base.InjectComponent;
import com.zhiyicx.common.dagger.scope.FragmentScoped;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = MusicCommentPresenterModule.class)
public interface MusicCommentComponent extends InjectComponent<MusicCommentActivity> {
}
