package com.zhiyicx.common.utils.imageloader.core;

import android.content.Context;

import com.zhiyicx.common.utils.imageloader.config.ImageConfig;
import com.zhiyicx.common.utils.imageloader.loadstrategy.ImageLoaderStrategy;

/**
 * @Describe   图片加载器
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class ImageLoader {
    private ImageLoaderStrategy mStrategy;

    public ImageLoader(ImageLoaderStrategy strategy) {
        setLoadImgStrategy(strategy);
    }

    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }


    public void setLoadImgStrategy(ImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

}
