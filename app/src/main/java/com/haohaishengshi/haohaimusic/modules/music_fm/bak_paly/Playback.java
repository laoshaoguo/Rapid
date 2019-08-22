package com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly;


import android.media.session.MediaSession;

/**
 * @Author Jliuer
 * @Date 2017/2/21/14:32
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放功能管理
 */
public interface Playback {

    void start();

    void stop(boolean notifyListeners);

    void setState(int state);

    int getState();

    boolean isConnected();

    boolean isPlaying();

    int getCurrentStreamPosition();

    void setCurrentStreamPosition(int pos);

    void updateLastKnownStreamPosition();

    void play(MediaSession.QueueItem item);

    void pause();

    void seekTo(int position);

    void setCurrentMediaId(String mediaId);

    String getCurrentMediaId();

    interface Callback {

        void onCompletion();

        void onPlaybackStatusChanged(int state);

        void onError(String error, int state);

        void setCurrentMediaId(String mediaId);

        void onBuffering(int percent);
    }

    void setCallback(Callback callback);
}
