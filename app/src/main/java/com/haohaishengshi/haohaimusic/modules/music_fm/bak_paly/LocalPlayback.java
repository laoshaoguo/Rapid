package com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.text.TextUtils;

import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicProvider;
import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicProviderSource;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_play.MusicPlayService;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.media.MediaPlayer.OnCompletionListener;
import static android.media.MediaPlayer.OnErrorListener;
import static android.media.MediaPlayer.OnPreparedListener;
import static android.media.MediaPlayer.OnSeekCompleteListener;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_SEND_MUSIC_LOAD;

/**
 * @Author Jliuer
 * @Date 2017/2/21/16:25
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放器功能实现类
 */
public class LocalPlayback implements Playback, AudioManager.OnAudioFocusChangeListener,
        OnCompletionListener, OnErrorListener, OnPreparedListener, OnSeekCompleteListener,
        MediaPlayer.OnBufferingUpdateListener {

    /**
     * 音量控制
     */
    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;

    /**
     * 播放焦点处理
     */
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;

    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;

    private static final int AUDIO_FOCUSED = 2;

    private final Context mContext;

    /**
     * wifi持有锁
     */
    private final WifiManager.WifiLock mWifiLock;

    /**
     * 播放状态
     */
    private int mState;

    /**
     * 重新获得焦点
     */
    private boolean mPlayOnFocusGain;

    private Callback mCallback;

    private MusicProvider mMusicProvider;

    private volatile boolean mAudioNoisyReceiverRegistered;

    private volatile int mCurrentPosition;

    private volatile String mCurrentMediaId;

    private int mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;

    private final AudioManager mAudioManager;

    private MediaPlayer mMediaPlayer;

    private boolean isCached;

    private final IntentFilter mAudioNoisyIntentFilter =
            new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    // Audio输出通道切换的事件的捕获与处理
    private final BroadcastReceiver mAudioNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (isPlaying()) {
                    Intent i = new Intent(context, MusicPlayService.class);
                    i.setAction(MusicPlayService.ACTION_CMD);
                    i.putExtra(MusicPlayService.CMD_NAME, MusicPlayService.CMD_PAUSE);
                    mContext.startService(i);
                } else {
                }
            }
        }
    };

    public LocalPlayback(Context context, MusicProvider musicProvider) {
        this.mContext = context;
        this.mMusicProvider = musicProvider;
        this.mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        //持有wifi锁，避免后台wifi休眠
        this.mWifiLock = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock");
        this.mState = PlaybackState.STATE_NONE;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop(boolean notifyListeners) {
        mState = PlaybackState.STATE_STOPPED;
        if (notifyListeners && mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
        mCurrentPosition = getCurrentStreamPosition();
        //放弃焦点 取消监听 释放资源
        giveUpAudioFocus();
        unregisterAudioNoisyReceiver();

        relaxResources(true);
    }

    @Override
    public void setState(int state) {
        this.mState = state;
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isPlaying() {
        return mPlayOnFocusGain || (mMediaPlayer != null && mMediaPlayer.isPlaying());
    }

    @Override
    public int getCurrentStreamPosition() {
        return mMediaPlayer != null ?
                mMediaPlayer.getCurrentPosition() : mCurrentPosition;
    }

    @Override
    public void updateLastKnownStreamPosition() {
        if (mMediaPlayer != null) {
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public void play(MediaSession.QueueItem item) {
        mPlayOnFocusGain = true;
        WindowUtils.setIsPause(false);
        tryToGetAudioFocus();
        registerAudioNoisyReceiver();
        String mediaId = item.getDescription().getMediaId();
        boolean mediaHasChanged = !TextUtils.equals(mediaId, mCurrentMediaId);

        if (mediaHasChanged) {
            mCurrentPosition = 0;
            mCurrentMediaId = mediaId;
        } else {
            LogUtils.d("mediaHasChanged:::没有切歌");
//            return;
        }

        // 防止未准备好的时候 重新播放该歌曲 mState == PlaybackState.STATE_PAUSED &&
        if (mState == PlaybackState.STATE_PAUSED && !mediaHasChanged && mMediaPlayer != null) {
            LogUtils.d("mCurrentPosition:::没有切歌");
            configMediaPlayerState();
        } else if (mState == PlaybackState.STATE_PAUSED && mediaHasChanged && mMediaPlayer != null) {
            // 这里是 错误后自动播放，如果暂停，则停止自动播放
            stop(true);
        } else {
            mState = PlaybackState.STATE_STOPPED;
            relaxResources(false);
            if (item.getDescription() == null || TextUtils.isEmpty(item.getDescription().getMediaId())) {
                onError(null, 0, 0);
                return;
            }
            MediaMetadata track = mMusicProvider.getMusic(
                    MediaIDHelper.extractMusicIDFromMediaID(item.getDescription().getMediaId()));
            if (track == null) {
                onError(null, 0, 0);
                return;
            }
            String source = track.getString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE);
            if (source != null) {
                source = source.replaceAll(" ", "%20");
            }

            try {
                createMediaPlayerIfNeeded();

                mState = PlaybackState.STATE_BUFFERING;
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build();
                mMediaPlayer.setAudioAttributes(attributes);
                String proxyUrl = null;
//                try {
//                    proxyUrl = AppApplication.getProxy().getProxyUrl(source);
//                } catch (Exception e) {
//                    e.printStackTrace();
                    proxyUrl = source;
//                }
                LogUtils.d(source);
//                isCached = AppApplication.getProxy().isCached(source);
                if (isCached) {
                    mCallback.onBuffering(100);
                    mMediaPlayer.setDataSource(proxyUrl);
                } else {
                    Map<String, String> header = new HashMap<>();
                    header.put("Authorization", " Bearer " + AppApplication.getmCurrentLoginAuth().getToken());
                    Uri uri = Uri.parse(source);
                    mMediaPlayer.setDataSource(mContext, uri, header);
                }

                mMediaPlayer.prepareAsync();
                EventBus.getDefault().post(-1,
                        EVENT_SEND_MUSIC_LOAD);

                mWifiLock.acquire();

                if (mCallback != null) {
                    mCallback.onPlaybackStatusChanged(mState);
                }

            } catch (IOException ex) {
                if (mCallback != null) {
                    mCallback.onError(ex.getMessage(), PlaybackState.STATE_PAUSED);
                }
            }
        }
    }

    @Override
    public void pause() {
        if (mState == PlaybackState.STATE_PLAYING) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentPosition = mMediaPlayer.getCurrentPosition();
            }
            relaxResources(false);
        }
        mState = PlaybackState.STATE_PAUSED;
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
        unregisterAudioNoisyReceiver();
    }

    @Override
    public void seekTo(int position) {
        if (mMediaPlayer == null) {
            mCurrentPosition = position;
        } else {
            if (mMediaPlayer.isPlaying()) {
                mState = PlaybackState.STATE_BUFFERING;
            }
            registerAudioNoisyReceiver();
            mMediaPlayer.seekTo(position);
            if (mCallback != null) {
                mCallback.onPlaybackStatusChanged(mState);
            }
        }

    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void setCurrentStreamPosition(int pos) {
        this.mCurrentPosition = pos;
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        this.mCurrentMediaId = mediaId;
    }

    @Override
    public String getCurrentMediaId() {
        return mCurrentMediaId;
    }

    private void tryToGetAudioFocus() {
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_FOCUSED;
        } else {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    private void giveUpAudioFocus() {
        if (mAudioManager.abandonAudioFocus(this) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    private void configMediaPlayerState() {
        if (mAudioFocus == AUDIO_NO_FOCUS_NO_DUCK) {
            // 没有焦点并且在播放中
            if (mState == PlaybackState.STATE_PLAYING) {
                pause();
            }
        } else {  // 播放器有焦点
            registerAudioNoisyReceiver();
            if (mAudioFocus == AUDIO_NO_FOCUS_CAN_DUCK) {
                mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK);
            } else {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL);
                }
            }

            if (mPlayOnFocusGain) {
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    if (mCurrentPosition == mMediaPlayer.getCurrentPosition()) {
                        LogUtils.d("mCurrentPosition == mMediaPlayer.start()");
                        mMediaPlayer.start();
                        mState = PlaybackState.STATE_PLAYING;
                    } else {
                        LogUtils.d("mCurrentPosition == mMediaPlayer.seekTo");
                        mMediaPlayer.seekTo(mCurrentPosition);
                        mState = PlaybackState.STATE_BUFFERING;
                    }
                }
                mPlayOnFocusGain = false;
            }
        }
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mAudioFocus = AUDIO_FOCUSED;

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

            boolean canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
            mAudioFocus = canDuck ? AUDIO_NO_FOCUS_CAN_DUCK : AUDIO_NO_FOCUS_NO_DUCK;

            if (mState == PlaybackState.STATE_PLAYING && !canDuck) {
                mPlayOnFocusGain = true;
            }
        }
        configMediaPlayerState();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mCurrentPosition = mp.getCurrentPosition();
        if (mState == PlaybackState.STATE_PAUSED) {
            mMediaPlayer.pause();
            return;
        }
        if (mState == PlaybackState.STATE_BUFFERING) {
            registerAudioNoisyReceiver();
            mMediaPlayer.start();
            mState = PlaybackState.STATE_PLAYING;
        }
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        mCurrentPosition = 0;
        if (mCallback != null) {
            mCallback.onCompletion();
        }
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        if (mState == PlaybackState.STATE_PAUSED) {
            mMediaPlayer.pause();
            return;
        }
        EventBus.getDefault().post(player.getDuration() / 1000,
                EVENT_SEND_MUSIC_LOAD);
        configMediaPlayerState();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mCallback != null) {
            mCallback.onError("MediaPlayer error " + what + " (" + extra + ")", mState);
        }
        mState = PlaybackState.STATE_ERROR;
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (!isCached) {
            mCallback.onBuffering(percent);
        }

    }

    private void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setWakeMode(mContext.getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
        } else {
            mMediaPlayer.reset();
        }
    }

    private void relaxResources(boolean releaseMediaPlayer) {

        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    private void registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            mContext.registerReceiver(mAudioNoisyReceiver, mAudioNoisyIntentFilter);
            mAudioNoisyReceiverRegistered = true;
        }
    }

    private void unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            mContext.unregisterReceiver(mAudioNoisyReceiver);
            mAudioNoisyReceiverRegistered = false;
        }
    }

    public void setMusicProvider(MusicProvider musicProvider) {
        mMusicProvider = musicProvider;
    }
}
