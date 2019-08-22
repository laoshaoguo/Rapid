package com.zhiyicx.common.utils.recycleviewdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */

public class CustomLinearDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int bottom;
    private int left;
    private int right;
    private boolean mIsNeedLastDecoration = true;
    private Drawable mDivider;
    private int mMarginLeft;
    private int mMarginRight;

    public int getMarginRight() {
        return mMarginRight;
    }

    public void setMarginRight(int marginRight) {
        mMarginRight = marginRight;
    }

    public int getMarginLeft() {
        return mMarginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        mMarginLeft = marginLeft;
    }

    public void setNeedLastDecoration(boolean needLastDecoration) {
        mIsNeedLastDecoration = needLastDecoration;
    }

    public CustomLinearDecoration(int top, int bottom, int left, int right, Drawable divider) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.mDivider = divider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDivider != null) {
            drawHorizontal(c, parent);
        } else {
            super.onDraw(c, parent, state);
        }

    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == childCount - 1) {
                return;
            }
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left+mMarginLeft, top, right-mMarginRight, bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        if (!mIsNeedLastDecoration && parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {// 最后一行
            outRect.bottom = 0;
        } else {
            outRect.bottom = bottom;
        }
        outRect.left = left;
        outRect.right = right;

    }

}
