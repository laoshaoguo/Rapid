package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_list;


import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.source.repository.i.IMusicRepository;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description 音乐FM 契约类
 */
public interface MusicContract {

    interface View extends ITSListView<MusicAlbumListBean, Presenter> {

        boolean isCollection();
    }

    interface Presenter extends ITSListPresenter<MusicAlbumListBean> {
        void updateOneMusic(MusicAlbumListBean albumListBean);
        void payNote(int position, int note, String psd);

        void canclePay();
    }

    interface Repository extends IMusicRepository {

    }
}
