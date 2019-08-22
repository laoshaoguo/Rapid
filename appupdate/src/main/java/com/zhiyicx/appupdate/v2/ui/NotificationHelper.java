package com.zhiyicx.appupdate.v2.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.zhiyicx.appupdate.R;
import com.zhiyicx.appupdate.core.PermissionDialogActivity;
import com.zhiyicx.appupdate.core.VersionFileProvider;
import com.zhiyicx.appupdate.utils.ALog;
import com.zhiyicx.appupdate.v2.builder.DownloadBuilder;
import com.zhiyicx.appupdate.v2.builder.NotificationBuilder;

import java.io.File;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by allenliu on 2018/1/19.
 */

public class NotificationHelper {
    private DownloadBuilder versionBuilder;
    private Context context;
    NotificationCompat.Builder notificationBuilder = null;
    NotificationManager manager = null;
    private boolean isDownloadSuccess=false,isFailed=false;
    private int currentProgress = 0;
    private String contentText;
    private  final int NOTIFICATION_ID=0;
    public NotificationHelper(Context context, DownloadBuilder builder) {
        this.context = context;
        this.versionBuilder = builder;
        currentProgress = 0;
    }

    /**
     * update notification progress
     *
     * @param progress the progress of notification
     */
    public void updateNotification(int progress) {
        if (versionBuilder.isShowNotification()) {
            if ((progress - currentProgress) > 5&&!isDownloadSuccess&&!isFailed) {
                notificationBuilder.setContentIntent(null);
                notificationBuilder.setContentText(String.format(contentText, progress));
                notificationBuilder.setProgress(100, progress, false);
                manager.notify(NOTIFICATION_ID, notificationBuilder.build());
                currentProgress = progress;
            }
        }
    }

    /**
     * show notification
     */
    public void showNotification() {
        isDownloadSuccess=false;
        isFailed=false;
        if (versionBuilder.isShowNotification()) {
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationBuilder = createNotification();
            manager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    /**
     * show download success notification
     */
    public void showDownloadCompleteNotifcation(File file) {
        isDownloadSuccess=true;
        if (!versionBuilder.isShowNotification()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = VersionFileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            ALog.e(context.getPackageName() + "");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        //设置intent的类型
        i.setDataAndType(uri,
                "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentText(context.getString(R.string.versionchecklib_download_finish));
        notificationBuilder.setProgress(100, 100, false);
        manager.cancelAll();
        manager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void showDownloadFailedNotification() {
        isDownloadSuccess=false;
        isFailed=true;
        if (versionBuilder.isShowNotification()) {
            Intent intent = new Intent(context, PermissionDialogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setContentText(context.getString(R.string.versionchecklib_download_fail));
            notificationBuilder.setProgress(100, 0, false);
            manager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private NotificationCompat.Builder createNotification() {
        final String CHANNEL_ID = "0", CHANNEL_NAME = "ALLEN_NOTIFICATION";
        NotificationCompat.Builder builder = null;
        NotificationBuilder libNotificationBuilder = versionBuilder.getNotificationBuilder();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        builder = new NotificationCompat.Builder(context);
//        builder.setChannelId(CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setSmallIcon(versionBuilder.getNotificationBuilder().getIcon());
        //set content title
        String contentTitle = context.getString(R.string.app_name);
        if (libNotificationBuilder.getContentTitle() != null) {
            contentTitle = libNotificationBuilder.getContentTitle();
        }
        builder.setContentTitle(contentTitle);
        //set ticker
        String ticker = context.getString(R.string.versionchecklib_downloading);
        if (libNotificationBuilder.getTicker() != null) {
            ticker = libNotificationBuilder.getTicker();
        }
        builder.setTicker(ticker);
        //set content text
        contentText = context.getString(R.string.versionchecklib_download_progress);
        if (libNotificationBuilder.getContentText() != null) {
            contentText = libNotificationBuilder.getContentText();
        }
        builder.setContentText(String.format(contentText, 0));

        if (libNotificationBuilder.isRingtone()) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        }

        return builder;
    }

    public void onDestroy() {
        if(manager!=null) {
            manager.cancel(NOTIFICATION_ID);
        }
    }
}
