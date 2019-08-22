package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 正方形图片&白色描边
 */
public class GlideMusicBgTransform extends BitmapTransformation {

    public GlideMusicBgTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return dealStoke(pool, toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private Bitmap dealStoke(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Canvas canvas = new Canvas(source);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(grayColorFilter);
        canvas.drawBitmap(source, 0, 0, paint);
        canvas.drawARGB(200, 255, 255, 255);

        return source;
    }
}
