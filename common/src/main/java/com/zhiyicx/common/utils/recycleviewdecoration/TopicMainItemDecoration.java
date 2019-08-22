package com.zhiyicx.common.utils.recycleviewdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.common.utils.ConvertUtils;

/**
 * ThinkSNS Plus
 * Copyright (c) 2018 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @Author Jliuer
 * @Date 2018/07/24/9:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopicMainItemDecoration extends TGridDecoration {

    /**
     * 分割线边距 ，
     */
    private int[] mConsume;

    public TopicMainItemDecoration(Context context, int drawableRes) {
        super(context, drawableRes);
    }

    public TopicMainItemDecoration(Context context, int drawableRes, int cutX, int cutY) {
        super(context, drawableRes);
        mConsume = new int[]{cutX, cutY};
    }

    public TopicMainItemDecoration(int width, int height, boolean fullPadding) {
        super(width, height, fullPadding);
    }

    public TopicMainItemDecoration(int left, int top, int right, int bottom, boolean fullPadding) {
        super(left, top, right, bottom, fullPadding);
    }

    @Override
    protected boolean isNeeddrawHorizontal(RecyclerView parent, int i, int spanCount, int childCount) {
        return !isLastRaw(parent, i, spanCount, childCount);
    }

    @Override
    protected boolean isNeeddrawVertical(RecyclerView parent, int i, int spanCount, int childCount) {
        return !isLastColum(parent, i, spanCount, childCount);
    }

    @Override
    protected int getLeftCut(RecyclerView parent, int i, int spanCount, int childCount) {
        if (i % spanCount == 0) {
            return mConsume[0];
        }
        return super.getLeftCut(parent, i, spanCount, childCount);
    }

    @Override
    protected int getRightCut(RecyclerView parent, int i, int spanCount, int childCount) {
        if (i % spanCount == 1) {
            return -mConsume[0];
        }
        return super.getRightCut(parent, i, spanCount, childCount);
    }

    @Override
    protected int getTopCut(RecyclerView parent, int i, int spanCount, int childCount) {
        if (mConsume != null) {
            if (i < spanCount) {
                return mConsume[1];
            }
        }
        return super.getTopCut(parent, i, spanCount, childCount);
    }

    @Override
    protected int getBottmCut(RecyclerView parent, int i, int spanCount, int childCount) {
        return super.getBottmCut(parent, i, spanCount, childCount);
    }

    @Override
    protected int getItemCount(RecyclerView parent) {
        return super.getItemCount(parent) - 1;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        view.setPadding(ConvertUtils.dp2px(view.getContext(), 15 + ((getItemPosition(parent, view) % 2 != 0) ? 5 : 0)),
                0, ConvertUtils.dp2px(view.getContext(), 15), 0);

    }
}
