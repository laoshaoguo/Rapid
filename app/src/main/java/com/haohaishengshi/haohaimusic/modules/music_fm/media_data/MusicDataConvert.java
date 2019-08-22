package com.haohaishengshi.haohaimusic.modules.music_fm.media_data;


import android.media.MediaMetadata;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author Jliuer
 * @Date 2017/03/16
 * @Email Jliuer@aliyun.com
 * @Description
 */
@SuppressWarnings("ALL")
public class MusicDataConvert implements MusicProviderSource {

    private MusicAlbumDetailsBean mAlbumDetailsBean;
    public static final String METADATA_KEY_GENRE = "_zhiyicx_";

    public MusicDataConvert(MusicAlbumDetailsBean albumDetailsBean) {
        mAlbumDetailsBean = albumDetailsBean;
    }

    private MusicDataConvert() {
    }

    @Override
    public Iterator<MediaMetadata> iterator() {
        ArrayList<MediaMetadata> tracks = new ArrayList<>();
        if (mAlbumDetailsBean == null) {
            return tracks.iterator();
        }
        for (MusicDetaisBean data : mAlbumDetailsBean.getMusics()) {
            // 跳过收费
            if (data.getStorage().getAmount() == 0 || data.getStorage().isPaid()){
                LogUtils.d("Iterator<MediaMetadata> :::" + data.getTitle());
                tracks.add(buildMusic(data));
            }
        }
        return tracks.iterator();
    }

    private MediaMetadata buildMusic(MusicDetaisBean needData) {
        String musicUrl = String.format(ApiConfig.APP_DOMAIN+ApiConfig.FILE_PATH,
                needData.getStorage().getId());

        String imageUrl = String.format(ImageUtils.imagePathConvertV2(needData.getSinger().getCover().getId(), 50, 50, 100));
        LogUtils.d("buildMusic--needData.getId:::" + needData.getId());
        //noinspection ResourceType
        return new MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_MEDIA_ID,
                        "" + needData.getId())

                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE,
                        "" + musicUrl)
                .putString(MediaMetadata.METADATA_KEY_ALBUM, needData.getTitle())
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, needData.isHas_like() + "")
                .putString(MediaMetadata.METADATA_KEY_ARTIST, needData.getSinger().getName())
                .putLong(MediaMetadata.METADATA_KEY_DURATION, needData.getLast_time() * 1000)
                .putLong(MediaMetadata.METADATA_KEY_YEAR,
                        (needData.getStorage().getAmount() == 0 || needData.getStorage().isPaid()) ? 1L : -1L)
                .putLong(MediaMetadata.METADATA_KEY_DURATION, needData.getLast_time() * 1000)
                .putString(MediaMetadata.METADATA_KEY_GENRE, METADATA_KEY_GENRE)
                .putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, imageUrl)
                .putString(MediaMetadata.METADATA_KEY_TITLE, needData.getTitle())
                .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, musicUrl)
                .build();
    }
}
