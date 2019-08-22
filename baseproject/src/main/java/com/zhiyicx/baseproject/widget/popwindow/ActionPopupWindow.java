package com.zhiyicx.baseproject.widget.popwindow;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @Describe 动作表单;
 * 使用场景：上传图片、删除操作确认、保存图片、性别选择
 * 动态/评论是否重发确认等情况
 * @Author Jungle68
 * @Date 2017/1/4
 * @Contact master.jungle68@gmail.com
 */

public class ActionPopupWindow extends PopupWindow {
    public static final float POPUPWINDOW_ALPHA = .8f;
    public static final int NO_ANIMATION = -1;

    protected Activity mActivity;
    protected View mParentView;
    protected View mContentView;
    protected String mItem1Str;
    protected String mItem2Str;
    protected String mItem3Str;
    protected String mItem4Str;
    protected String mItem5Str;
    protected String mItem6Str;
    protected String mDesStr;
    protected String mBottomStr;


    protected int mItem1Color;
    protected int mItem2Color;
    protected int mAnimationStyle;
    protected int mItem3Color;
    protected int mItem4Color;
    protected int mItem5Color;
    protected int mItem6Color;
    protected int mItemDesColor;
    protected int mItemBottomColor;

    protected boolean mIsOutsideTouch;
    protected boolean mIsFocus;
    protected float mAlpha;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    protected ActionPopupWindowItem1ClickListener mActionPopupWindowItem1ClickListener;
    protected ActionPopupWindowItem2ClickListener mActionPopupWindowItem2ClickListener;
    protected ActionPopupWindowItem3ClickListener mActionPopupWindowItem3ClickListener;

    protected ActionPopupWindowItem4ClickListener mActionPopupWindowItem4ClickListener;
    protected ActionPopupWindowDesClickListener mActionPopupWindowDesClickListener;
    protected ActionPopupWindowItem5ClickListener mActionPopupWindowItem5ClickListener;
    protected ActionPopupWindowItem6ClickListener mActionPopupWindowItem6ClickListener;
    protected ActionPopupWindowShowOrDismissListener mActionPopupWindowDismissListener;

    protected ActionPopupWindow.ActionPopupWindowBottomClickListener mActionPopupWindowBottomClickListener;

    public ActionPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.parentView;
        this.mItem1Str = builder.mItem1Str;
        this.mItem2Str = builder.mItem2Str;
        this.mItem3Str = builder.mItem3Str;
        this.mItem4Str = builder.mItem4Str;
        this.mItem5Str = builder.mItem5Str;
        this.mItem6Str = builder.mItem6Str;
        this.mAnimationStyle = builder.mAnimationStyle;
        this.mDesStr = builder.mDesStr;
        this.mBottomStr = builder.mBottomStr;
        this.mItem1Color = builder.mItem1Color;
        this.mItem2Color = builder.mItem2Color;
        this.mItem3Color = builder.mItem3Color;
        this.mItem4Color = builder.mItem4Color;
        this.mItem5Color = builder.mItem5Color;
        this.mItem6Color = builder.mItem6Color;
        this.mItemDesColor = builder.mItemDesColor;
        this.mItemBottomColor = builder.mItemBottomColor;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mActionPopupWindowItem1ClickListener = builder.mActionPopupWindowItem1ClickListener;
        this.mActionPopupWindowItem2ClickListener = builder.mActionPopupWindowItem2ClickListener;
        this.mActionPopupWindowItem3ClickListener = builder.mActionPopupWindowItem3ClickListener;
        this.mActionPopupWindowItem4ClickListener = builder.mActionPopupWindowItem4ClickListener;
        this.mActionPopupWindowItem5ClickListener = builder.mActionPopupWindowItem5ClickListener;
        this.mActionPopupWindowItem6ClickListener = builder.mActionPopupWindowItem6ClickListener;
        this.mActionPopupWindowDesClickListener = builder.mActionPopupWindowDesClickListener;
        this.mActionPopupWindowBottomClickListener = builder.mActionPopupWindowBottomClickListener;
        this.mActionPopupWindowDismissListener = builder.mActionPopupWindowDismissListener;
        if (canInitView()) {
            initView();
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    /**
     * 子类重新调用initView
     *
     * @return
     */
    protected boolean canInitView() {
        return true;
    }

    protected void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        setContentView(mContentView);
        if (mAnimationStyle == NO_ANIMATION) {
            return;
        }
        setAnimationStyle(mAnimationStyle > 0 ? mAnimationStyle : R.style.style_actionPopupAnimation);
    }

