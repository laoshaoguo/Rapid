package com.zhiyicx.baseproject.cache;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;

/**
 * @author LiuChao
 * @describe 实现自定义缓存的接口
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public interface ICache<T extends CacheBean> {
    /**
     * 获取缓存的Observable
     *
     * @param key 缓存标识符
     */
    Observable<BaseJson<T>> get(Long key);

    /**
     * 保存数据到缓存
     *
     * @param key 缓存标识符
     * @param t   需要缓存的对象
     */
    void put(Long key, T t);

}
