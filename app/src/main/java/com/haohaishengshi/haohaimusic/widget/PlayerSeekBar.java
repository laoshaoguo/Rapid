package com.haohaishengshi.haohaimusic.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

import com.haohaishengshi.haohaimusic.R;


/**
 * @Author Jliuer
 * @Date 2017/3/8/14:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PlayerSeekBar extends AppCompatSeekBar {

    private boolean drawLoading = false;
    private int degree = 0;
    private Matrix matrix = new Matrix();
    private Bitmap loading = BitmapFactory.decodeResource(getResources(), R.mipmap
            .default_white000);
    private Drawable drawable;

    public PlayerSeekBar(Context context) {
        super(context);
    }

    public PlayerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLoading(boolean loading) {
        drawLoading = loading;
        if (drawLoading) {
            setProgress(0);
            setSecondaryProgress(0);
            invalidate();
        }
    }

    @Override
    public void setThumb(Drawable thumb) {
        Rect localRect = null;
        if (drawable != null) {
            localRect = drawable.getBounds();
        }
        super.setThumb(drawable);
        drawable = thumb;
        if ((localRect != null) && (drawable != null)) {
            drawable.setBounds(localRect);
        }
    }

    public void setThumb(int res) {
        setThumb(getContext().getResources().getDrawable(res));
    }

    @Override
    public Drawable getThumb() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getThumb();
        }
        return drawable;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawLoading) {
            canvas.save();
            degree = ((int) (degree + 3.0F));
            degree %= 360;
            matrix.reset();
            float scale = 0.6f;
            if (drawable != null) {
                float result = (float) drawable.getIntrinsicWidth() / (float) loading.getWidth();
                scale = (float) (Math.round(result * 100)) / 100;// 取两位小数
            }
            matrix.setScale(scale, scale, loading.getWidth() / 2, loading.getHeight() / 2);
            matrix.postRotate(degree, loading.getWidth() / 2, loading.getHeight() / 2);
            canvas.translate(getPaddingLeft() + getThumb().getBounds().left + getThumb().getBounds
                            ().width() / 2 - loading.getWidth() / 2 - getThumbOffset(),
                    getPaddingTop() + getThumb().getBounds().top + getThumb().getBounds().height()
                            / 2 - loading.getHeight() / 2);
            canvas.drawBitmap(loading, matrix, null);
            canvas.restore();
            invalidate();
        }

    }
}