    public void setItem1Str(String item1Str) {
        ((TextView) mContentView.findViewById(R.id.tv_pop_item1)).setText(item1Str);
    }

    public void setItem1StrColor(int res) {
        ((TextView) mContentView.findViewById(R.id.tv_pop_item1)).setTextColor(res);
    }

    protected int getLayoutId() {
        return R.layout.ppw_for_action;
    }

    protected void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
        initItemView(R.id.tv_pop_item1, mItem1Color, mItem1Str, mActionPopupWindowItem1ClickListener);
        initItemView(R.id.tv_pop_item2, mItem2Color, mItem2Str, mActionPopupWindowItem2ClickListener);
        initItemView(R.id.tv_pop_item3, mItem3Color, mItem3Str, mActionPopupWindowItem3ClickListener);
        initItemView(R.id.tv_pop_item4, mItem4Color, mItem4Str, mActionPopupWindowItem4ClickListener);
        initItemView(R.id.tv_pop_des, mItemDesColor, mDesStr, mActionPopupWindowDesClickListener);
        initItemView(R.id.tv_pop_item5, mItem5Color, mItem5Str, mActionPopupWindowItem5ClickListener);
        initItemView(R.id.tv_pop_item6, mItem6Color, mItem6Str, mActionPopupWindowItem6ClickListener);
        initItemView(R.id.tv_pop_bottom, mItem5Color, mBottomStr, mActionPopupWindowBottomClickListener);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
    }

    protected void initItemView(@IdRes int viewId, int viewColor, String text, final ItemClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            TextView itemView = (TextView) mContentView.findViewById(viewId);
            itemView.setVisibility(View.VISIBLE);
            itemView.setText(text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClicked();
                    }
                }
            });
            if (viewColor != 0) {
                itemView.setTextColor(viewColor);
            }
        }
    }

    /**
     * 设置屏幕的透明度
     *
     * @param alpha 需要设置透明度
     */
    protected void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public static ActionPopupWindow.Builder builder() {
        return new ActionPopupWindow.Builder();
    }

    /**
     * 默认显示到底部
     */
    public void show() {
        setWindowAlpha(mAlpha);
        if (mParentView == null) {
            showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        } else {
            showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
        }
    }

    public void showTop() {
        if (mParentView == null) {
            showAtLocation(mContentView, Gravity.TOP, 0, 0);
        } else {
            initTopInAnimation(mContentView);
            showAsDropDown(mParentView, 0, 0);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (mActionPopupWindowDismissListener != null) {
            mActionPopupWindowDismissListener.onShow();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        if (mActionPopupWindowDismissListener != null) {
            mActionPopupWindowDismissListener.onShow();
        }
    }

    @Override
    public void dismiss() {
        if (mAnimationStyle == NO_ANIMATION) {
            mContentView.clearAnimation();
        }
        super.dismiss();
        if (mActionPopupWindowDismissListener != null) {
            mActionPopupWindowDismissListener.onDismiss();
        }
    }

    /**
     * 隐藏popupwindow
     */
    public void hide() {
        dismiss();
    }

    public static class Builder {
        protected Activity mActivity;
        protected View parentView;
        protected String mItem1Str;
        protected String mItem2Str;
        protected String mItem3Str;
        protected String mItem4Str;
        protected String mItem5Str;
        protected String mItem6Str;
        protected String mDesStr;
        protected String mBottomStr;

        protected int mItem1Color;
        protected int mItem2Color;
        protected int mAnimationStyle;
        protected int mItem3Color;
        protected int mItem4Color;
        protected int mItem5Color;
        protected int mItem6Color;
        protected int mItemDesColor;
        protected int mItemBottomColor;

        protected float mAlpha = POPUPWINDOW_ALPHA;
        protected boolean mIsOutsideTouch = true;// 默认为true
        protected boolean mIsFocus = true;// 默认为true
        protected ActionPopupWindowItem1ClickListener mActionPopupWindowItem1ClickListener;
        protected ActionPopupWindowItem2ClickListener mActionPopupWindowItem2ClickListener;
        protected ActionPopupWindowItem3ClickListener mActionPopupWindowItem3ClickListener;
        protected ActionPopupWindowItem4ClickListener mActionPopupWindowItem4ClickListener;
        protected ActionPopupWindowItem5ClickListener mActionPopupWindowItem5ClickListener;
        protected ActionPopupWindowItem6ClickListener mActionPopupWindowItem6ClickListener;
        protected ActionPopupWindowShowOrDismissListener mActionPopupWindowDismissListener;
        protected ActionPopupWindowDesClickListener mActionPopupWindowDesClickListener;
        protected ActionPopupWindow.ActionPopupWindowBottomClickListener mActionPopupWindowBottomClickListener;

        protected Builder() {
        }

        private Builder(ActionPopupWindow popupWindow) {
            this.mActivity = popupWindow.mActivity;
            this.parentView = popupWindow.mParentView;
            this.mItem1Str = popupWindow.mItem1Str;
            this.mItem2Str = popupWindow.mItem2Str;
            this.mItem3Str = popupWindow.mItem3Str;
            this.mItem4Str = popupWindow.mItem4Str;
            this.mItem5Str = popupWindow.mItem5Str;
            this.mItem6Str = popupWindow.mItem6Str;
            this.mAnimationStyle = popupWindow.mAnimationStyle;
            this.mDesStr = popupWindow.mDesStr;
            this.mBottomStr = popupWindow.mBottomStr;
            this.mItem1Color = popupWindow.mItem1Color;
            this.mItem2Color = popupWindow.mItem2Color;
            this.mItem3Color = popupWindow.mItem3Color;
            this.mItem4Color = popupWindow.mItem4Color;
            this.mItem5Color = popupWindow.mItem5Color;
            this.mItem6Color = popupWindow.mItem6Color;
            this.mItemDesColor = popupWindow.mItemDesColor;
            this.mItemBottomColor = popupWindow.mItemBottomColor;
            this.mIsOutsideTouch = popupWindow.mIsOutsideTouch;
            this.mIsFocus = popupWindow.mIsFocus;
            this.mAlpha = popupWindow.mAlpha;
            this.mActionPopupWindowItem1ClickListener = popupWindow.mActionPopupWindowItem1ClickListener;
            this.mActionPopupWindowItem2ClickListener = popupWindow.mActionPopupWindowItem2ClickListener;
            this.mActionPopupWindowItem3ClickListener = popupWindow.mActionPopupWindowItem3ClickListener;
            this.mActionPopupWindowItem4ClickListener = popupWindow.mActionPopupWindowItem4ClickListener;
            this.mActionPopupWindowItem5ClickListener = popupWindow.mActionPopupWindowItem5ClickListener;
            this.mActionPopupWindowItem6ClickListener = popupWindow.mActionPopupWindowItem6ClickListener;
            this.mActionPopupWindowDesClickListener = popupWindow.mActionPopupWindowDesClickListener;
            this.mActionPopupWindowBottomClickListener = popupWindow.mActionPopupWindowBottomClickListener;
            this.mActionPopupWindowDismissListener = popupWindow.mActionPopupWindowDismissListener;
        }

        public ActionPopupWindow.Builder with(Activity activity) {
            this.mActivity = activity;
            return this;
        }

        public ActionPopupWindow.Builder parentView(View parentView) {
            this.parentView = parentView;
            return this;
        }

        public ActionPopupWindow.Builder item1Str(String item1Str) {
            this.mItem1Str = item1Str;
            return this;
        }

        public ActionPopupWindow.Builder item2Str(String item2Str) {
            this.mItem2Str = item2Str;
            return this;
        }

        public ActionPopupWindow.Builder item3Str(String item3Str) {
            this.mItem3Str = item3Str;
            return this;
        }

        public ActionPopupWindow.Builder item4Str(String item4Str) {
            this.mItem4Str = item4Str;
            return this;
        }

        public ActionPopupWindow.Builder item5Str(String item5Str) {
            this.mItem5Str = item5Str;
            return this;
        }

        public ActionPopupWindow.Builder item6Str(String item6Str) {
            this.mItem6Str = item6Str;
            return this;
        }

        public ActionPopupWindow.Builder desStr(String desStr) {
            this.mDesStr = desStr;
            return this;
        }

        public ActionPopupWindow.Builder bottomStr(String bottomStr) {
            this.mBottomStr = bottomStr;
            return this;
        }

        public ActionPopupWindow.Builder item1Color(int color) {
            this.mItem1Color = color;
            return this;
        }

        public ActionPopupWindow.Builder animationStyle(int animationStyle) {
            this.mAnimationStyle = animationStyle;
            return this;
        }

        public ActionPopupWindow.Builder item2Color(int color) {
            this.mItem2Color = color;
            return this;
        }

        public ActionPopupWindow.Builder item3Color(int color) {
            this.mItem3Color = color;
            return this;
        }

        public ActionPopupWindow.Builder item4Color(int color) {
            this.mItem4Color = color;
            return this;
        }

        public ActionPopupWindow.Builder item5Color(int color) {
            this.mItem5Color = color;
            return this;
        }

        public ActionPopupWindow.Builder item6Color(int color) {
            this.mItem6Color = color;
            return this;
        }

        public ActionPopupWindow.Builder itemDesColor(int color) {
            this.mItemDesColor = color;
            return this;
        }

        public ActionPopupWindow.Builder item1ClickListener(ActionPopupWindowItem1ClickListener actionPopupWindowItem1ClickListener) {
            this.mActionPopupWindowItem1ClickListener = actionPopupWindowItem1ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item2ClickListener(ActionPopupWindowItem2ClickListener actionPopupWindowItem2ClickListener) {
            this.mActionPopupWindowItem2ClickListener = actionPopupWindowItem2ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item3ClickListener(ActionPopupWindowItem3ClickListener actionPopupWindowItem3ClickListener) {
            this.mActionPopupWindowItem3ClickListener = actionPopupWindowItem3ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item4ClickListener(ActionPopupWindowItem4ClickListener actionPopupWindowItem4ClickListener) {
            this.mActionPopupWindowItem4ClickListener = actionPopupWindowItem4ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item5ClickListener(ActionPopupWindowItem5ClickListener actionPopupWindowItem5ClickListener) {
            this.mActionPopupWindowItem5ClickListener = actionPopupWindowItem5ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder item6ClickListener(ActionPopupWindowItem6ClickListener actionPopupWindowItem6ClickListener) {
            this.mActionPopupWindowItem6ClickListener = actionPopupWindowItem6ClickListener;
            return this;
        }

        public ActionPopupWindow.Builder dismissListener(ActionPopupWindowShowOrDismissListener actionPopupWindowDismissListener) {
            this.mActionPopupWindowDismissListener = actionPopupWindowDismissListener;
            return this;
        }

        public ActionPopupWindow.Builder desClickListener(ActionPopupWindowDesClickListener actionPopupWindowDesClickListener) {
            this.mActionPopupWindowDesClickListener = actionPopupWindowDesClickListener;
            return this;
        }

        public ActionPopupWindow.Builder bottomClickListener(ActionPopupWindow.ActionPopupWindowBottomClickListener
                                                                     actionPopupWindowBottomClickListener) {
            this.mActionPopupWindowBottomClickListener = actionPopupWindowBottomClickListener;
            return this;
        }

        public ActionPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public ActionPopupWindow.Builder isFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public ActionPopupWindow.Builder backgroundAlpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public ActionPopupWindow build() {
            return new ActionPopupWindow(this);
        }
    }

    public interface ItemClickListener {
        void onItemClicked();
    }

    public interface ActionPopupWindowItem1ClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowItem2ClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowItem3ClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowItem4ClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowItem5ClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowItem6ClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowDesClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowBottomClickListener extends ItemClickListener {
    }

    public interface ActionPopupWindowShowOrDismissListener {
        void onDismiss();

        void onShow();
    }

    private void initTopInAnimation(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                int distance = view.getTop() + view.getHeight();
                AnimatorSet mAnimatorSet = new AnimatorSet();
                ViewCompat.setPivotX(view, view.getWidth() / 2.0f);
                ViewCompat.setPivotY(view, view.getHeight() / 2.0f);
                mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimatorSet.setDuration(500);
                mAnimatorSet.playTogether(
                        ObjectAnimator.ofFloat(view, "translationY", -distance, 0)
                );
                mAnimatorSet.start();
            }
        });
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    //    private void initToOutAnimation(final View view) {
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                AnimatorSet mAnimatorSet = new AnimatorSet();
//                ViewCompat.setPivotX(view, view.getWidth() / 2.0f);
//                ViewCompat.setPivotY(view, view.getHeight() / 2.0f);
//                mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
//                mAnimatorSet.setDuration(15000);
//                mAnimatorSet.playTogether(
//                        ObjectAnimator.ofFloat(view, "alpha", 1f, 0f),
//                        ObjectAnimator.ofFloat(view, "translationY", 0, -view.getBottom())
//                );
//                mAnimatorSet.start();
//            }
//        });
//    }

}