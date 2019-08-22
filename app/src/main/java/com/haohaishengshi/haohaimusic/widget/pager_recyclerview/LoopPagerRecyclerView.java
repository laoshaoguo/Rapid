package com.haohaishengshi.haohaimusic.widget.pager_recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Author Jliuer
 * @Date 2017/2/17/12:54
 * @Email Jliuer@aliyun.com
 * @Description 无限滑动的  viewpager 特性的 RecyclerView
 */
public class LoopPagerRecyclerView extends PagerRecyclerView {

    public LoopPagerRecyclerView(Context context) {
        this(context, null);
    }

    public LoopPagerRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopPagerRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        super.scrollToPosition(getMiddlePosition());
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        super.scrollToPosition(getMiddlePosition());
    }

    @Override
    @NonNull
    protected AdapterWrapper ensureAdapter(Adapter adapter) {
        return (adapter instanceof LoopAdapter)
                ? (LoopAdapter) adapter
                : new LoopAdapter(this, adapter);
    }

    /**
     * 如果目标位置小于实际最大个数，将被转换为正确值
     *
     * @param position 目标位置
     */
    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(transformInnerPositionIfNeed(position));
    }

    @Override
    protected void removeDetachedView(View child, boolean animate) {
        super.removeDetachedView(child, animate);
    }

    @Override
    protected void detachAllViewsFromParent() {
        super.detachAllViewsFromParent();
    }

    public int getActualCurrentPosition() {
        int position = getCurrentPosition();
        return transformToActualPosition(position);
    }

    public int transformToActualPosition(int position) {
        return position % getActualItemCountFromAdapter();
    }

    private int getActualItemCountFromAdapter() {
        return ((LoopAdapter) getWrapperAdapter()).getActualItemCount();

    }

    private int transformInnerPositionIfNeed(int position) {
        LogUtils.d("transformInnerPositionIfNeed:::"+position);
        final int actualItemCount = getActualItemCountFromAdapter();//正确的item个数

        final int actualCurrentPosition = getCurrentPosition() % actualItemCount;//正确的当前位置

        int morePosition = position % actualItemCount;//处在第几个

        int currentPosition = getCurrentPosition();//总的当前位置

        int offsetPosition = currentPosition - actualCurrentPosition;// 偏移位置

        int normalPosition = offsetPosition + morePosition;//如果按照正常排序位

        int bakPosition1 = normalPosition;

        int bakPosition2 = normalPosition - actualItemCount;//往后 退回一次循环

        int bakPosition3 = normalPosition + actualItemCount;//往前 前进一次循环

        // 取得最靠近当前位置的 正序位。
        if (Math.abs(bakPosition1 - currentPosition) > Math.abs(bakPosition2 - currentPosition)) {
            if (Math.abs(bakPosition2 - currentPosition) > Math.abs(bakPosition3 -
                    currentPosition)) {
                return bakPosition3;
            }
            return bakPosition2;
        } else {
            if (Math.abs(bakPosition1 -
                    currentPosition) > Math.abs(bakPosition3 -
                    currentPosition)) {
                return bakPosition3;
            }
            return bakPosition1;
        }

    }

    /**
     * 初始化时便滚动到中间，便于两边滚动
     *
     * @return 中间位置
     */
    private int getMiddlePosition() {
        int middlePosition = Integer.MAX_VALUE / 2;
        final int actualItemCount = getActualItemCountFromAdapter();
        if (actualItemCount > 0 && middlePosition % actualItemCount != 0) {
            middlePosition -= middlePosition % actualItemCount;
        }
        return middlePosition;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return false;
    }
}
