package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/05/15/14:13
 * @Email Jliuer@aliyun.com
 * @Description 支付中间弹窗
 */
public class PayPopWindow extends CustomPopupWindow {

    private CenterPopWindowItem1ClickListener mCenterPopWindowItem1ClickListener;
    private CenterPopWindowItem2ClickListener mCenterPopWindowItem2ClickListener;
    private CenterPopWindowLinkClickListener mCenterPopWindowLinkClickListener;

    private String titleStr;
    private String moneyStr;
    private String descrStr;
    private String item1Str;
    private String item2Str;
    private String linksStr;

    private int titleColor;
    private int moneyColor;
    private int descrColor;
    private int item1Color;
    private int item2Color;
    private int linksColor1;
    private int linksColor2;

    public static CBuilder builder() {
        return new CBuilder();
    }

    public CBuilder newBuilder(){
        return new CBuilder(this);
    }

    private PayPopWindow(CBuilder builder) {
        super(builder);
        this.titleStr = builder.titleStr;
        this.moneyStr = builder.moneyStr;
        this.descrStr = builder.descrStr;
        this.linksStr = builder.linksStr;
        this.item1Str = builder.item1Str;
        this.item2Str = builder.item2Str;

        this.titleColor = builder.titleColor;
        this.moneyColor = builder.moneyColor;
        this.descrColor = builder.descrColor;
        this.item1Color = builder.item1Color;
        this.item2Color = builder.item2Color;
        this.linksColor1 = builder.linksColor1;
        this.linksColor2 = builder.linksColor2;

        this.mCenterPopWindowItem1ClickListener = builder.mCenterPopWindowItem1ClickListener;
        this.mCenterPopWindowItem2ClickListener = builder.mCenterPopWindowItem2ClickListener;
        this.mCenterPopWindowLinkClickListener = builder.mCenterPopWindowLinkClickListener;

        initView();
    }

    protected void initView() {
        initTextView(titleStr, titleColor, R.id.ppw_center_title, null);
        initTextView(descrStr, descrColor, R.id.ppw_center_description, mCenterPopWindowLinkClickListener);
        initTextView(item1Str, item1Color, R.id.ppw_center_item1, mCenterPopWindowItem1ClickListener);
        initTextView(item2Str, item2Color, R.id.ppw_center_item2, mCenterPopWindowItem2ClickListener);

        initMoneyTextView(moneyStr, moneyColor, R.id.ppw_center_money);
    }

