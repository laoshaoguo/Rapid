package com.haohaishengshi.haohaimusic.widget.pager_recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.haohaishengshi.haohaimusic.R;
import com.zhiyicx.common.utils.log.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/2/16/14:54
 * @Email Jliuer@aliyun.com
 * @Description viewpager 特性的 RecyclerView
 */
public class PagerRecyclerView extends RecyclerView {

    private AdapterWrapper<?> mViewPagerAdapter;

    /**
     * 划出多少触发下一页
     */
    private float mTriggerOffset = 0.2f;

    /**
     * 惯性速率
     */
    private float mFlingFactor = 0.15f;

    private float mTouchSpan;

    private List<OnPageChangedListener> mOnPageChangedListeners;

    private int mSmoothScrollTargetPosition = -1;

    private int mPositionBeforeScroll = -1;

    private boolean mSinglePageFling = true;

    /**
     * 滑动停止后是否需要调整
     */
    boolean mNeedAdjust;

    int mFisrtLeftWhenDragging;

    int mFirstTopWhenDragging;

    /**
     * 当前view
     */
    View mCurView;

    int mMaxLeftWhenDragging = Integer.MIN_VALUE;
    int mMinLeftWhenDragging = Integer.MAX_VALUE;
    int mMaxTopWhenDragging = Integer.MIN_VALUE;
    int mMinTopWhenDragging = Integer.MAX_VALUE;

    private int mPositionOnTouchDown = -1;

    private boolean mHasCalledOnPageChanged = true;

    /**
     * 线性布局是否反转
     */
    private boolean reverseLayout = false;

    private float mLastX;

    private float mLastY;

    private float mSpeed = 250;

    public PagerRecyclerView(Context context) {
        this(context, null);
    }

