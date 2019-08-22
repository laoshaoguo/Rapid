package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 正方形图片&白色描边
 * 这个描边有问题，有点长图图片会变形，不知道为什么。by - tym 2018-7-4 14:31:48
 */
public class GlideStokeTransform extends BitmapTransformation {
    private int mStokeWidth = 3;
    private int mColor = Color.WHITE;
    private int mWidth;
    private int mHeight;

    public GlideStokeTransform(Context context) {
        super(context);
    }

    public GlideStokeTransform(Context context, int mStokeWidth) {
        this(context);
        this.mStokeWidth = mStokeWidth;
    }

    public GlideStokeTransform(Context context, int mStokeWidth,int color) {
        this(context);
        this.mStokeWidth = mStokeWidth;
        this.mColor = color;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        int size = Math.min(source.getWidth(), source.getHeight());

        mWidth = (source.getWidth() - size) / 2;
        mHeight = (source.getHeight() - size) / 2;

        Bitmap bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size);
        Rect dst = new Rect(mStokeWidth/2, mStokeWidth/2, size - mStokeWidth/2, size - mStokeWidth/2);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mStokeWidth);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(dst, paint);
        return bitmap;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
