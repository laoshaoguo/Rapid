package com.zhiyicx.baseproject.base;

import android.text.TextUtils;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageLoaderStrategy;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.utils.SharePreferenceUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/21
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSApplication extends BaseApplication {
    private static final int DEFAULT_TOAST_SHORT_DISPLAY_TIME = 2_000;


    @Override
    public void onCreate() {
        super.onCreate();
        /// 处理app崩溃异常
//        CrashClient.init(new CrashClient.CrashListener() {
//            @SuppressLint("MyToastHelper")
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                e.printStackTrace();
//                Toast.makeText(BaseApplication.getContext(), R.string.app_crash_tip, Toast.LENGTH_SHORT).show();
//                rx.Observable.timer(DEFAULT_TOAST_SHORT_DISPLAY_TIME, TimeUnit.MILLISECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<Long>() {
//                            @Override
//                            public void call(Long aLong) {
//                                ActivityHandler.getInstance().AppExit();
//                            }
//                        });
//            }
//        });
        // 友盟
//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
//        UMShareAPI.get(getApplicationContext()).setShareConfig(config);
//        UMShareAPI.get(this);
//        MobclickAgent.setDebugMode(BuildConfig.USE_LOG);
    }

    /**
     * 网络根地址
     *
     * @return
     */
    @Override
    public String getBaseUrl() {
        if (BuildConfig.USE_DOMAIN_SWITCH) {
            String domain = SharePreferenceUtils.getString(getContext(), SharePreferenceUtils.SP_DOMAIN);
            if (!TextUtils.isEmpty(domain)) {
                ApiConfig.APP_DOMAIN = domain;
            }
        }
        return ApiConfig.APP_DOMAIN;
    }

    /**
     * 默认使用 glide,如果需要使用picasso等，请按照Gi{@Link GlideImageLoaderStrategy 配置}
     *
     * @return
     */
    @Override
    protected ImageModule getImageModule() {
        return new ImageModule(new GlideImageLoaderStrategy());
    }


}
