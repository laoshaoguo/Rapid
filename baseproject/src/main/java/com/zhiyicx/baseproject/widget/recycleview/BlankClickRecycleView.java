package com.zhiyicx.baseproject.widget.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * @Describe 可以点击空白处的 RecycleView
 * @Author Jungle68
 * @Date 2017/12/15
 * @Contact master.jungle68@gmail.com
 */
public class BlankClickRecycleView extends RecyclerView {
    private GestureDetectorCompat mGestureDetector;
    private BlankClickListener mBlankClickListener;

    public BlankClickRecycleView(Context context) {
        super(context);
    }

    public BlankClickRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlankClickRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setBlankListener(BlankClickListener listener) {
        this.mBlankClickListener = listener;
        this.mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    /**
     * 处理点击事件
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (mGestureDetector != null && mGestureDetector.onTouchEvent(e)) {
            View childView = findChildViewUnder(e.getX(), e.getY());
            if (childView == null) {
                mBlankClickListener.onBlickClick();
                return true;
            }
        }
        return super.onTouchEvent(e);

    }

    public interface BlankClickListener {

        void onBlickClick();

    }
}
