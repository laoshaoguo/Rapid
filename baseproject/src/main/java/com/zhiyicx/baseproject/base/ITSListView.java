package com.zhiyicx.baseproject.base;

import com.zhiyicx.common.mvp.i.IBaseView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public interface ITSListView<T, P> extends IBaseView<P> {
    /**
     * 服务器返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onNetResponseSuccess(List<T> data, boolean isLoadMore);

    /**
     * 数据库返回数据
     *
     * @param data       内容信息
     * @param isLoadMore 加载状态
     */
    void onCacheResponseSuccess(List<T> data, boolean isLoadMore);

    /**
     * 错误信息
     *
     * @param throwable  具体错误信息
     * @param isLoadMore 加载状态
     */
    void onResponseError(Throwable throwable, boolean isLoadMore);

    /**
     * 隐藏刷新动画
     * @param isLoadMore
     */
    void hideRefreshState(boolean isLoadMore);

    /**
     * 显示常驻信息
     *
     * @param msg 信息内容
     */
    void showStickyMessage(String msg);

    /**
     * 显示常驻信息 html 格式
     * 动态列表广告
     *
     * @param html 信息内容
     */
    void showStickyHtmlMessage(@NotNull String html);

    /**
     * 隐藏常驻信息
     */
    void hideStickyMessage();

    /**
     * 当 max_id 无法使用的时候标识分页
     *
     * @return
     */
    int getPage();

    /**
     * 获取数据源数据
     *
     * @return
     */
    List<T> getListDatas();

    /**
     * 刷新数据
     */
    void refreshData();

    /**
     * 刷新数据
     */
    void refreshData(List<T> datas);

    /**
     * 刷新单条数据
     */
    void refreshData(int index);

    void refreshRangeData(int start,int count);

    /**
     * 手动刷新
     */
    void startRefrsh();
}
