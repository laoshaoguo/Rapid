package com.zhiyicx.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;

import com.zhiyicx.common.base.BaseApplication;

/**
 * @author LiuChao
 * @describe 一些和Ui相关的工具类方法
 * @date 2017/1/10
 * @contact email:450127106@qq.com
 */

public class UIUtils {
    /**
     * TextView设置下划线
     */
    public static void setBottomDivider(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * Textview或者button设置Drawable
     */
    public static Drawable getCompoundDrawables(Context context, int imgRsID) {
        if (imgRsID == 0) {
            return null;
        }
        Drawable drawable = ContextCompat.getDrawable(context, imgRsID);
        if (drawable == null) {
            return null;
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static Drawable getCompoundDrawables(Context context, int imgRsID,int color) {
        if (imgRsID == 0) {
            return null;
        }
        Drawable drawable = ContextCompat.getDrawable(context, imgRsID);
        if (drawable == null) {
            return null;
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);

        return wrappedDrawable;
    }

    //获取某一个百分比间的颜色,radio取值[0,1]
    public static int getColor(int mStartColor,int mEndColor,float radio) {
        int redStart = Color.red(mStartColor);
        int blueStart = Color.blue(mStartColor);
        int greenStart = Color.green(mStartColor);
        int redEnd = Color.red(mEndColor);
        int blueEnd = Color.blue(mEndColor);
        int greenEnd = Color.green(mEndColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255,red, greed, blue);
    }


    /**
     * 通过资源名称查找resource
     */
    public static int getResourceByName(String name, String type, Context context) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */

    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
