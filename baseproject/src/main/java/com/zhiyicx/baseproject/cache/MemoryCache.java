package com.zhiyicx.baseproject.cache;

import android.util.LruCache;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;
import rx.Subscriber;

/**
 * @author LiuChao
 * @describe 获取内存缓存数据，通过lruCache实现
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public class MemoryCache<T extends CacheBean> implements ICache<T> {
    // LruCache定义泛型：String类型的key，Object类型的缓存数据
    private LruCache<String, T> mLruCache;

    public MemoryCache() {
        // 系统分配给应用的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 给当前应用分配最大缓存,应用最大内存的八分之一
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, T>(cacheMemory) {
            @Override
            protected int sizeOf(String key, T value) {
                return super.sizeOf(key, value);
            }
        };
    }

    @Override
    public Observable<BaseJson<T>> get(final Long key) {
        return Observable.create(new Observable.OnSubscribe<BaseJson<T>>() {
            @Override
            public void call(Subscriber<? super BaseJson<T>> subscriber) {
                // 取消订阅
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                // 获取缓存数据
                T t = mLruCache.get(String.valueOf(key));
                // 发射缓存数据
                BaseJson<T> baseJson = new BaseJson<T>();
                baseJson.setCode(0);
                baseJson.setData(t);
                baseJson.setStatus(true);
                subscriber.onNext(baseJson);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void put(Long key, T t) {
        mLruCache.put(String.valueOf(key), t);
    }
}
