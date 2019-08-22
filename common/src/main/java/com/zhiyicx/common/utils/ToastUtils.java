package com.zhiyicx.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhiyicx.common.base.BaseApplication;


public class ToastUtils {
    private static Toast toastText;
    private static Toast toastView;

    public static void showToast(int resID) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(String text) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, text);
    }

    public static void showToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_SHORT, text);
    }

    public static void showLongToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_LONG, resID);
    }

    public static void showLongToast(int resID) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_LONG, resID);
    }

    public static void showLongToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_LONG, text);
    }

    public static void showLongToast(String text) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_LONG, text);
    }

    public static void showToast(Context ctx, int duration, int resID) {
        showToast(ctx, duration, ctx.getString(resID));
    }

    /**
     * Toast一个图片
     */
    public static Toast showToastImage(Context ctx, int resID) {
        final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null)
            mNextView.setBackgroundResource(resID);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    /**
     * Toast防止时间累积
     */
    public static void showToast(final Context ctx, final int duration,
                                 final String text) {
        if (toastText != null) {
            toastText.setText(text);
            toastText.show();
        } else {
            toastText = Toast.makeText(ctx, text, duration);
            toastText.setGravity(Gravity.CENTER, 0, 0);
            toastText.show();
        }
    }

    /**
     * toast一个自定义布局
     */
    public static void showToast(View v, Context context) {
        if (toastView != null) {
            toastView.setView(v);
            toastView.setDuration(Toast.LENGTH_SHORT);
            toastView.show();
        } else {
            toastView = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toastView.setView(v);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.setDuration(Toast.LENGTH_SHORT);
            toastView.show();
        }
    }

}
