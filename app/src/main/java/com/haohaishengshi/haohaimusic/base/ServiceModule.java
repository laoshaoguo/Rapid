package com.haohaishengshi.haohaimusic.base;



import com.haohaishengshi.haohaimusic.data.source.remote.CommonClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zhiyicx on 2016/3/30.
 */
@Module
public class ServiceModule {
    /**
     * 公用相关的网络接口
     *
     * @param retrofit 网络框架
     * @return
     */
    @Singleton
    @Provides
    CommonClient provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonClient.class);
    }

//    /**
//     * 登录相关的网络接口
//     *
//     * @param retrofit 网络框架
//     * @return
//     */
//    @Singleton
//    @Provides
//    LoginClient provideLoginClient(Retrofit retrofit) {
//        return retrofit.create(LoginClient.class);
//    }
//
//    /**
//     * 密码相关的网络接口
//     *
//     * @param retrofit 网络框架
//     * @return
//     */
//    @Singleton
//    @Provides
//    PasswordClient providePasswordClient(Retrofit retrofit) {
//        return retrofit.create(PasswordClient.class);
//    }
//
//    /**
//     * 用户信息的网络接口
//     *
//     * @param retrofit 网络框架
//     * @return
//     */
//    @Singleton
//    @Provides
//    UserInfoClient provideUserInfoClient(Retrofit retrofit) {
//        return retrofit.create(UserInfoClient.class);
//    }
//
//    /**
//     * 注册相关的网络接口
//     *
//     * @param retrofit 网络框架
//     * @return
//     */
//    @Singleton
//    @Provides
//    RegisterClient provideRegisterClient(Retrofit retrofit) {
//        return retrofit.create(RegisterClient.class);
//    }
}
