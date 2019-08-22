package com.haohaishengshi.haohaimusic.modules.music_fm.music_helper;

import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicProvider;
import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicSearchParams;

import java.util.ArrayList;
import java.util.List;

import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_SEARCH;


/**
 * @Author Jliuer
 * @Date 2017/2/20/14:07
 * @Email Jliuer@aliyun.com
 * @Description 播放队列
 */
public class QueueHelper {

    @Nullable
    public static List<MediaSession.QueueItem> getPlayingQueue(String mediaId,
                                                               MusicProvider musicProvider) {
        String[] hierarchy = MediaIDHelper.getHierarchy(mediaId);
        if (hierarchy.length != 2) {
            return null;
        }
        String categoryType = hierarchy[0];
        String categoryValue = hierarchy[1];

        Iterable<MediaMetadata> tracks = null;

        if (categoryType.equals(MEDIA_ID_MUSICS_BY_GENRE)) {
            tracks = musicProvider.getMusicsByGenre(categoryValue);
        } else if (categoryType.equals(MEDIA_ID_MUSICS_BY_SEARCH)) {
            tracks = musicProvider.searchMusicBySongTitle(categoryValue);
        }

        if (tracks == null) {
            return null;
        }

        return convertToQueue(tracks, hierarchy[0], hierarchy[1]);
    }

    public static List<MediaSession.QueueItem> getPlayingQueueFromSearch(String query,
                                                                               Bundle queryParams, MusicProvider musicProvider) {

        MusicSearchParams params = new MusicSearchParams(query, queryParams);

        if (params.isAny) {
            return getRandomQueue(musicProvider);
        }

        Iterable<MediaMetadata> result = null;
        if (params.isAlbumFocus) {
            result = musicProvider.searchMusicByAlbum(params.album);
        } else if (params.isGenreFocus) {
            result = musicProvider.getMusicsByGenre(params.genre);
        } else if (params.isArtistFocus) {
            result = musicProvider.searchMusicByArtist(params.artist);
        } else if (params.isSongFocus) {
            result = musicProvider.searchMusicBySongTitle(params.song);
        }

        if (params.isUnstructured || result == null || !result.iterator().hasNext()) {
            result = musicProvider.searchMusicBySongTitle(query);
        }
        return convertToQueue(result, MEDIA_ID_MUSICS_BY_SEARCH, query);
    }


    public static int getMusicIndexOnQueue(Iterable<MediaSession.QueueItem> queue,
                                           String mediaId) {
        int index = 0;
        for (MediaSession.QueueItem item : queue) {
            if (mediaId.equals(item.getDescription().getMediaId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static int getMusicIndexOnQueue(Iterable<MediaSession.QueueItem> queue,
                                           long queueId) {
        int index = 0;
        for (MediaSession.QueueItem item : queue) {
            if (queueId == item.getQueueId()) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static List<MediaSession.QueueItem> convertToQueue(
            Iterable<MediaMetadata> tracks, String... categories) {
        List<MediaSession.QueueItem> queue = new ArrayList<>();
        int count = 0;
        for (MediaMetadata track : tracks) {
            String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                    track.getDescription().getMediaId(), categories);

            MediaMetadata trackCopy = new MediaMetadata.Builder(track)
                    .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                    .build();

            MediaSession.QueueItem item = new MediaSession.QueueItem(
                    trackCopy.getDescription(), count++);
            queue.add(item);
        }
        return queue;

    }

    public static List<MediaSession.QueueItem> getRandomQueue(MusicProvider musicProvider) {
        Iterable<MediaMetadata> shuffled = musicProvider.getShuffledMusic();
        List<MediaMetadata> result = new ArrayList<>();
        for (MediaMetadata metadata : shuffled) {
            result.add(metadata);
        }
        return convertToQueue(result, MEDIA_ID_MUSICS_BY_SEARCH, "random");
    }

    public static boolean isIndexPlayable(int index, List<MediaSession.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }

    public static boolean equals(List<MediaSession.QueueItem> list1,
                                 List<MediaSession.QueueItem> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getQueueId() != list2.get(i).getQueueId()) {
                return false;
            }
            if (!TextUtils.equals(list1.get(i).getDescription().getMediaId(),
                    list2.get(i).getDescription().getMediaId())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isQueueItemPlaying(Context context,
                                             MediaSession.QueueItem queueItem) {

        MediaController controller = ((FragmentActivity) context).getMediaController();
        if (controller != null && controller.getPlaybackState() != null) {
            long currentPlayingQueueId = controller.getPlaybackState().getActiveQueueItemId();
            String currentPlayingMediaId = controller.getMetadata().getDescription()
                    .getMediaId();
            String itemMusicId = MediaIDHelper.extractMusicIDFromMediaID(
                    queueItem.getDescription().getMediaId());
            if (queueItem.getQueueId() == currentPlayingQueueId
                    && currentPlayingMediaId != null
                    && TextUtils.equals(currentPlayingMediaId, itemMusicId)) {
                return true;
            }
        }
        return false;
    }

}
