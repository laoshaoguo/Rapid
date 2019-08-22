package com.haohaishengshi.haohaimusic.widget.pager_recyclerview.itemtouch;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author Catherine
 * @describe 已订阅板块拖拽
 * @date 2016/12/30
 * @contact email:648129313@qq.com
 */

public class ChannelItemTouchHelpCallBack extends DefaultItemTouchHelpCallback{

    private int canNotDragRange = 3; // 不能拖动的范围，即为推荐的范围 0-canNotDragRange

    public ChannelItemTouchHelpCallBack(OnItemTouchCallbackListener onItemTouchCallbackListener) {
        super(onItemTouchCallbackListener);
    }


    /**
     * 复写这个方法，推荐的不拖动，只能拖动自己订阅的
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            if (viewHolder.getAdapterPosition() == recyclerView.getAdapter().getItemCount() - 1) {
                dragFlag = 0;
                swipeFlag = 0;
            }

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
            if (viewHolder.getAdapterPosition() <= canNotDragRange) {
                dragFlag = 0;
                swipeFlag = 0;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    public int getCanNotDragRange() {
        return canNotDragRange;
    }

    public void setCanNotDragRange(int canNotDragRange) {
        this.canNotDragRange = canNotDragRange;
    }
}
