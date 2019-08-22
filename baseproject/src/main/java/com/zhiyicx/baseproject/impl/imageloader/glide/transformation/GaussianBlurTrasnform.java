package com.zhiyicx.baseproject.impl.imageloader.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.common.utils.FastBlur;


/**
 * @Describe  滤镜效果
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public class GaussianBlurTrasnform extends BitmapTransformation {
    public GaussianBlurTrasnform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return FastBlur.blurBitmap(toTransform,outWidth,outHeight);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}

