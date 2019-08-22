package com.zhiyicx.baseproject.impl.imageloader.glide;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.signature.StringSignature;
import com.zhiyicx.common.utils.imageloader.config.ImageConfig;

/**
 * @Describe Glide图片加载builder
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class GlideImageConfig extends ImageConfig {

    private Transformation<Bitmap> transformation;
    private StringSignature stringSignature;
    private boolean crossFade;


    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.resourceId = builder.resourceId;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.crossFade = builder.crossFade;
        this.transformation = builder.transformation;
        this.stringSignature = builder.stringSignature;

    }

    public Transformation<Bitmap> getTransformation() {
        return transformation;
    }

    public StringSignature getStringSignature() {
        return stringSignature;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public static Buidler builder() {
        return new Buidler();
    }


    public static final class Buidler {
        private String url;
        private Integer resourceId;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private boolean crossFade;
        private Transformation<Bitmap> transformation;
        private StringSignature stringSignature;
        private Buidler() {
        }

        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        public Buidler resourceId(Integer resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Buidler transformation(Transformation<Bitmap> transformation) {
            this.transformation = transformation;
            return this;
        }
           public Buidler stringSignature(StringSignature stringSignature) {
            this.stringSignature = stringSignature;
            return this;
        }


        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Buidler errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Buidler imagerView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Buidler crossFade(boolean crossFade) {
            this.crossFade = crossFade;
            return this;
        }

        public GlideImageConfig build() {
            if (imageView == null) throw new IllegalStateException("imageview is required");
            return new GlideImageConfig(this);
        }
    }
}
