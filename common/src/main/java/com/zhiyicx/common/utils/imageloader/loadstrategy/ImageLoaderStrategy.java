package com.zhiyicx.common.utils.imageloader.loadstrategy;

import android.content.Context;

import com.zhiyicx.common.utils.imageloader.config.ImageConfig;

/**
 * @Describe   图片加载策略接口
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public interface ImageLoaderStrategy<T extends ImageConfig> {
    void loadImage(Context ctx, T config);
}
