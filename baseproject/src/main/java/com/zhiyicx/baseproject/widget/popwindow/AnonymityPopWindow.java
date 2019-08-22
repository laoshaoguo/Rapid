package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;

/**
 * @Author Jliuer
 * @Date 2017/08/15/16:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnonymityPopWindow extends CustomPopupWindow {

    private String descrStr;
    private int descrColor;
    private AnonymityPopWindowSwitchClickListener anonymityPopWindowSwitchClickListener;

    public AnonymityPopWindow(CBuilder builder) {
        super(builder);
        this.descrStr = builder.descrStr;
        this.descrColor = builder.descrColor;
        this.anonymityPopWindowSwitchClickListener = builder.anonymityPopWindowSwitchClickListener;

        initSwitchButton();
    }

    public static CBuilder builder() {
        return new CBuilder();
    }

    public CBuilder newBuilder() {
        return new CBuilder(this);
    }

    private void initSwitchButton() {
        final SwitchCompat switchCompat = (SwitchCompat) mContentView.findViewById(R.id.ppw_switch);
        final TextView descrip = (TextView) mContentView.findViewById(R.id.tv_ppw_desc);
        descrip.setText(descrStr);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCompat.performClick();
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (anonymityPopWindowSwitchClickListener != null) {
                    anonymityPopWindowSwitchClickListener.onClicked(isChecked);
                }
            }
        });
    }

    public void setSwitchButton(boolean isChecked){
        ((SwitchCompat) mContentView.findViewById(R.id.ppw_switch)).setChecked(isChecked);
    }

    public static final class CBuilder extends Builder {

        private AnonymityPopWindowSwitchClickListener anonymityPopWindowSwitchClickListener;

        private String descrStr;
        private int descrColor;

        public CBuilder() {
        }

        public CBuilder(AnonymityPopWindow builder) {
            this.descrStr = builder.descrStr;
            this.descrColor = builder.descrColor;
        }

        public CBuilder buildAnonymityPopWindowSwitchClickListener(AnonymityPopWindowSwitchClickListener anonymityPopWindowSwitchClickListener) {
            this.anonymityPopWindowSwitchClickListener = anonymityPopWindowSwitchClickListener;
            return this;
        }

        public CBuilder buildDescrColor(int descrColor) {
            this.descrColor = descrColor;
            return this;
        }

        public CBuilder buildDescrStr(String descrStr) {
            this.descrStr = descrStr;
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

        @Override
        public CBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public CBuilder contentView(int contentViewId) {
            super.contentView(contentViewId);
            return this;
        }

        @Override
        public CBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public CBuilder isWrap(boolean isWrap) {
            super.isWrap(isWrap);
            return this;
        }

        @Override
        public CBuilder customListener(CustomPopupWindowListener listener) {
            super.customListener(listener);
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

        @Override
        public AnonymityPopWindow build() {
            super.build();
            return new AnonymityPopWindow(this);
        }
    }

    public interface AnonymityPopWindowSwitchClickListener {
        void onClicked(boolean isChecked);
    }
}
