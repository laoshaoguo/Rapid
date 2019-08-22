package com.zhiyicx.common.utils.recycleviewdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */

public class LinearDecoration extends RecyclerView.ItemDecoration {
    public static final int BASE_ITEM_TYPE_HEADER = 100000;
    public static final int BASE_ITEM_TYPE_FOOTER = 200000;
    protected int top;
    protected int bottom;
    protected int left;
    protected int right;
    protected int headerCount;
    protected int footerCount;
    protected boolean mIsNeedLastDecoration = true;

    public void setNeedLastDecoration(boolean needLastDecoration) {
        mIsNeedLastDecoration = needLastDecoration;
    }

    public LinearDecoration(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public LinearDecoration(int top, int bottom, int left, int right, boolean isNeedLastDecoration) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.mIsNeedLastDecoration = isNeedLastDecoration;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;

        if (!mIsNeedLastDecoration && parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            // 最后一行
            outRect.bottom = 0;
        } else {
            outRect.bottom = bottom;
        }
        if (parent.getChildAdapterPosition(view) < headerCount) {
            outRect.bottom = 0;
        }
        if (view.getTag() != null && view.getTag() instanceof Integer && (int) view.getTag() == BASE_ITEM_TYPE_FOOTER) {
            outRect.bottom = 0;
        }
        outRect.left = left;
        outRect.right = right;

    }

    public int getHeaderCount() {
        return headerCount;
    }

    public void setHeaderCount(int headerCount) {
        this.headerCount = headerCount;
    }

    public int getFooterCount() {
        return footerCount;
    }

    public void setFooterCount(int footerCount) {
        this.footerCount = footerCount;
    }
}
