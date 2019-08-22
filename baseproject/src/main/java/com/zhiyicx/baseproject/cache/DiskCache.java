package com.zhiyicx.baseproject.cache;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe 本地磁盘缓存，实现缓存接口，当前使用数据库做持久化保存
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public class DiskCache<T extends CacheBean> implements ICache<T> {

    // 数据库实现类的公共接口
    private IDataBaseOperate<T> mDataBaseOperate;

    public DiskCache(IDataBaseOperate<T> commonCache) {
        mDataBaseOperate = commonCache;
    }

    @Override
    public Observable<BaseJson<T>> get(final Long key) {
        return Observable.create(new Observable.OnSubscribe<BaseJson<T>>() {
            @Override
            public void call(Subscriber<? super BaseJson<T>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                T t = mDataBaseOperate.getSingleDataFromCache(key);
                BaseJson<T> baseJson = new BaseJson<T>();
                baseJson.setCode(0);
                baseJson.setData(t);
                baseJson.setStatus(true);
                subscriber.onNext(baseJson);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void put(Long key, T t) {
        mDataBaseOperate.saveSingleData(t);
    }
}
