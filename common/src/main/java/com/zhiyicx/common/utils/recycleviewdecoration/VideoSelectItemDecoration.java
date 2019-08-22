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
 * @Date 2018/03/30/14:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VideoSelectItemDecoration extends BaseGridDecoration {

    private Drawable mDivider;

    private int[] mSpace;

    public VideoSelectItemDecoration(Context context, int drawableRes) {
        mDivider = ContextCompat.getDrawable(context, drawableRes);
    }

    public VideoSelectItemDecoration(int width, int height) {
        mSpace = new int[]{width, height};
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

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildAdapterPosition(view);
        int width = 0, height = 0;
        if (mSpace != null) {
            width = mSpace[0];
            height = mSpace[1];
        }
        if (mDivider != null) {
            width = mDivider.getIntrinsicWidth();
            height = mDivider.getIntrinsicHeight();
        }


        if (isLastColum(parent, itemPosition, spanCount, childCount)) {
            // 如果是最后一列，则不需要绘制右边
            outRect.set(0, height, 0, 0);
        } else {
            outRect.set(0, height, width, 0);
        }

    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
