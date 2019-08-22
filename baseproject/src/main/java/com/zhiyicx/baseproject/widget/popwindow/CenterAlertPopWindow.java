package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;

/**
 * @author Catherine
 * @describe 中间的提示框 按钮默认确认和取消
 * @date 2017/8/21
 * @contact email:648129313@qq.com
 */

public class CenterAlertPopWindow extends CustomPopupWindow {

    private CenterPopWindowItemClickListener mCenterPopWindowItemClickListener;

    private String titleStr;
    private String desStr;
    private String itemRight;
    private String itemLeft;

    private int titleColor;
    private int desColor;
    private int itemRightColor;
    private int itemLeftColor;

    public static CBuilder builder() {
        return new CBuilder();
    }

    protected CenterAlertPopWindow(CBuilder builder) {
        super(builder);
        this.titleStr = builder.titleStr;
        this.desStr = builder.desStr;
        this.itemRight = builder.itemRight;
        this.titleColor = builder.titleColor;
        this.desColor = builder.desColor;
        this.itemRightColor = builder.itemRightColor;
        this.mCenterPopWindowItemClickListener = builder.mCenterPopWindowItemClickListener;
        this.itemLeft = builder.itemLeft;
        this.itemLeftColor = builder.itemColorLeft;
        initView();
    }

    private void initView() {
        initTextView(titleStr, titleColor, R.id.ppw_center_title);
        initTextView(desStr, desColor, R.id.ppw_center_description);
        initTextView(itemRight, itemRightColor, R.id.ppw_center_item);
        initBottomLeftView(itemLeft, itemLeftColor, R.id.ppw_center_item_left, mCenterPopWindowItemClickListener);
        initBottomRightView(itemRight, itemRightColor, R.id.ppw_center_item, mCenterPopWindowItemClickListener);
    }

    private void initBottomRightView(String text, int colorId, int resId, final CenterPopWindowItemClickListener listener) {
        TextView textView = (TextView) mContentView.findViewById(resId);
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
        if (colorId != 0) {
            textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRightClicked();
                }
            }
        });
    }

    private void initBottomLeftView(String text, int colorId, int resId, final CenterPopWindowItemClickListener listener) {
        TextView textView = (TextView) mContentView.findViewById(resId);
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
        if (colorId != 0) {
            textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onLeftClicked();
                }
            }
        });
    }

    protected void initTextView(String text, int colorId, int resId) {
        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) mContentView.findViewById(resId);
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            if (colorId != 0) {
                textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
            }
        }
    }

    public static final class CBuilder extends Builder {

        private CenterPopWindowItemClickListener mCenterPopWindowItemClickListener;
        private String titleStr;
        private String desStr;
        private String itemRight;
        private String itemLeft;

        private int titleColor;
        private int desColor;
        private int itemRightColor;
        private int itemColorLeft;

        public CBuilder buildCenterPopWindowItem1ClickListener(CenterPopWindowItemClickListener mCenterPopWindowItem1ClickListener) {
            this.mCenterPopWindowItemClickListener = mCenterPopWindowItem1ClickListener;
            return this;
        }

        @Override
        public CBuilder backgroundAlpha(float alpha) {
            super.backgroundAlpha(alpha);
            return this;
        }

        @Override
        public CBuilder width(int width) {
            super.width(width);
            return this;
        }

        @Override
        public CBuilder height(int height) {
            super.height(height);
            return this;
        }

        public CBuilder desColor(int descrColor) {
            this.desColor = descrColor;
            return this;
        }

        public CBuilder itemRightColor(int itemRightColor) {
            this.itemRightColor = itemRightColor;
            return this;
        }

        public CBuilder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public CBuilder titleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public CBuilder desStr(String desStr) {
            this.desStr = desStr;
            return this;
        }

        public CBuilder itemRight(String itemRightStr) {
            this.itemRight = itemRightStr;
            return this;
        }

        public CBuilder itemLeft(String itemLeft){
            this.itemLeft = itemLeft;
            return this;
        }

        public CBuilder itemColorLeft(int itemColorLeft){
            this.itemColorLeft = itemColorLeft;
            return this;
        }


        @Override
        public CBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public CBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public CBuilder isFocus(boolean isFocus) {
            super.isFocus(isFocus);
            return this;
        }

        @Override
        public CBuilder backgroundDrawable(Drawable backgroundDrawable) {
            super.backgroundDrawable(backgroundDrawable);
            return this;
        }

        @Override
        public CBuilder animationStyle(int animationStyle) {
            super.animationStyle(animationStyle);
            return this;
        }

        public CBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public CenterAlertPopWindow build() {
            contentViewId = R.layout.ppw_for_center_alert;
            isWrap = true;
            return new CenterAlertPopWindow(this);
        }
    }

    public interface CenterPopWindowItemClickListener {
        void onRightClicked();
        void onLeftClicked();
    }

}
