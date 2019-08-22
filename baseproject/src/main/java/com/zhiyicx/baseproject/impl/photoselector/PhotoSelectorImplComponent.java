package com.zhiyicx.baseproject.impl.photoselector;


import com.zhiyicx.common.dagger.scope.FragmentScoped;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/17
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(modules = PhotoSeletorImplModule.class)
public interface PhotoSelectorImplComponent {
    PhotoSelectorImpl photoSelectorImpl();
    //void inject(Activity userInfoActivity);
}
