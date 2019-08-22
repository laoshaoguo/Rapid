/*
 * Copyright © Yolanda. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haohaishengshi.haohaimusic.widget.pager_recyclerview.itemtouch;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author legendary_tym
 * @Title SpicyCommunity
 * @Package com.zycx.spicycommunity.projcode.quanzi.view.quanzidetail.itemtouch
 * @Description: 拖拽核心类，待进一步优化
 * @date: 2016-11-08 18:20
 */
public class DefaultItemTouchHelpCallback extends ItemTouchHelper.Callback {

    /**
     * Item操作的回调
     */
    private OnItemTouchCallbackListener onItemTouchCallbackListener;

    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag = false;
    /**
     * 是否可以被滑动
     */
    private boolean isCanSwipe = false;

    public DefaultItemTouchHelpCallback(OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    /**
     * 设置Item操作的回调，去更新UI和数据源
     */
    public void setOnItemTouchCallbackListener(
            OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    /**
     * 设置是否可以被拖拽
     *
     * @param canDrag 是true，否false
     */
    public void setDragEnable(boolean canDrag) {
        isCanDrag = canDrag;
    }

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    public void setSwipeEnable(boolean canSwipe) {
        isCanSwipe = canSwipe;
    }

    /**
     * 当Item被长按的时候是否可以被拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return isCanDrag;
    }

    /**
     * Item是否可以被滑动(H：左右滑动，V：上下滑动)
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwipe;
    }

    /**
     * @author legendary_tym
     * @date 2016/11/30 9:29
     * @description 当用户拖拽或者滑动Item的时候需要我们告诉系统滑动或者拖拽的方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            // create make
            return makeMovementFlags(dragFlag, swipeFlag);
        } else if (layoutManager instanceof LinearLayoutManager) {// linearLayoutManager
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();

            int dragFlag = 0;
            int swipeFlag = 0;

            //布局方向
            if (orientation == LinearLayoutManager.HORIZONTAL) {// 如果是横向的布局
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL) {// 如果是竖向的布局
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            if (viewHolder.getAdapterPosition() == recyclerView.getAdapter().getItemCount() - 1) {
                dragFlag = 0;
            }
            if (viewHolder.getAdapterPosition() == recyclerView.getAdapter().getItemCount() - 1) {
                dragFlag = 0;
                swipeFlag = 0;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    /**
     * @param recyclerView     recyclerView
     * @param srcViewHolder    拖拽的ViewHolder
     * @param targetViewHolder 目的地的viewHolder
     * @author legendary_tym
     * @date 2016/11/30 9:29
     * @description 当Item被拖拽的时候被回调
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcViewHolder,
                          RecyclerView.ViewHolder targetViewHolder) {
        if (onItemTouchCallbackListener != null) {
            return onItemTouchCallbackListener.onMove(srcViewHolder.getAdapterPosition(),
                    targetViewHolder.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (onItemTouchCallbackListener != null) {
            onItemTouchCallbackListener.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    public interface OnItemTouchCallbackListener {
        /**
         * @param adapterPosition item的position
         * @author legendary_tym
         * @date 2016/11/30 9:29
         * @description 当某个Item被滑动删除的时候
         */
        void onSwiped(int adapterPosition);

        /**
         * @param srcPosition    拖拽的item的position
         * @param targetPosition 目的地的Item的position
         * @return 开发者处理了操作应该返回true，开发者没有处理就返回false
         * @author legendary_tym
         * @date 2016/11/30 9:28
         * @description 当两个Item位置互换的时候被回调
         */
        boolean onMove(int srcPosition, int targetPosition);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            //左右滑动时改变Item的透明度
//            final float alpha = 1 - Math.abs(dX) / (float)viewHolder.itemView.getWidth();
//            viewHolder.itemView.setAlpha(alpha);
//            viewHolder.itemView.setTranslationX(dX);
//        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //当选中Item时候会调用该方法，重写此方法可以实现选中时候的一些动画逻辑
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (canScaleOnLongTouch()) {
                viewHolder.itemView.setScaleX((float) 1.1);
                viewHolder.itemView.setScaleY((float) 1.1);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //当动画已经结束的时候调用该方法，重写此方法可以实现恢复Item的初始状态
        viewHolder.itemView.setScaleX(1);
        viewHolder.itemView.setScaleY(1);
    }

    /**
     * @author legendary_tym
     * @date 2016/11/30 10:19
     * @description 拖拽至边界
     */
    @Override
    public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize,
                                            int viewSizeOutOfBounds, int totalSize,
                                            long msSinceStartScroll) {
        //边界
        return super.interpolateOutOfBoundsScroll(recyclerView, viewSize,
                viewSizeOutOfBounds, totalSize, msSinceStartScroll);
    }

    protected boolean canScaleOnLongTouch() {
        return true;
    }
}
