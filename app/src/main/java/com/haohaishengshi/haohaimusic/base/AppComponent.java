package com.haohaishengshi.haohaimusic.base;

import android.app.Application;

import com.haohaishengshi.haohaimusic.data.source.local.CacheManager;
import com.haohaishengshi.haohaimusic.data.source.remote.ServiceManager;
import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.HttpClientModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.rxerrorhandler.RxErrorHandler;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

@Singleton
@Component(modules = {AppModule.class, HttpClientModule.class, ServiceModule.class, CacheModule.class, ImageModule.class})
public interface AppComponent extends InjectComponent<AppApplication> {


    /**
     * 以下是想往外提供的东西
     */

    Application Application();

    //服务管理器,retrofitApi
    ServiceManager serviceManager();

//    CommonClient commonClient();

    //缓存管理器
    CacheManager cacheManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();

    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();

}
