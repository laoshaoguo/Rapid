package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.internal.InternalClassics;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.UIUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/10/24
 * @Contact master.jungle68@gmail.com
 */
public class TSRefreshFooter extends RelativeLayout implements RefreshFooter {

    private TextView mTvtip;


    public TSRefreshFooter(Context context) {
        this(context, null);
    }

    public TSRefreshFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TSRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTvtip = new TextView(getContext());
        mTvtip.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.drawable.frame_loading_grey), null, null, null);
        mTvtip.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.spacing_small));
        mTvtip.setText(getResources().getString(R.string.loading));
        mTvtip.setTextColor(ContextCompat.getColor(getContext(), R.color.general_for_loading_more));
        mTvtip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mTvtip.setGravity(Gravity.CENTER);
        addView(mTvtip);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTvtip.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height);
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    public void updteFooterHeight(int height, int bottomargin) {
        mTvtip.getLayoutParams().height = height;
        ((LayoutParams) mTvtip.getLayoutParams()).bottomMargin = bottomargin;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
        setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        AnimationDrawable background = (AnimationDrawable) mTvtip.getCompoundDrawables()[0];
        if (!background.isRunning()) {
            background.start();
        }

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if (success) {
            // 刷新成功
        } else {
            // 刷新失败
        }
        AnimationDrawable background = (AnimationDrawable) mTvtip.getCompoundDrawables()[0];
        if (background.isRunning()) {
            background.stop();
        }
        return 500;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
