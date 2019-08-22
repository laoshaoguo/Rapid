package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;

import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.LocalPlayback;
import com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager;
import com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.QueueManager;
import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicDataConvert;
import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicProvider;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaNotificationManager;
import com.zhiyicx.common.utils.log.LogUtils;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_SEND_MUSIC_CACHE_PROGRESS;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ACTION_BUNDLE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ID;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_EMPTY_ROOT;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_ROOT;


/**
 * @Author Jliuer
 * @Date 2017/2/21/19:00
 * @Email Jliuer@aliyun.com
 * @Description 音乐的后台Service
 */
public class MusicPlayService extends MediaBrowserService implements
        PlaybackManager.PlaybackServiceCallback {


    public static final String EXTRA_CONNECTED_CAST = "com.zhiyicx.thinksnsplus.CAST_NAME";
    public static final String ACTION_CMD = "com.zhiyicx.thinksnsplus.ACTION_CMD";
    public static final String CMD_NAME = "CMD_NAME";
    public static final String CMD_PAUSE = "CMD_PAUSE";
    public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";
    private static final int STOP_DELAY = 30000;

    private MusicProvider mMusicProvider;
    private PlaybackManager mPlaybackManager;

    private MediaSession mSession;
    private MediaNotificationManager mMediaNotificationManager;
    private final DelayedStopHandler mDelayedStopHandler = new DelayedStopHandler(this);
    private QueueManager mQueueManager;
    private LocalPlayback mLocalPlayback;

    @Override
    public void onCreate() {
        super.onCreate();
        mSession = new MediaSession(this, "MusicPlayService");
        setSessionToken(mSession.getSessionToken());
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MusicDetailActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 99 /*request code*/,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mSession.setSessionActivity(pi);
        mMusicProvider = new MusicProvider();
        mMusicProvider.retrieveMediaAsync(null /* Callback */);
        configMusicProvider(mMusicProvider);

        try {
            mMediaNotificationManager = new MediaNotificationManager(this);
        } catch (RemoteException e) {
            throw new IllegalStateException("Could not create a MediaNotificationManager", e);
        }
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        if (startIntent != null) {
            String action = startIntent.getAction();
            String command = startIntent.getStringExtra(CMD_NAME);
            if (ACTION_CMD.equals(action)) {
                if (CMD_PAUSE.equals(command)) {
                    mPlaybackManager.handlePauseRequest();
                } else if (CMD_STOP_CASTING.equals(command)) {

                }
            } else {
//                MediaButtonReceiver.handleIntent(mSession, startIntent);
            }
        }
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mPlaybackManager.handleStopRequest(null);
        mMediaNotificationManager.stopNotification();
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mSession.release();
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid,
                                 Bundle rootHints) {
        return new BrowserRoot(MEDIA_ID_ROOT, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentMediaId, @NonNull Result<List<MediaBrowser.MediaItem>> result) {
        if (MEDIA_ID_EMPTY_ROOT.equals(parentMediaId)) {
            result.sendResult(new ArrayList<>());
        } else if (mMusicProvider.isInitialized()) {
            List<MediaBrowser.MediaItem> data;
            data = mMusicProvider.getChildren(parentMediaId, getResources());
            result.sendResult(data);
        } else {
            result.detach();
            mMusicProvider.retrieveMediaAsync(success -> {
                List<MediaBrowser.MediaItem> data;
                data = mMusicProvider.getChildren(parentMediaId, getResources());
                result.sendResult(data);
            });
        }
    }

    @Override
    public void onPlaybackStart() {
        mSession.setActive(true);
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        startService(new Intent(getApplicationContext(), MusicPlayService.class));
    }

    @Override
    public void onPlaybackStop() {
        mSession.setActive(false);
        mDelayedStopHandler.removeCallbacksAndMessages(null);
        mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);
        stopForeground(true);
    }

    @Override
    public void onNotificationRequired() {
//        mMediaNotificationManager.startNotification();
    }

    @Override
    public void onPlaybackStateUpdated(PlaybackState newState) {
        mSession.setPlaybackState(newState);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        EventBus.getDefault().post(percent, EVENT_SEND_MUSIC_CACHE_PROGRESS);
    }

    @Override
    public void onCustomAction(String action, Bundle extras) {
        LogUtils.d("onCustomAction");
        String tym = extras.getString(MUSIC_ID, MUSIC_ID);
        MusicAlbumDetailsBean musicAblum = (MusicAlbumDetailsBean) extras.getSerializable(MUSIC_ACTION_BUNDLE);
        if (musicAblum != null) {
            MusicProvider newMusicProvider = new MusicProvider(new MusicDataConvert(musicAblum));
            newMusicProvider.retrieveMediaAsync(success -> {
                mMusicProvider = newMusicProvider;
                mQueueManager.setMusicProvider(mMusicProvider);
                mLocalPlayback.setMusicProvider(mMusicProvider);
                if (!tym.equals(MUSIC_ID)) {// 加入播放队列
                    mQueueManager.setQueueFromMusic(tym);
                    mPlaybackManager.handlePlayRequest();
                }
            });
//            if (newMusicProvider.isInitialized()){
//                mMusicProvider = newMusicProvider;
//                mQueueManager.setMusicProvider(mMusicProvider);
//                mLocalPlayback.setMusicProvider(mMusicProvider);
//                if (!tym.equals(MUSIC_ID)) {// 加入播放队列
//                    mQueueManager.setQueueFromMusic(tym);
//                }
//            }

        }

    }

    private static class DelayedStopHandler extends Handler {
        private final WeakReference<MusicPlayService> mWeakReference;

        private DelayedStopHandler(MusicPlayService service) {
            mWeakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            MusicPlayService service = mWeakReference.get();
            if (service != null && service.mPlaybackManager.getPlayback() != null) {
                if (service.mPlaybackManager.getPlayback().isPlaying()) {
                    return;
                }
                service.stopSelf();
            }
        }
    }

    private void configMusicProvider(MusicProvider provider) {

        mQueueManager = new QueueManager(provider,
                new QueueManager.MetadataUpdateListener() {
                    @Override
                    public void onMetadataChanged(MediaMetadata metadata) {
                        if (metadata == null) {
                            onMetadataRetrieveError();
                            return;
                        }
                        mSession.setMetadata(metadata);
                    }

                    @Override
                    public void onMetadataRetrieveError() {
                        mPlaybackManager.updatePlaybackState(
                                "error_no_metadata");
                    }

                    @Override
                    public void onCurrentQueueIndexUpdated(int queueIndex) {
                        LogUtils.d("mCurrentPosition:::handlePlayRequest::onCurrentQueueIndexUpdated");
//                        mPlaybackManager.handlePlayRequest();
                    }

                    @Override
                    public void onQueueUpdated(String title,
                                               List<MediaSession.QueueItem> newQueue) {
                        mSession.setQueue(newQueue);
                        mSession.setQueueTitle(title);
                    }
                });
        mLocalPlayback = new LocalPlayback(this, provider);
        mPlaybackManager = new PlaybackManager(this, mQueueManager,
                mLocalPlayback);
        mPlaybackManager.updatePlaybackState(null);
        mSession.setCallback(mPlaybackManager.getMediaSessionCallback());
        AppApplication.setmQueueManager(mQueueManager);
        AppApplication.setPlaybackManager(mPlaybackManager);
    }

}

