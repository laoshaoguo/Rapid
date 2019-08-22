package com.zhiyicx.baseproject.cache;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.log.LogUtils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public class CacheImp<T extends CacheBean> {

    private DiskCache<T> mDiskCache;

    public CacheImp(IDataBaseOperate iDataBaseOperate) {

        mDiskCache = new DiskCache(iDataBaseOperate);
    }

    public Observable<BaseJson<T>> load(Long key, NetWorkCache<T> networkCache) {
        return Observable.concat(
                loadFromDisk(key)
                , loadFromNetWork(key, networkCache)
        ).first(new Func1<BaseJson<T>, Boolean>() {
            @Override
            public Boolean call(BaseJson<T> o) {
                // 拿到第一条数据,通过是否过期判断
                if (o == null) {
                    String result = "no cache";
                    LogUtils.i(result, o);
                } else {
                    T data = o.getData();
                    String result = (data == null ? "no cache" : data.isExpire() ? "cache is expire" : "cache is ok");
                    LogUtils.i(result, o);
                    return o != null && data != null && !data.isExpire();
                }
                return false;
            }
        });
    }

    private Observable<BaseJson<T>> loadFromDisk(final Long key) {
        Observable.Transformer<BaseJson<T>, BaseJson<T>> transformer = log("load from Disk: " + key);
        return mDiskCache.get(key)
                .compose(transformer);
    }

    private Observable<BaseJson<T>> loadFromNetWork(final Long key, NetWorkCache<T> networkCache) {
        Observable.Transformer<BaseJson<T>, BaseJson<T>> transformer = log("load from NetWork: " + key);
        return networkCache.get(key)
                .compose(transformer)
                .doOnNext(new Action1<BaseJson<T>>() {
                    @Override
                    public void call(BaseJson<T> o) {
                        // 网络请求结束后，需要将数据缓存到memory和磁盘中（数据库）
                        if (o != null && o.getData() != null) {
                            mDiskCache.put(key, o.getData());
                        }
                    }
                });
    }

    private Observable.Transformer<BaseJson<T>, BaseJson<T>> log(final String msg) {
        return new Observable.Transformer<BaseJson<T>, BaseJson<T>>() {
            @Override
            public Observable<BaseJson<T>> call(Observable<BaseJson<T>> baseJsonObservable) {
                return baseJsonObservable.doOnNext(new Action1<BaseJson<T>>() {
                    @Override
                    public void call(BaseJson<T> t) {
                        //MemoryCache、DiskCache中已经打印过log了，这里只是为了演示transformer、和compose的使用
                        LogUtils.d("cache", msg);
                    }
                });
            }
        };
    }
}
