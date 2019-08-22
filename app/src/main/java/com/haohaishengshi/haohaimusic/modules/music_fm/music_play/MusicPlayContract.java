package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import android.graphics.Bitmap;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
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
public interface MusicPlayContract {

    interface View extends IBaseView<Presenter> {
        MusicDetaisBean getCurrentMusic();
        List<MusicDetaisBean> getListDatas();

        void refreshData(int position);
        MusicAlbumDetailsBean getCurrentAblum();
    }

    interface Presenter extends IBaseTouristPresenter {
        void shareMusic(Bitmap bitmap);
        void payNote(int position, int note, String psd);
        void handleLike(boolean isLiked, final String music_id);

        void canclePay();
    }

    interface Repository extends IMusicRepository {

    }
}
