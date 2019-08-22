package com.zhiyicx.baseproject.widget.dragview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @author LiuChao
 * @describe 可以弹性滑动的ScrollView，配合DragViewGroup父容器实现，该类主要处理和ViewDragHelper的事件分发冲突
 * @date 2017/3/25
 * @contact email:450127106@qq.com
 */

public class DragScrollView extends ScrollView implements DragViewEndgeWatcher {
    private static final String TAG = "DragScrollView";
    // 纪录触摸点，方便判断用户手势
    private float mDownX;
    private float mDownY;

    public DragScrollView(Context context) {
        super(context);
    }

    public DragScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean parentDisallowIntercept = true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 纪录触摸的起始点位置
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dX = ev.getX() - mDownX;
                float dY = ev.getY() - mDownY;
                mDownX = ev.getX();
                mDownY = ev.getY();
                // 判断手指滑动趋势，是偏向水平方向，还是垂直方向
                LogUtils.i(TAG+"ACTION_MOVE  Math.abs(dX)-->" + Math.abs(dX) + "  Math.abs(dY)-->" + Math.abs(dY));
                if (Math.abs(dX) > Math.abs(dY)) {
                    // 偏向水平方向
                } else {
                    // 偏向垂直方向
                    if (dY > 0) {
                        // 向下趋势滑动
                        parentDisallowIntercept = !isScrollToTop();
                        LogUtils.i(TAG+"dispatchTouchEvent 向下趋势滑动");
                    } else {
                        //向上趋势滑动
                        parentDisallowIntercept = !isScrollToBottom();
                        LogUtils.i(TAG+"dispatchTouchEvent 向上趋势滑动");
                    }
                }
                break;
        }
        LogUtils.i(TAG+"dispatchTouchEvent 不允许父控件接管滑动" + parentDisallowIntercept);
        getParent().requestDisallowInterceptTouchEvent(parentDisallowIntercept);
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean isScrollToBottom() {
        // 是否能够向上滚动
        return !canScrollVertically(1);
    }

    @Override
    public boolean isScrollToTop() {
        // 是否能够向下滚动
        return !canScrollVertically(-1);
    }

    @Override
    public boolean isScrollToLeftEdge() {
        return false;
    }

    @Override
    public boolean isScrollToRightEdge() {
        return false;
    }


}
