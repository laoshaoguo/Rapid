package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * @Describe 带白底框的圆形 trasform
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public class GlideCircleBorderTransform extends BitmapTransformation {
    private int mBorderWith;
    private int mBorderColor;

    public GlideCircleBorderTransform(Context context, int borderWith, int borderColor) {
        super(context);
        this.mBorderColor = borderColor;
        this.mBorderWith = borderWith;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }


        Canvas canvas = new Canvas(result);
        float r = size / 2f;

        //画轮廓
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(mBorderColor);
        canvas.drawCircle(r, r, r, paint1);

        //画图片
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        canvas.drawCircle(r, r, r - mBorderWith, paint);

        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
