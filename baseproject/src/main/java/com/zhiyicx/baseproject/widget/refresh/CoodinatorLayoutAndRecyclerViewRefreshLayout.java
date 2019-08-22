package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe 处理CoodianatorLayout中ToolBar滚动隐藏后，下拉刷新和recyclerview滑动的冲突
 * @date 2017/4/7
 * @contact email:450127106@qq.com
 */

public class CoodinatorLayoutAndRecyclerViewRefreshLayout extends SmartRefreshLayout
        implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnScrollChangedListener {
    private static final String TAG = "CoodinatorLayoutAndRecy";
    private float downX, downY;
    private int startY;
    private boolean isFirstOnLayout = true;

    public CoodinatorLayoutAndRecyclerViewRefreshLayout(Context context) {
        super(context);
    }

    public CoodinatorLayoutAndRecyclerViewRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoodinatorLayoutAndRecyclerViewRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        // 获取刷新控件初始距离顶部的位置
        if (isFirstOnLayout) {
            isFirstOnLayout = false;
            getViewTreeObserver().addOnGlobalLayoutListener(this);
            int[] location = new int[2];
            getLocationOnScreen(location);
            startY = location[1];
        }
    }

    @Override
    public void onGlobalLayout() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        startY = location[1];
    }

    @Override
    public void onScrollChanged() {
        int[] location = new int[2];
        getLocationOnScreen(location);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        init();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float temX = ev.getX();
                float temY = ev.getY();
                float dX = temX - downX;
                float dY = temY - downY;
                // 垂直方向 手指向下滑动
                if (Math.abs(dY) / Math.abs(dX) >= 1 && dY > 0) {
                    int[] location = new int[2];
                    getLocationOnScreen(location);
                    if (location[1] <= startY - 1) {// 少一点要求多一些包容 岂不美滋滋
                        LogUtils.i("onInterceptTouchEvent ::" + "不触发刷新");
                        return false;
                    }
                } else {
                    LogUtils.d(Math.abs(dY) / Math.abs(dX));
                    LogUtils.d(dY);
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:

        }
        return super.onInterceptTouchEvent(ev);
    }
}
