package com.haohaishengshi.haohaimusic.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.haohaishengshi.haohaimusic.R;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 嵌套滑动, 约定头部 id 必须是 R.id.nestedscroll_target
 */
public class NestedScrollLineayLayout extends LinearLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper parentHelper;
    private View headerView;
    private int mTopViewHeight;
    private int width, height;
    private int mNotConsumeHeight;
    private OverScroller mScroller;
    private boolean addHeight;
    private OnHeadFlingListener mOnHeadFlingListener;
    private boolean hiddenTop;
    private boolean showTop;

    // 阻尼系数
    private static final float SCROLL_RATIO = 0.3f;

    private float mPreX, mPreY, mDistanceY;

    public NestedScrollLineayLayout(Context context) {
        super(context);
        init(context);
    }

    public NestedScrollLineayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestedScrollLineayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.setOrientation(LinearLayout.VERTICAL);
        parentHelper = new NestedScrollingParentHelper(this);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 判断HeadView是否完全显示了.
     *
     * @return true, 完全显示, false 没有显示
     */
    private boolean isDisplayHeaderView() {
        int[] location = new int[2]; // 0位存储的是x轴的值, 1是y轴的值
        // 获取HeadView屏幕中y轴的值
        headerView.getLocationOnScreen(location);
        int mSecondHeaderViewYOnScreen = location[1];
        return mSecondHeaderViewYOnScreen > 0 ? true : false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (headerView == null) {
            headerView = findViewById(R.id.nestedscroll_target);
        }
        if (headerView == null) {
            throw new RuntimeException("headerView can not be null");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = headerView.getMeasuredHeight();
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        height = headerView.getHeight();
        return true;
    }

    @Override
    public void onStopNestedScroll(View child) {
        parentHelper.onStopNestedScroll(child);
        mDistanceY = 0;
        replyView();
        LogUtils.d("onStopNestedScroll");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        //处理子view传上来的事件
        //头部高度
        mTopViewHeight = height - mNotConsumeHeight;
        hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
        if (hiddenTop || showTop) {
            if (!addHeight) {//只增加一次 高度 height
                addHeight = true;
                ViewGroup.LayoutParams params = this.getLayoutParams();
                params.height = mTopViewHeight + this.getHeight();
                this.setLayoutParams(params);
                requestLayout();
            }

            if (getScrollY() == mTopViewHeight) {
                replyView();
            }
            scrollBy(0, dy);
            consumed[1] = dy;

        }
        if (mOnHeadFlingListener != null && getScrollY() != 0 && getScrollY() <= mTopViewHeight) {
            mOnHeadFlingListener.onHeadFling(getScrollY());
        }
        if (getScrollY() == 0) {
            mDistanceY += -dy;
            dealScale(mDistanceY * SCROLL_RATIO);
        }
    }

    private void dealScale(float s) {
        float scaleTimes = (float) ((height + s) / (height * 1.0));
//        if (mOnHeadFlingListener != null && scaleTimes == 1.3f) {
//            mOnHeadFlingListener.onHeadZoom();
//        }
//        if (mOnHeadFlingListener != null && scaleTimes == 1.0f) {
//            mOnHeadFlingListener.onHeadRedu();
//        }
        // 如超过最大放大倍数，直接返回
        if (scaleTimes > 1.5f) return;

        ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
        int scaleHeight = (int) (height * scaleTimes);
        layoutParams.height = scaleHeight;
        headerView.setLayoutParams(layoutParams);
        int scrollTo = (height - scaleHeight);
        headerView.scrollTo(0, scrollTo / 2);
        LogUtils.d("scrollTo:::" + scrollTo);
        LogUtils.d("heightscrollTo:::" + mTopViewHeight);
        LogUtils.d("getScrollYscrollTo:::" + getScrollY());
    }

    private void replyView() {
        final float distance = headerView.getHeight() - height;

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(distance, 0.0F).setDuration((long) (distance * SCROLL_RATIO));
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.d("onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dealScale((Float) animation.getAnimatedValue());
            }


        });
        anim.start();

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
            dyUnconsumed) {
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

        int scrollY = getScrollY();

        if (getScrollY() >= mTopViewHeight) {
            return false;
        }
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        if (hiddenTop) {
            LogUtils.d("hiddenTop");
            mScroller.fling(0, scrollY, (int) velocityX, (int) velocityY, 0, 0, 0,
                    mTopViewHeight);
            mOnHeadFlingListener.onHeadFling(mTopViewHeight);
        } else if (showTop) {
            LogUtils.d("showTop");
            mScroller.fling(0, scrollY, (int) velocityX, (int) velocityY, 0, 0, 0,
                    -mTopViewHeight);
            mOnHeadFlingListener.onHeadFling(0);
        }
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {

        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void setOnHeadFlingListener(OnHeadFlingListener onHeadFlingListener) {
        mOnHeadFlingListener = onHeadFlingListener;
    }

    public int getTopViewHeight() {
        return mTopViewHeight;
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setNotConsumeHeight(int notConsumeHeight) {
        mNotConsumeHeight = notConsumeHeight;
    }

    public interface OnHeadFlingListener {
        void onHeadFling(int scrollY);

        void onHeadZoom();

        void onHeadRedu();
    }
}
