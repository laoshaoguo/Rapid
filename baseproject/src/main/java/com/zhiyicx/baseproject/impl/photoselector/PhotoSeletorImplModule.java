package com.zhiyicx.baseproject.impl.photoselector;

import com.zhiyicx.common.base.BaseFragment;

import dagger.Module;
import dagger.Provides;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/17
 * @contact email:450127106@qq.com
 */
@Module
public class PhotoSeletorImplModule {
    private PhotoSelectorImpl.IPhotoBackListener mPhotoBackListener;
    private BaseFragment mFragment;
    private int mCropShape;// 裁剪框的形状

    public PhotoSeletorImplModule(PhotoSelectorImpl.IPhotoBackListener photoBackListener, BaseFragment fragment, int cropShape) {
        mPhotoBackListener = photoBackListener;
        this.mFragment = fragment;
        this.mCropShape = cropShape;
    }

    @Provides
    public PhotoSelectorImpl providePhotoSelectorImpl() {
        return new PhotoSelectorImpl(mPhotoBackListener, mFragment, mCropShape);
    }
}
