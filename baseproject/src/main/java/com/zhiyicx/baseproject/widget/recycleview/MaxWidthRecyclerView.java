package com.zhiyicx.baseproject.widget.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.zhiyicx.common.utils.DeviceUtils;

/**
 * @Author Jliuer
 * @Date 2018/01/27/17:07
 * @Email Jliuer@aliyun.com
 * @Description 设置 RecyclerView 在 wrap_content 模式下的最大宽度
 */
public class MaxWidthRecyclerView extends RecyclerView {

    private int needW;

    public MaxWidthRecyclerView(Context context) {
        this(context, null);
    }

    public MaxWidthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxWidthRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int screenW = DeviceUtils.getScreenWidth(context);
        needW = 4 * screenW / 5;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        widthSpec = MeasureSpec.makeMeasureSpec(needW, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
