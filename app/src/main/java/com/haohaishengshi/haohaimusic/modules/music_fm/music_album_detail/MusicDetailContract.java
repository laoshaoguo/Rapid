package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail;

import android.graphics.Bitmap;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.data.source.repository.i.IMusicRepository;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicDetailContract {

    interface View extends IBaseView<Presenter> {
        void setMusicAblum(MusicAlbumDetailsBean musicAblum);

        void setCollect(boolean isCollected);

        void albumHasBeDeleted();

        void noNetWork();

        MusicAlbumDetailsBean getCurrentAblum();

        MusicAlbumListBean getmMusicAlbumListBean();

        List<MusicDetaisBean> getListDatas();

        void refreshData(int position);
    }

    interface Presenter extends IBaseTouristPresenter {
        void getMusicAblum(String id);

        void shareMusicAlbum(Bitmap bitmap);

        void handleCollect(boolean isUnCollected, String special_id);

        void getMusicDetails(String music_id);

        MusicAlbumDetailsBean getCacheAblumDetail(int id);

        void payNote(int position, int note, String psd);

        void canclePay();
    }

    interface Repository extends IMusicRepository {

    }
}