    public PagerRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
        setNestedScrollingEnabled(false);// 阻止了嵌套滑动，待优化(嵌套导致自身滑动冲突);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerRecyclerView,
                defStyle,
                0);
        mFlingFactor = a.getFloat(R.styleable.PagerRecyclerView_flingFactor, 0.15f);
        mTriggerOffset = a.getFloat(R.styleable.PagerRecyclerView_triggerOffset, 0.25f);
        mSinglePageFling = a.getBoolean(R.styleable.PagerRecyclerView_singlePageFling,
                mSinglePageFling);
        a.recycle();
    }

    /**
     * 惯性速率
     * @param flingFactor
     */
    public void setFlingFactor(float flingFactor) {
        mFlingFactor = flingFactor;
    }

    public float getFlingFactor() {
        return mFlingFactor;
    }

    /**
     * 划出多少触发下一页
     * @param triggerOffset
     */
    public void setTriggerOffset(float triggerOffset) {
        mTriggerOffset = triggerOffset;
    }

    public float getTriggerOffset() {
        return mTriggerOffset;
    }

    public void setSinglePageFling(boolean singlePageFling) {
        mSinglePageFling = singlePageFling;
    }

    public boolean isSinglePageFling() {
        return mSinglePageFling;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            //通过反射获得布局状态的属性
            Field fLayoutState = state.getClass().getDeclaredField("mLayoutState");
            //设置可以访问
            fLayoutState.setAccessible(true);
            Object layoutState = fLayoutState.get(state);
            //通过反射获得停止偏移与停止位置的属性
            Field fAnchorOffset = layoutState.getClass().getDeclaredField("mAnchorOffset");
            Field fAnchorPosition = layoutState.getClass().getDeclaredField("mAnchorPosition");
            fAnchorPosition.setAccessible(true);
            fAnchorOffset.setAccessible(true);

            if (fAnchorOffset.getInt(layoutState) > 0) {//如果偏移值大于0，设置停止位置为上一页
                fAnchorPosition.set(layoutState, fAnchorPosition.getInt(layoutState) - 1);
            } else if (fAnchorOffset.getInt(layoutState) < 0) {//如果偏移值小于0，设置停止位置为下一页
                fAnchorPosition.set(layoutState, fAnchorPosition.getInt(layoutState) + 1);
            }
            fAnchorOffset.setInt(layoutState, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mViewPagerAdapter = ensureAdapter(adapter);
        super.setAdapter(mViewPagerAdapter);
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        mViewPagerAdapter = ensureAdapter(adapter);
        super.swapAdapter(mViewPagerAdapter, removeAndRecycleExistingViews);
    }

    @Override
    public Adapter getAdapter() {
        if (mViewPagerAdapter != null) {
            return mViewPagerAdapter.mAdapter;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    protected AdapterWrapper ensureAdapter(Adapter adapter) {
        return (adapter instanceof AdapterWrapper)
                ? (AdapterWrapper) adapter
                : new AdapterWrapper(this, adapter);

    }

    public AdapterWrapper getWrapperAdapter() {
        return mViewPagerAdapter;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            reverseLayout = ((LinearLayoutManager) layout).getReverseLayout();
        }
    }

    /**
     * 惯性滑动
     *
     * @param velocityX x方向准备的距离
     * @param velocityY
     * @return
     */
    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean flinging = super.fling((int) (velocityX * mFlingFactor), (int) (velocityY *
                mFlingFactor));
        if (flinging) {
            if (getLayoutManager().canScrollHorizontally()) {
                adjustPositionX(velocityX);
            } else {
                adjustPositionY(velocityY);
            }
        }

        return flinging;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if (mPositionBeforeScroll < 0) {
            mPositionBeforeScroll = getCurrentPosition();
        }
        mSmoothScrollTargetPosition = position;
        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager) {
            // exclude item decoration
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            if (getLayoutManager() == null) {
                                return null;
                            }
                            return ((LinearLayoutManager) getLayoutManager())
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        @Override
                        protected void onTargetFound(View targetView, State state, Action action) {
                            if (getLayoutManager() == null) {
                                return;
                            }
                            int dx = calculateDxToMakeVisible(targetView,
                                    getHorizontalSnapPreference());
                            int dy = calculateDyToMakeVisible(targetView,
                                    getVerticalSnapPreference());
                            if (dx > 0) {
                                dx = dx - getLayoutManager()
                                        .getLeftDecorationWidth(targetView);
                            } else {
                                dx = dx + getLayoutManager()
                                        .getRightDecorationWidth(targetView);
                            }
                            if (dy > 0) {
                                dy = dy - getLayoutManager()
                                        .getTopDecorationHeight(targetView);
                            } else {
                                dy = dy + getLayoutManager()
                                        .getBottomDecorationHeight(targetView);
                            }
                            final int distance = (int) Math.sqrt(dx * dx + dy * dy);
                            final int time = calculateTimeForDeceleration(distance);
                            if (time > 0) {
                                action.update(-dx, -dy, time, mDecelerateInterpolator);
                            }
                        }

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return mSpeed / displayMetrics.densityDpi;
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            getLayoutManager().startSmoothScroll(linearSmoothScroller);
        } else {
            super.smoothScrollToPosition(position);
        }
    }

    @Override
    public void scrollToPosition(int position) {
        // 滑动前
        mPositionBeforeScroll = getCurrentPosition();
        // 目标位置
        mSmoothScrollTargetPosition = position;
        super.scrollToPosition(position);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                if (mSmoothScrollTargetPosition >= 0 && mSmoothScrollTargetPosition <
                        getItemCount()) {
                    if (mOnPageChangedListeners != null) {
                        for (OnPageChangedListener onPageChangedListener :
                                mOnPageChangedListeners) {
                            if (onPageChangedListener != null) {
                                onPageChangedListener.OnPageChanged(mPositionBeforeScroll,
                                        getCurrentPosition());
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && getLayoutManager() != null) {
            mPositionOnTouchDown = getLayoutManager().canScrollHorizontally()
                    ? RecyclerViewUtils.getCenterXChildPosition(this)
                    : RecyclerViewUtils.getCenterYChildPosition(this);

            mLastY = ev.getRawY();
            mLastX = ev.getRawX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE && getLayoutManager() != null) {
            if (mOnPageChangedListeners != null && mNeedAdjust) {// 决定最小距离和recyclerview 一致
                for (OnPageChangedListener onPageChangedListener : mOnPageChangedListeners) {
                    if (onPageChangedListener != null) {
                        onPageChangedListener.OnDragging(mPositionOnTouchDown);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)&&(Math.abs(ev.getRawY() - mLastY) <= Math.abs(ev.getRawX() - mLastX));
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // 记录拖动的最大/最小距离
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mCurView != null) {
                mMaxLeftWhenDragging = Math.max(mCurView.getLeft(), mMaxLeftWhenDragging);
                mMaxTopWhenDragging = Math.max(mCurView.getTop(), mMaxTopWhenDragging);
                mMinLeftWhenDragging = Math.min(mCurView.getLeft(), mMinLeftWhenDragging);
                mMinTopWhenDragging = Math.min(mCurView.getTop(), mMinTopWhenDragging);
            }
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_DRAGGING) {//拖动滑动
            mNeedAdjust = true;
            mCurView = getLayoutManager().canScrollHorizontally() ? RecyclerViewUtils
                    .getCenterXChild(this) : RecyclerViewUtils.getCenterYChild(this);

            if (mCurView != null) {
                if (mHasCalledOnPageChanged) {
                    mPositionBeforeScroll = getChildLayoutPosition(mCurView);
                    mHasCalledOnPageChanged = false;
                }

                mFisrtLeftWhenDragging = mCurView.getLeft();
                mFirstTopWhenDragging = mCurView.getTop();
            } else {
                mPositionBeforeScroll = -1;
            }
            mTouchSpan = 0;
        } else if (state == SCROLL_STATE_SETTLING) {
            //滚动到某个位置的动画过程,但没有被触摸滚动.调用 scrollToPosition(int) 应该会触发这个状态
            mNeedAdjust = false;
            if (mCurView != null) {
                if (getLayoutManager().canScrollHorizontally()) {
                    mTouchSpan = mCurView.getLeft() - mFisrtLeftWhenDragging;
                } else {
                    mTouchSpan = mCurView.getTop() - mFirstTopWhenDragging;
                }
            } else {
                mTouchSpan = 0;
            }
            mCurView = null;
        } else if (state == SCROLL_STATE_IDLE) {//不滑动
            if (mNeedAdjust) {
                int targetPosition = getLayoutManager().canScrollHorizontally() ? RecyclerViewUtils
                        .getCenterXChildPosition(this) :
                        RecyclerViewUtils.getCenterYChildPosition(this);
                if (mCurView != null) {
                    targetPosition = getChildAdapterPosition(mCurView);// 预停留位置
                    if (getLayoutManager().canScrollHorizontally()) {
                        int spanX = mCurView.getLeft() - mFisrtLeftWhenDragging;

                        if (spanX > mCurView.getWidth() * mTriggerOffset && mCurView.getLeft() >=
                                mMaxLeftWhenDragging) {
                            if (!reverseLayout) {
                                LogUtils.d("targetPosition--");
                                targetPosition--;
                            } else {
                                targetPosition++;
                            }
                        } else if (spanX < mCurView.getWidth() * -mTriggerOffset && mCurView
                                .getLeft() <= mMinLeftWhenDragging) {
                            if (!reverseLayout) {
                                LogUtils.d("targetPosition++");
                                targetPosition++;
                            } else {
                                targetPosition--;
                            }
                        }// 如果不想滚动，不改变targetPosition，没有达到临界值。
                    } else {
                        int spanY = mCurView.getTop() - mFirstTopWhenDragging;
                        if (spanY > mCurView.getHeight() * mTriggerOffset && mCurView.getTop() >=
                                mMaxTopWhenDragging) {
                            if (!reverseLayout) targetPosition--;
                            else targetPosition++;
                        } else if (spanY < mCurView.getHeight() * -mTriggerOffset && mCurView
                                .getTop() <= mMinTopWhenDragging) {
                            if (!reverseLayout) targetPosition++;
                            else targetPosition--;
                        }
                    }
                }
                smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()));
                mCurView = null;
            } else {
                if (mSmoothScrollTargetPosition != mPositionBeforeScroll) {
                    //发生了滚动
                    if (mOnPageChangedListeners != null) {
                        for (OnPageChangedListener onPageChangedListener :
                                mOnPageChangedListeners) {
                            if (onPageChangedListener != null) {
                                onPageChangedListener.OnPageChanged(mPositionBeforeScroll,
                                        mSmoothScrollTargetPosition);
                            }
                        }
                    }
                    mHasCalledOnPageChanged = true;
                    mPositionBeforeScroll = mSmoothScrollTargetPosition;
                }

                if (mOnPageChangedListeners != null) {
                    for (OnPageChangedListener onPageChangedListener : mOnPageChangedListeners) {
                        if (onPageChangedListener != null) {
                            onPageChangedListener.OnIdle(mSmoothScrollTargetPosition);
                        }
                    }
                }
            }


        }
        // reset
        mMaxLeftWhenDragging = Integer.MIN_VALUE;
        mMinLeftWhenDragging = Integer.MAX_VALUE;
        mMaxTopWhenDragging = Integer.MIN_VALUE;
        mMinTopWhenDragging = Integer.MAX_VALUE;
    }


    private int getItemCount() {
        return mViewPagerAdapter == null ? 0 : mViewPagerAdapter.getItemCount();
    }

    private int getActualItemCount() {
        return getAdapter() == null ? 0 : getAdapter().getItemCount();
    }


    public int getCurrentPosition() {
        int curPosition;
        if (getLayoutManager().canScrollHorizontally()) {
            //如果允许水平移动，返回屏幕X坐标轴中心点的子view
            curPosition = RecyclerViewUtils.getCenterXChildPosition(this);
        } else {
            curPosition = RecyclerViewUtils.getCenterYChildPosition(this);
        }
        if (curPosition < 0) {
            curPosition = mSmoothScrollTargetPosition;
        }
        return curPosition;
    }

    /**
     * 快速的额抛掷运动
     *
     * @param velocityX
     */
    protected void adjustPositionX(int velocityX) {
        if (reverseLayout) velocityX *= -1;
        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = RecyclerViewUtils.getCenterXChildPosition(this);
            int childWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            int flingCount = getFlingCount(velocityX, childWidth);
            int targetPosition = curPosition + flingCount;

            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                targetPosition = flingCount == 0 ? curPosition : mPositionOnTouchDown + flingCount;

            }

            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, getItemCount() - 1);

            if (targetPosition == curPosition
                    && (!mSinglePageFling || mPositionOnTouchDown == curPosition)) {
                View centerXChild = RecyclerViewUtils.getCenterXChild(this);
                if (centerXChild != null) {
                    if (mTouchSpan > centerXChild.getWidth() * mTriggerOffset * mTriggerOffset &&
                            targetPosition != 0) {
                        if (!reverseLayout) targetPosition--;
                        else targetPosition++;
                    } else if (mTouchSpan < centerXChild.getWidth() * -mTriggerOffset &&
                            targetPosition != getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++;
                        else targetPosition--;
                    }
                }
            }

            smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()));
        }
    }

    protected void adjustPositionY(int velocityY) {
        if (reverseLayout) velocityY *= -1;
        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = RecyclerViewUtils.getCenterYChildPosition(this);
            int childHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            int flingCount = getFlingCount(velocityY, childHeight);
            int targetPosition = curPosition + flingCount;
            if (mSinglePageFling) {// 只滑行单个
                flingCount = Math.max(-1, Math.min(1, flingCount));// 0 、 1
                targetPosition = flingCount == 0 ? curPosition : mPositionOnTouchDown + flingCount;
            }

            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, getItemCount() - 1);

            if (targetPosition == curPosition
                    && (!mSinglePageFling || mPositionOnTouchDown == curPosition)) {
                View centerYChild = RecyclerViewUtils.getCenterYChild(this);
                if (centerYChild != null) {
                    if (mTouchSpan > centerYChild.getHeight() * mTriggerOffset && targetPosition
                            != 0) {
                        if (!reverseLayout) targetPosition--;
                        else targetPosition++;
                    } else if (mTouchSpan < centerYChild.getHeight() * -mTriggerOffset &&
                            targetPosition != getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++;
                        else targetPosition--;
                    }
                }
            }

            smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()));
        }
    }

    /**
     * 获取抛掷滑动的view个数
     *
     * @param velocity 总的抛掷距离
     * @param cellSize 单个view抛掷距离
     * @return 抛掷滑动的view个数
     */
    private int getFlingCount(int velocity, int cellSize) {
        if (velocity == 0) {
            return 0;
        }
        int sign = velocity > 0 ? 1 : -1;
        //参数x的最小整数,即对浮点数向上取整
        return (int) (sign * Math.ceil((velocity * sign * mFlingFactor / cellSize)
                - mTriggerOffset));
    }

    private int safeTargetPosition(int position, int count) {
        if (position < 0) {
            return 0;
        }
        if (position >= count) {
            return count - 1;
        }
        return position;
    }

    /**
     * 可修改滑动速度，屏幕是固定的，时间越长，则速度越慢。
     *
     * @param f 单个item滑过一个屏幕的时间，系统默认值为 25
     * @return
     */
    public float setSpeed(float f) {
        mSpeed = f;
        return mSpeed;
    }

    public void addOnPageChangedListener(OnPageChangedListener listener) {
        if (mOnPageChangedListeners == null) {
            mOnPageChangedListeners = new ArrayList<>();
        }
        mOnPageChangedListeners.add(listener);
    }

    public void removeOnPageChangedListener(OnPageChangedListener listener) {
        if (mOnPageChangedListeners != null) {
            mOnPageChangedListeners.remove(listener);
        }
    }

    public void clearOnPageChangedListeners() {
        if (mOnPageChangedListeners != null) {
            mOnPageChangedListeners.clear();
        }
    }

    public View getmCurView() {
        return mCurView;
    }

    public interface OnPageChangedListener {
        /**
         * 页面变换
         * @param oldPosition
         * @param newPosition
         */
        void OnPageChanged(int oldPosition, int newPosition);

        /**
         * 拖动中
         * @param downPosition
         */
        void OnDragging(int downPosition);

        /**
         * 滑动停止
         * @param position
         */
        void OnIdle(int position);

    }
}

