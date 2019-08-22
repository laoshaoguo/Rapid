package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.music_album;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MyMusicAblumListContract {
    interface View extends ITSListView<MusicAlbumListBean,Presenter> {}
    interface Presenter extends ITSListPresenter<MusicAlbumListBean> {
        void updateOneMusic(MusicAlbumListBean albumListBean);
    }
}
