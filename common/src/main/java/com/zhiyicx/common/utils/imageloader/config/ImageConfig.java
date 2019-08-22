package com.zhiyicx.common.utils.imageloader.config;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * @Describe   图片加载的配置信息
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class ImageConfig {
    protected String url;
    protected Integer resourceId;
    protected ImageView imageView;
    protected int placeholder;
    protected int errorPic;


    public String getUrl() {
        return url;
    }

    public Integer getResourceId() {
        return resourceId;
    }
    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }

}
