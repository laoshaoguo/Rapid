package com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly;

import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicProvider;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.QueueHelper;
import com.zhiyicx.common.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/2/21/17:36
 * @Email Jliuer@aliyun.com
 * @Description 播放队列管理
 */
public class QueueManager {

    private MusicProvider mMusicProvider;

    private MetadataUpdateListener mListener;

    private List<MediaSession.QueueItem> mPlayingQueue;

    private List<MediaSession.QueueItem> mCacheQueue;

    private int mCurrentIndex;

    public QueueManager(@NonNull MusicProvider musicProvider,
                        @NonNull MetadataUpdateListener listener) {
        this.mMusicProvider = musicProvider;
        this.mListener = listener;

        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSession.QueueItem>());
        mCurrentIndex = 0;
    }

    public boolean isSameBrowsingCategory(@NonNull String mediaId) {
        String[] newBrowseHierarchy = MediaIDHelper.getHierarchy(mediaId);
        MediaSession.QueueItem current = getCurrentMusic();
        if (current == null) {
            return false;
        }
        String[] currentBrowseHierarchy = MediaIDHelper.getHierarchy(
                current.getDescription().getMediaId());

        return Arrays.equals(newBrowseHierarchy, currentBrowseHierarchy);
    }

    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }

    public boolean setCurrentQueueItem(long queueId) {
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, queueId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean setCurrentQueueItem(String mediaId) {
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean skipQueuePosition(int amount) {
        int index = mCurrentIndex + amount;
        if (index < 0) {
            index = mPlayingQueue.size() - 1;
        } else {
            index %= mPlayingQueue.size();
        }
        if (!QueueHelper.isIndexPlayable(index, mPlayingQueue)) {
            return false;
        }
        mCurrentIndex = index;
        return true;
    }

    public boolean setQueueFromSearch(String query, Bundle extras) {
        List<MediaSession.QueueItem> queue =
                QueueHelper.getPlayingQueueFromSearch(query, extras, mMusicProvider);
        setCurrentQueue("search_queue_title", queue);
        updateMetadata();
        return queue != null && !queue.isEmpty();
    }

    public void setRandomQueue() {
        setCurrentQueue("random_queue_title",
                QueueHelper.getRandomQueue(mMusicProvider));
        updateMetadata();
    }

    public void setRandomQueue(String mediaId) {
        setCurrentQueue("random_queue_title",
                QueueHelper.getPlayingQueue(mediaId, mMusicProvider), mediaId);
        updateMetadata();
    }

    public void setNormalQueue(String mediaId) {
        setCurrentQueue("normal_queue_title",
                mCacheQueue, mediaId);
        updateMetadata();
    }

    public void setQueueFromMusic(String mediaId) {
        boolean canReuseQueue = false;
        if (isSameBrowsingCategory(mediaId)) {
            canReuseQueue = setCurrentQueueItem(mediaId);
        }
        if (!canReuseQueue) {
            String queueTitle = "browse_musics_by_genre_subtitle";
            mCacheQueue = QueueHelper.getPlayingQueue(mediaId, mMusicProvider);
            setCurrentQueue(queueTitle, QueueHelper.getPlayingQueue(mediaId, mMusicProvider),
                    mediaId);
        }
        updateMetadata();
    }

    public MediaSession.QueueItem getCurrentMusic() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex);
    }

    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }

    protected void setCurrentQueue(String title, List<MediaSession.QueueItem> newQueue) {
        setCurrentQueue(title, newQueue, null);
    }

    protected void setCurrentQueue(String title, List<MediaSession.QueueItem> newQueue,
                                   String initialMediaId) {
        Collections.reverse(newQueue);
        mPlayingQueue = newQueue;
        int index = 0;
        if (initialMediaId != null) {
            index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
        mListener.onQueueUpdated(title, newQueue);
    }

    public void updateMetadata() {
        MediaSession.QueueItem currentMusic = getCurrentMusic();
        if (currentMusic == null) {
            mListener.onMetadataRetrieveError();
            return;
        }
        if (currentMusic.getDescription() == null) {
            mListener.onMetadataRetrieveError();
            return;
        }
        final String musicId = MediaIDHelper.extractMusicIDFromMediaID(
                currentMusic.getDescription().getMediaId());
        MediaMetadata metadata = mMusicProvider.getMusic(musicId);
        if (metadata == null) {
            mListener.onMetadataRetrieveError();
            LogUtils.e("Invalid musicId " + musicId);
            return;
        }

        mListener.onMetadataChanged(metadata);

        if (metadata.getDescription().getIconBitmap() == null &&
                metadata.getDescription().getIconUri() != null) {
            String albumUri = metadata.getDescription().getIconUri().toString();

        }
    }

    public MusicProvider getMusicProvider() {
        return mMusicProvider;
    }

    public void setMusicProvider(MusicProvider musicProvider) {
        mMusicProvider = musicProvider;
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadata metadata);

        void onMetadataRetrieveError();

        void onCurrentQueueIndexUpdated(int queueIndex);

        void onQueueUpdated(String title, List<MediaSession.QueueItem> newQueue);
    }
}
