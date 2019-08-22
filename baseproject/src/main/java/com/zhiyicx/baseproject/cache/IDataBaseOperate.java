package com.zhiyicx.baseproject.cache;

import java.util.List;

/**
 * @Describe 数据库保存服务器数据，实现本地缓存
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public interface IDataBaseOperate<T> {
    /**
     * 保存服务器单条数据
     */
    long saveSingleData(T singleData);

    /**
     * 保存服务器多条数据
     */
    void saveMultiData(List<T> multiData);

    /**
     * 判断数据是否失效
     */
    boolean isInvalide();

    /**
     * 根据key(比如userID)，从缓存中获取单条数据
     */
    T getSingleDataFromCache(Long primaryKey);

    /**
     * 获取表中所有的数据，（比如所有的好友）
     */
    List<T> getMultiDataFromCache();

    /**
     * 清空当前的表
     */
    void clearTable();

    /**
     * 根据key，删除缓存中的某条数据
     */
    void deleteSingleCache(Long primaryKey);
    /**
     * 根据 data，删除缓存中的某条数据
     */
    void deleteSingleCache(T dta);

    /**
     * 更新缓存中的某条数据
     */
    void updateSingleData(T newData);
    /**
     * 插入或者替换缓存中的某条数据
     */
    long insertOrReplace(T newData);
}
