package com.zhiyicx.baseproject.impl.imageloader.glide;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.imageloader.loadstrategy.ImageLoaderStrategy;

/**
 * @Describe Glide统一加载配置, 默认图片统一用灰色正方形块儿，
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class GlideImageLoaderStrategy implements ImageLoaderStrategy<GlideImageConfig> {
    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        RequestManager manager = null;
        if (ctx instanceof Activity)//如果是activity则可以使用Activity的生命周期
            manager = Glide.with((Activity) ctx);
        else
            manager = Glide.with(ctx);
        String imgUrl = config.getUrl();
        boolean isFromNet = !TextUtils.isEmpty(imgUrl) && imgUrl.startsWith("http");// 是否来源于网络
        GenericRequestBuilder requestBuilder = manager.load(TextUtils.isEmpty(imgUrl) ? config.getResourceId() : isFromNet ? imgUrl : "file://" + imgUrl)
                .asBitmap() // asbitmap 就不能使用crossfade
                .diskCacheStrategy(isFromNet ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                // .skipMemoryCache(isFromNet ? false : true) 本地图片也需要缓存到内存中
                .centerCrop();
        if (config.getErrorPic() != 0)// 设置错误的图片
        {
            requestBuilder.error(config.getErrorPic());
        } else {
            requestBuilder.error(R.drawable.shape_default_image);
        }
        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }
        if (config.getStringSignature() != null) {
            requestBuilder.signature(config.getStringSignature());
        }
        if (config.getPlaceholder() != 0)// 设置占位符
        {
            requestBuilder.placeholder(config.getPlaceholder());
        } else {// 设置默认占位符
            requestBuilder.placeholder(R.drawable.shape_default_image);
        }
        requestBuilder
                .into(config.getImageView());
    }
}
