package com.zhiyicx.baseproject.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * @Author Jliuer
 * @Date 17/11/07 11:28
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放提示窗口
 */
public class WindowUtils {
    private static final String LOG_TAG = "WindowUtils";
    private static Bundle sMusicAlbumDetailsBean;

    private static Boolean isShown = false;
    private static Boolean isPause = false;
    private static AblumHeadInfo sAblumHeadInfo;

    private static CopyOnWriteArrayList<OnWindowDismisslistener> sDismisslistenerLists = new CopyOnWriteArrayList<>();

    public interface OnWindowDismisslistener {
        void onMusicWindowDismiss();
    }

    public static void setWindowDismisslistener(OnWindowDismisslistener windowDismisslistener) {
        sDismisslistenerLists.add(windowDismisslistener);
    }

    public static void removeWindowDismisslistener(OnWindowDismisslistener windowDismisslistener) {
        sDismisslistenerLists.remove(windowDismisslistener);
    }

    public static void showPopupWindow(final Context context) {
        isShown = true;
    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        Observable.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        try {
                            isShown = false;
                            for (OnWindowDismisslistener windowDismisslistener : sDismisslistenerLists) {
                                if (windowDismisslistener != null) {
                                    windowDismisslistener.onMusicWindowDismiss();
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .subscribe();

    }

    public static Boolean getIsShown() {
        return isShown;
    }

    public static Boolean getIsPause() {
        return isPause;
    }

    public static void setIsPause(Boolean isPause) {
        WindowUtils.isPause = isPause;
    }

    public static void changeToWhiteIcon() {

    }

    public static void changeToBlackIcon() {

    }

    public static Bundle getMusicAlbumDetailsBean() {
        return sMusicAlbumDetailsBean;
    }

    public static void setMusicAlbumDetailsBean(Bundle musicAlbumDetailsBean) {
        sMusicAlbumDetailsBean = musicAlbumDetailsBean;
    }

    public static AblumHeadInfo getAblumHeadInfo() {
        return sAblumHeadInfo;
    }

    public static void setAblumHeadInfo(AblumHeadInfo ablumHeadInfo) {
        sAblumHeadInfo = ablumHeadInfo;
    }

    public static class AblumHeadInfo {
        int listenCount;
        int shareCount;
        int commentCount;
        int likeCount;
        int ablumId;

        public int getAblumId() {
            return ablumId;
        }

        public void setAblumId(int ablumId) {
            this.ablumId = ablumId;
        }

        public int getListenCount() {
            return listenCount;
        }

        public void setListenCount(int listenCount) {
            this.listenCount = listenCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }
    }
}
