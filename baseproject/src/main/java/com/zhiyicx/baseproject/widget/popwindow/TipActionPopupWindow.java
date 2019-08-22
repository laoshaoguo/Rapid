package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @Author Jliuer
 * @Date 2018/05/30/14:45
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TipActionPopupWindow extends ActionPopupWindow {

    private boolean showTip1;
    private boolean showTip2;
    private boolean showTip3;
    private boolean showTip4;
    private boolean showTip5;
    private boolean showTip6;
    private boolean showTipDes;
    private boolean showTipBottom;

    public TipActionPopupWindow(TBuilder builder) {
        super(builder);
        this.showTip1 = builder.showTip1;
        this.showTip2 = builder.showTip2;
        this.showTip3 = builder.showTip3;
        this.showTip4 = builder.showTip4;
        this.showTip5 = builder.showTip5;
        this.showTip6 = builder.showTip6;
        this.showTipDes = builder.showTipDes;
        this.showTipBottom = builder.showTipBottom;
        initView();
    }

    @Override
    protected boolean canInitView() {
        return false;
    }

    public static TBuilder builder(){
        return new TBuilder();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ppw_for_tip_action;
    }

    @Override
    protected void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
        initItemView(R.id.ll_pop_item1, R.id.tv_pop_item1, R.id.v_mine_tip1, showTip1, mItem1Color, mItem1Str, mActionPopupWindowItem1ClickListener);
        initItemView(R.id.ll_pop_item2, R.id.tv_pop_item2, R.id.v_mine_tip2, showTip2, mItem2Color, mItem2Str, mActionPopupWindowItem2ClickListener);
        initItemView(R.id.ll_pop_item3, R.id.tv_pop_item3, R.id.v_mine_tip3, showTip3, mItem3Color, mItem3Str, mActionPopupWindowItem3ClickListener);
        initItemView(R.id.ll_pop_item4, R.id.tv_pop_item4, R.id.v_mine_tip4, showTip4, mItem4Color, mItem4Str, mActionPopupWindowItem4ClickListener);
        initItemView(R.id.ll_pop_des, R.id.tv_pop_des, R.id.v_pop_des_tip, showTipDes, mItemDesColor, mDesStr, mActionPopupWindowDesClickListener);
        initItemView(R.id.ll_pop_item5, R.id.tv_pop_item5, R.id.v_mine_tip5, showTip5, mItem5Color, mItem5Str, mActionPopupWindowItem5ClickListener);
        initItemView(R.id.ll_pop_item6, R.id.tv_pop_item6, R.id.v_mine_tip6, showTip6, mItem6Color, mItem6Str, mActionPopupWindowItem6ClickListener);
        initItemView(R.id.ll_pop_bottom, R.id.tv_pop_bottom, R.id.tv_pop_bottom_tip, showTipBottom, mItem5Color, mBottomStr, mActionPopupWindowBottomClickListener);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
    }

    protected void initItemView(@IdRes int layoutId, @IdRes int viewId, @IdRes int tipId,
                                boolean showTip, int viewColor, String text,
                                final ItemClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) mContentView.findViewById(viewId);
            View itemView = mContentView.findViewById(layoutId);
            View tipView = mContentView.findViewById(tipId);
            tipView.setVisibility(showTip ? View.VISIBLE : View.GONE);
            itemView.setVisibility(View.VISIBLE);
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClicked();
                    }
                }
            });
            if (viewColor != 0) {
                textView.setTextColor(viewColor);
            }
        }
    }

    public static final class TBuilder extends Builder {
        private boolean showTip1;
        private boolean showTip2;
        private boolean showTip3;
        private boolean showTip4;
        private boolean showTip5;
        private boolean showTip6;
        private boolean showTipDes;
        private boolean showTipBottom;


        protected TBuilder() {
            super();
        }

        public TBuilder showTip1(boolean showTip1) {
            this.showTip1 = showTip1;
            return this;
        }

        public TBuilder showTip2(boolean showTip2) {
            this.showTip2 = showTip2;
            return this;
        }

        public TBuilder showTip3(boolean showTip3) {
            this.showTip3 = showTip3;
            return this;
        }

        public TBuilder showTip4(boolean showTip4) {
            this.showTip4 = showTip4;
            return this;
        }

        public TBuilder showTip5(boolean showTip5) {
            this.showTip5 = showTip5;
            return this;
        }

        public TBuilder showTip6(boolean showTip6) {
            this.showTip6 = showTip6;
            return this;
        }

        public TBuilder showTipDes(boolean showTipDes) {
            this.showTipDes = showTipDes;
            return this;
        }

        public TBuilder showTipBottom(boolean showTipBottom) {
            this.showTipBottom = showTipBottom;
            return this;
        }

        @Override
        public TBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public TBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public TBuilder item1Str(String item1Str) {
            super.item1Str(item1Str);
            return this;
        }

        @Override
        public TBuilder item2Str(String item2Str) {
            super.item2Str(item2Str);
            return this;
        }

        @Override
        public TBuilder item3Str(String item3Str) {
            super.item3Str(item3Str);
            return this;
        }

        @Override
        public TBuilder item4Str(String item4Str) {
            super.item4Str(item4Str);
            return this;
        }

        @Override
        public TBuilder item5Str(String item5Str) {
            super.item5Str(item5Str);
            return this;
        }

        @Override
        public TBuilder item6Str(String item6Str) {
            super.item6Str(item6Str);
            return this;
        }

        @Override
        public TBuilder desStr(String desStr) {
            super.desStr(desStr);
            return this;
        }

        @Override
        public TBuilder bottomStr(String bottomStr) {
            super.bottomStr(bottomStr);
            return this;
        }

        @Override
        public TBuilder item1Color(int color) {
            super.item1Color(color);
            return this;
        }

        @Override
        public TBuilder animationStyle(int animationStyle) {
            super.animationStyle(animationStyle);
            return this;
        }

        @Override
        public TBuilder item2Color(int color) {
            super.item2Color(color);
            return this;
        }

        @Override
        public TBuilder item3Color(int color) {
            super.item3Color(color);
            return this;
        }

        @Override
        public TBuilder item4Color(int color) {
            super.item4Color(color);
            return this;
        }

        @Override
        public TBuilder item5Color(int color) {
            super.item5Color(color);
            return this;
        }

        @Override
        public TBuilder item6Color(int color) {
            super.item6Color(color);
            return this;
        }

        @Override
        public TBuilder itemDesColor(int color) {
            super.itemDesColor(color);
            return this;
        }

        @Override
        public TBuilder item1ClickListener(ActionPopupWindowItem1ClickListener actionPopupWindowItem1ClickListener) {
            super.item1ClickListener(actionPopupWindowItem1ClickListener);
            return this;
        }

        @Override
        public TBuilder item2ClickListener(ActionPopupWindowItem2ClickListener actionPopupWindowItem2ClickListener) {
            super.item2ClickListener(actionPopupWindowItem2ClickListener);
            return this;
        }

        @Override
        public TBuilder item3ClickListener(ActionPopupWindowItem3ClickListener actionPopupWindowItem3ClickListener) {
            super.item3ClickListener(actionPopupWindowItem3ClickListener);
            return this;
        }

        @Override
        public TBuilder item4ClickListener(ActionPopupWindowItem4ClickListener actionPopupWindowItem4ClickListener) {
            super.item4ClickListener(actionPopupWindowItem4ClickListener);
            return this;
        }

        @Override
        public TBuilder item5ClickListener(ActionPopupWindowItem5ClickListener actionPopupWindowItem5ClickListener) {
            super.item5ClickListener(actionPopupWindowItem5ClickListener);
            return this;
        }

        @Override
        public TBuilder item6ClickListener(ActionPopupWindowItem6ClickListener actionPopupWindowItem6ClickListener) {
            super.item6ClickListener(actionPopupWindowItem6ClickListener);
            return this;
        }

        @Override
        public TBuilder dismissListener(ActionPopupWindowShowOrDismissListener actionPopupWindowDismissListener) {
            super.dismissListener(actionPopupWindowDismissListener);
            return this;
        }

        @Override
        public TBuilder desClickListener(ActionPopupWindowDesClickListener actionPopupWindowDesClickListener) {
            super.desClickListener(actionPopupWindowDesClickListener);
            return this;
        }

        @Override
        public TBuilder bottomClickListener(ActionPopupWindowBottomClickListener actionPopupWindowBottomClickListener) {
            super.bottomClickListener(actionPopupWindowBottomClickListener);
            return this;
        }

        @Override
        public TBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public TBuilder isFocus(boolean isFocus) {
            super.isFocus(isFocus);
            return this;
        }

        @Override
        public TBuilder backgroundAlpha(float alpha) {
            super.backgroundAlpha(alpha);
            return this;
        }

        @Override
        public TipActionPopupWindow build() {
            super.build();
            return new TipActionPopupWindow(this);
        }
    }
}
