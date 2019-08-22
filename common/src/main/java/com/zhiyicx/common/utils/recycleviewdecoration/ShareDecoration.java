package com.zhiyicx.common.utils.recycleviewdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/31
 * @Contact master.jungle68@gmail.com
 */
public class ShareDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ShareDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.right =space;
        outRect.top = 0;
        outRect.bottom = 0;


    }

}