package com.haohaishengshi.haohaimusic.modules.music_fm.music_helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_play.MusicPlayService;

/**
 * @Author Jliuer
 * @Date 2017/2/21/14:11
 * @Email Jliuer@aliyun.com
 * @Description 通知栏界面
 */
public class MediaNotificationManager extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 412;
    private static final int REQUEST_CODE = 100;

    public static final String ACTION_PAUSE = "com.zhiyicx.thinksnsplus.pause";
    public static final String ACTION_PLAY = "com.zhiyicx.thinksnsplus.play";
    public static final String ACTION_PREV = "com.zhiyicx.thinksnsplus.prev";
    public static final String ACTION_NEXT = "com.zhiyicx.thinksnsplus.next";
    public static final String ACTION_STOP_CASTING = "com.zhiyicx.thinksnsplus.stop_cast";

    private final MusicPlayService mService;

    private MediaSession.Token mSessionToken;

    private MediaController mController;

    private MediaController.TransportControls mTransportControls;

    private PlaybackState mPlaybackState;

    private MediaMetadata mMetadata;

    private final NotificationManagerCompat mNotificationManager;

    private final PendingIntent mPauseIntent;
    private final PendingIntent mPlayIntent;
    private final PendingIntent mPreviousIntent;
    private final PendingIntent mNextIntent;

    private final int mNotificationColor;

    private boolean mStarted = false;

    public MediaNotificationManager(MusicPlayService service) throws RemoteException {
        mService = service;
        updateSessionToken();

        mNotificationColor = getThemeColor(mService, R.attr.colorPrimary,
                Color.DKGRAY);

        mNotificationManager = NotificationManagerCompat.from(service);

        String pkg = mService.getPackageName();
        mPauseIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPlayIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPreviousIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mNextIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);

        mNotificationManager.cancelAll();
    }

    public void startNotification() {
        if (!mStarted) {

            mMetadata = mController.getMetadata();

            mPlaybackState = mController.getPlaybackState();

            Notification notification = createNotification();
            if (notification != null) {
                mController.registerCallback(mCb);
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_NEXT);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PREV);
                filter.addAction(ACTION_STOP_CASTING);
                mService.registerReceiver(this, filter);

                mService.startForeground(NOTIFICATION_ID, notification);
                mStarted = true;
            }
        }
    }

    public void stopNotification() {
        if (mStarted) {
            mStarted = false;
            mController.unregisterCallback(mCb);
            try {
                mNotificationManager.cancel(NOTIFICATION_ID);
                mService.unregisterReceiver(this);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            mService.stopForeground(true);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        switch (action) {
            case ACTION_PAUSE:
                mTransportControls.pause();
                break;
            case ACTION_PLAY:
                mTransportControls.play();
                break;
            case ACTION_NEXT:
                mTransportControls.skipToNext();
                break;
            case ACTION_PREV:
                mTransportControls.skipToPrevious();
                break;
            case ACTION_STOP_CASTING:
                Intent i = new Intent(context, MusicPlayService.class);
                i.setAction(MusicPlayService.ACTION_CMD);
                i.putExtra(MusicPlayService.CMD_NAME, MusicPlayService.CMD_STOP_CASTING);
                mService.startService(i);
                break;
            default:
        }
    }

    private void updateSessionToken() throws RemoteException {
        MediaSession.Token freshToken = mService.getSessionToken();
        if (mSessionToken == null && freshToken != null ||
                mSessionToken != null && !mSessionToken.equals(freshToken)) {
            if (mController != null) {
                mController.unregisterCallback(mCb);
            }
            mSessionToken = freshToken;
            if (mSessionToken != null) {
                mController = new MediaController(mService, mSessionToken);
                mTransportControls = mController.getTransportControls();
                if (mStarted) {
                    mController.registerCallback(mCb);
                }
            }
        }
    }

    private PendingIntent createContentIntent(MediaDescription description) {
        Intent openUI = new Intent(mService, MusicDetailActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        openUI.putExtra(MusicDetailActivity.EXTRA_START_FULLSCREEN, true);
        if (description != null) {
            openUI.putExtra(MusicDetailActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION, description);
        }
        return PendingIntent.getActivity(mService, REQUEST_CODE, openUI,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private final MediaController.Callback mCb = new MediaController.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackState state) {
            mPlaybackState = state;
            if (state.getState() == PlaybackState.STATE_STOPPED ||
                    state.getState() == PlaybackState.STATE_NONE) {
                stopNotification();
            } else {
                Notification notification = createNotification();
                if (notification != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadata metadata) {
            mMetadata = metadata;
            Notification notification = createNotification();
            if (notification != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            try {
                updateSessionToken();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Notification createNotification() {
        if (mMetadata == null || mPlaybackState == null) {
            return null;
        }

        Notification.Builder notificationBuilder = new Notification.Builder(mService);
        int playPauseButtonPosition = 0;

        if ((mPlaybackState.getActions() & PlaybackState.ACTION_SKIP_TO_PREVIOUS) != 0) {
            notificationBuilder.addAction(R.mipmap.music_ico_last,
                    "pre", mPreviousIntent);
            playPauseButtonPosition = 1;
        }

        addPlayPauseAction(notificationBuilder);
        if ((mPlaybackState.getActions() & PlaybackState.ACTION_SKIP_TO_NEXT) != 0) {
            notificationBuilder.addAction(R.mipmap.music_ico_next,
                    "next", mNextIntent);
        }

        MediaDescription description = mMetadata.getDescription();

        String fetchArtUrl = null;
        Bitmap art = null;
        if (description.getIconUri() != null) {
            String artUrl = description.getIconUri().toString();
        }

        notificationBuilder
                .setStyle(new Notification.MediaStyle()
                        .setShowActionsInCompactView(playPauseButtonPosition)
                        .setMediaSession(mSessionToken))
                .setColor(mNotificationColor)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                .setContentIntent(createContentIntent(description))
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setLargeIcon(art);

        if (mController != null && mController.getExtras() != null) {
            String castName = mController.getExtras().getString(MusicPlayService
                    .EXTRA_CONNECTED_CAST);
            if (castName != null) {
            }
        }

        setNotificationPlaybackState(notificationBuilder);
        if (fetchArtUrl != null) {
            fetchBitmapFromURLAsync(fetchArtUrl, notificationBuilder);
        }

        return notificationBuilder.build();
    }

    private void addPlayPauseAction(Notification.Builder builder) {
        String label;
        int icon;
        PendingIntent intent;
        if (mPlaybackState.getState() == PlaybackState.STATE_PLAYING) {
            label = "pause";
            icon = R.mipmap.music_ico_stop;
            intent = mPauseIntent;
        } else {
            label = "play";
            icon = R.mipmap.music_ico_play;
            intent = mPlayIntent;
        }
        builder.addAction(new Notification.Action(icon, label, intent));
    }

    private void setNotificationPlaybackState(Notification.Builder builder) {
        if (mPlaybackState == null || !mStarted) {
            mService.stopForeground(true);
            return;
        }
        if (mPlaybackState.getState() == PlaybackState.STATE_PLAYING
                && mPlaybackState.getPosition() >= 0) {
            builder
                    .setWhen(System.currentTimeMillis() - mPlaybackState.getPosition())
                    .setShowWhen(true)
                    .setUsesChronometer(true);
        } else {
            builder
                    .setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false);
        }

        builder.setOngoing(mPlaybackState.getState() == PlaybackState.STATE_PLAYING);
    }

    private void fetchBitmapFromURLAsync(final String bitmapUrl,
                                         final Notification.Builder builder) {

    }

    public int getThemeColor(Context context, int attribute, int defaultColor) {
        int themeColor = 0;
        String packageName = context.getPackageName();
        try {
            Context packageContext = context.createPackageContext(packageName, 0);
            ApplicationInfo applicationInfo =
                    context.getPackageManager().getApplicationInfo(packageName, 0);
            packageContext.setTheme(applicationInfo.theme);
            Resources.Theme theme = packageContext.getTheme();
            TypedArray ta = theme.obtainStyledAttributes(new int[]{attribute});
            themeColor = ta.getColor(0, defaultColor);
            ta.recycle();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return themeColor;
    }
}
