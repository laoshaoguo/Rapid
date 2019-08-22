package com.zhiyicx.common.utils.recycleviewdecoration;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import static com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration.BASE_ITEM_TYPE_FOOTER;
import static com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration.BASE_ITEM_TYPE_HEADER;

/**
 * @Author Jliuer
 * @Date 2018/03/30/14:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseGridDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    /**
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return 最后一列
     */
    protected boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return 最后一行
     */
    protected boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (childCount % spanCount == 0) {
                childCount = childCount - spanCount;
            } else {
                childCount = childCount - childCount % spanCount;
            }
            if (pos >= childCount) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // StaggeredGridLayoutManager 且纵向滚动
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount) {
                    return true;
                }
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    protected boolean isFooter(View view) {
        return view.getTag() != null && view.getTag() instanceof Integer && (int) view.getTag() == BASE_ITEM_TYPE_FOOTER;
    }

    protected boolean isHeader(View view) {
        return view.getTag() != null && view.getTag() instanceof Integer && (int) view.getTag() == BASE_ITEM_TYPE_HEADER;
    }

    protected int getItemCount(RecyclerView parent) {
        return parent.getAdapter().getItemCount();
    }
}
