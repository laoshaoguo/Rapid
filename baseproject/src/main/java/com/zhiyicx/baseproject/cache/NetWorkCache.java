package com.zhiyicx.baseproject.cache;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */

public interface NetWorkCache<T extends CacheBean> {
    Observable<BaseJson<T>> get(Long key);
}
