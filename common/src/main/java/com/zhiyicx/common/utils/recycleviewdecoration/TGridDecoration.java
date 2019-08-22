package com.zhiyicx.common.utils.recycleviewdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Author Jliuer
 * @Date 2017/3/2/11:22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TGridDecoration extends BaseGridDecoration {

    private Drawable mDivider;

    private int[] mSpace;

    private boolean mFullPadding;

    public TGridDecoration(Context context, int drawableRes) {
        mDivider = ContextCompat.getDrawable(context, drawableRes);
    }

    public TGridDecoration(int width, int height, boolean fullPadding) {
        mSpace = new int[]{width, height};
        this.mFullPadding = fullPadding;
    }

    public TGridDecoration(int left, int top, int right, int bottom, boolean fullPadding) {
        mSpace = new int[]{right, bottom, left, top};
        this.mFullPadding = fullPadding;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null) {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        } else {
            super.onDraw(c, parent, state);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = getItemCount(parent);
        final int spanCount = getSpanCount(parent);
        int position;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child==null){
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            if (isFooter(child)) {
                continue;
            }
            position = parent.getChildAdapterPosition(child);
            if (!isNeeddrawHorizontal(parent, position, spanCount, childCount)) {
                continue;
            }
            left += getLeftCut(parent,position , spanCount, childCount);
            right += getRightCut(parent,position , spanCount, childCount);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = getItemCount(parent);
        final int spanCount = getSpanCount(parent);
        int position;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child==null){
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            if (isFooter(child)) {
                continue;
            }
            position = parent.getChildAdapterPosition(child);
            if (!isNeeddrawVertical(parent,position , spanCount, childCount)) {
                continue;
            }
            top += getTopCut(parent,position , spanCount, childCount);
            bottom += getBottmCut(parent,position , spanCount, childCount);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        int spanCount = getSpanCount(parent);
        int childCount = getItemCount(parent);
        int itemPosition = getItemPosition(parent,view);
        int width = 0, height = 0;
        if (mSpace != null) {
            width = mSpace[0];
            height = mSpace[1];
        }
        if (mDivider != null) {
            width = mDivider.getIntrinsicWidth();
            height = mDivider.getIntrinsicHeight();
        }

        if (mFullPadding) {
            outRect.set(0, 0, width,
                    height);
        } else {
            if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                // 如果是最后一行，则不需要绘制底部
                outRect.set(0, 0, width, 0);
            } else if (isLastColum(parent, itemPosition, spanCount, childCount)) {
                // 如果是最后一列，则不需要绘制右边
                outRect.set(0, 0, 0, height);
            } else {
                outRect.set(0, 0, width,
                        height);
            }
        }
        if (isFooter(view)) {
            outRect.set(0, 0, 0, 0);
        }
    }

    protected boolean isNeeddrawVertical(RecyclerView parent, int i, int spanCount, int childCount) {
        return true;
    }

    protected boolean isNeeddrawHorizontal(RecyclerView parent, int i, int spanCount, int childCount) {
        return true;
    }

    protected int getLeftCut(RecyclerView parent, int i, int spanCount, int childCount) {
        return 0;
    }

    protected int getRightCut(RecyclerView parent, int i, int spanCount, int childCount) {
        return 0;
    }

    protected int getTopCut(RecyclerView parent, int i, int spanCount, int childCount) {
        return 0;
    }

    protected int getBottmCut(RecyclerView parent, int i, int spanCount, int childCount) {
        return 0;
    }

    protected int getItemPosition(RecyclerView parent, View view){
        return parent.getChildAdapterPosition(view);
    }
}
