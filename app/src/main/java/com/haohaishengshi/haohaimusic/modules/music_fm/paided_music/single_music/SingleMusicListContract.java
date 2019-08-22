package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.single_music;

import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface SingleMusicListContract {
    interface View extends ITSListView<MusicDetaisBean,Presenter>{}
    interface Presenter extends ITSListPresenter<MusicDetaisBean>{}
}
