package com.zhiyicx.baseproject.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
public interface ITSListPresenter<T extends BaseListBean> extends IBaseTouristPresenter {
    /**
     * 请求列表数据
     *
     * @param maxId      当前获取到数据的最大 id
     * @param isLoadMore 加载状态，是否是加载更多
     */
    void requestNetData(Long maxId, boolean isLoadMore);

    /**
     * 请求缓存列表数据
     *
     * @param maxId      当前获取到数据的最小时间
     * @param isLoadMore 加载状态，是否是加载更多
     * @return 返回数据按时间排序
     */
    void requestCacheData(Long maxId, boolean isLoadMore);

    /**
     * 插入或者更新缓存
     *
     * @param data       要保存的数据
     * @param isLoadMore 加载状态，是否是加载更多
     * @return true 保存成功
     */
    boolean insertOrUpdateData(@NotNull List<T> data, boolean isLoadMore);

}
