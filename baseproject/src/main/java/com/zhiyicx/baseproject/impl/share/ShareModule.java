package com.zhiyicx.baseproject.impl.share;


import android.app.Activity;

import com.zhiyicx.baseproject.share.SharePolicy;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

@Module
public class ShareModule {
    private Activity mActivity;

    public ShareModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    public SharePolicy provideSharePolicy() {
        return new UmengSharePolicyImpl(mActivity);
    }
}