    protected void initTextView(String text, int colorId, int resId, final CenterPopWindowItemClickListener clickListener) {
        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) mContentView.findViewById(resId);
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            if (colorId != 0) {
                textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
            }
            if (!TextUtils.isEmpty(linksStr) && clickListener != null && clickListener instanceof CenterPopWindowLinkClickListener) {
                ConvertUtils.stringLinkConvert(textView, setLinks(linksStr));
                return;
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClicked();
                    }
                }
            });
        }

    }

    protected void initMoneyTextView(String moneyStr, int colorId, int resId) {

//        Spannable moneySpan = new SpannableString(moneyStr);
//        moneySpan.setSpan(new AbsoluteSizeSpan(ConvertUtils.sp2px(mActivity,15)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        moneySpan.setSpan(new AbsoluteSizeSpan(ConvertUtils.sp2px(mActivity,30)), 1, moneyStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (!TextUtils.isEmpty(moneyStr)) {
            TextView textView = (TextView) mContentView.findViewById(resId);
            textView.setVisibility(View.VISIBLE);
            textView.setText(moneyStr);
            if (colorId != 0) {
                textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
            }
        }
    }

    protected List<Link> setLinks(String link) {
        List<Link> links = new ArrayList<>();
        Link replyNameLink = new Link(link)
                .setTextColor(ContextCompat.getColor(mActivity, linksColor1))                  // optional, defaults to holo blue
                .setTextColorOfHighlightedLink(ContextCompat.getColor(mActivity, linksColor2)) // optional, defaults to holo blue
                .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                .setUnderlined(false)                                       // optional, defaults to true
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText, LinkMetadata linkMetadata) {
                        if (mCenterPopWindowLinkClickListener != null) {
                            mCenterPopWindowLinkClickListener.onLongClick();
                        }
                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText, LinkMetadata linkMetadata) {
                        if (mCenterPopWindowLinkClickListener != null) {
                            mCenterPopWindowLinkClickListener.onClicked();
                        }
                    }
                });
        links.add(replyNameLink);
        return links;
    }

    public static final class CBuilder extends Builder {

        private CenterPopWindowItem1ClickListener mCenterPopWindowItem1ClickListener;
        private CenterPopWindowItem2ClickListener mCenterPopWindowItem2ClickListener;
        private CenterPopWindowLinkClickListener mCenterPopWindowLinkClickListener;

        private String titleStr;
        private String moneyStr;
        private String descrStr;
        private String item1Str;
        private String item2Str;
        private String linksStr;

        private int titleColor;
        private int moneyColor;
        private int descrColor;
        private int item1Color;
        private int item2Color;
        private int linksColor1;
        private int linksColor2;

        public CBuilder() {
        }

        public CBuilder(PayPopWindow builder) {
            this.titleStr = builder.titleStr;
            this.moneyStr = builder.moneyStr;
            this.descrStr = builder.descrStr;
            this.linksStr = builder.linksStr;
            this.item1Str = builder.item1Str;
            this.item2Str = builder.item2Str;

            this.titleColor = builder.titleColor;
            this.moneyColor = builder.moneyColor;
            this.descrColor = builder.descrColor;
            this.item1Color = builder.item1Color;
            this.item2Color = builder.item2Color;
            this.linksColor1 = builder.linksColor1;
            this.linksColor2 = builder.linksColor2;

            this.mCenterPopWindowItem1ClickListener = builder.mCenterPopWindowItem1ClickListener;
            this.mCenterPopWindowItem2ClickListener = builder.mCenterPopWindowItem2ClickListener;
            this.mCenterPopWindowLinkClickListener = builder.mCenterPopWindowLinkClickListener;

        }

        public CBuilder buildCenterPopWindowLinkClickListener(CenterPopWindowLinkClickListener mCenterPopWindowLinkClickListener) {
            this.mCenterPopWindowLinkClickListener = mCenterPopWindowLinkClickListener;
            return this;
        }

        public CBuilder buildCenterPopWindowItem2ClickListener(CenterPopWindowItem2ClickListener mCenterPopWindowItem2ClickListener) {
            this.mCenterPopWindowItem2ClickListener = mCenterPopWindowItem2ClickListener;
            return this;
        }

        public CBuilder buildCenterPopWindowItem1ClickListener(CenterPopWindowItem1ClickListener mCenterPopWindowItem1ClickListener) {
            this.mCenterPopWindowItem1ClickListener = mCenterPopWindowItem1ClickListener;
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

        public CBuilder buildLinksColor1(int linksColor1) {
            this.linksColor1 = linksColor1;
            return this;
        }

        public CBuilder buildLinksColor2(int linksColor2) {
            this.linksColor2 = linksColor2;
            return this;
        }


        public CBuilder buildItem2Color(int item2Color) {
            this.item2Color = item2Color;
            return this;
        }

        public CBuilder buildItem1Color(int item1Color) {
            this.item1Color = item1Color;
            return this;
        }

        public CBuilder buildDescrColor(int descrColor) {
            this.descrColor = descrColor;
            return this;
        }

        public CBuilder buildMoneyColor(int moneyColor) {
            this.moneyColor = moneyColor;
            return this;
        }

        public CBuilder buildTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public CBuilder buildLinksStr(String linksStr) {
            this.linksStr = linksStr;
            return this;
        }

        public CBuilder buildTitleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public CBuilder buildMoneyStr(String moneyStr) {
            this.moneyStr = moneyStr;
            return this;
        }

        public CBuilder buildDescrStr(String descrStr) {
            this.descrStr = descrStr;
            return this;
        }

        public CBuilder buildItem1Str(String item1Str) {
            this.item1Str = item1Str;
            return this;
        }

        public CBuilder buildItem2Str(String item2Str) {
            this.item2Str = item2Str;
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
        public PayPopWindow build() {
            super.build();
            return new PayPopWindow(this);
        }
    }

    private interface CenterPopWindowItemClickListener {
        void onClicked();
    }

    public interface CenterPopWindowItem1ClickListener extends CenterPopWindowItemClickListener {
    }

    public interface CenterPopWindowItem2ClickListener extends CenterPopWindowItemClickListener {
    }

    public interface CenterPopWindowLinkClickListener extends CenterPopWindowItemClickListener {
        void onLongClick();
    }
}
