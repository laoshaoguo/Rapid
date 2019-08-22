package com.zhiyicx.baseproject.widget.popwindow;

import android.app.Activity;
import android.view.View;

import com.zhiyicx.baseproject.R;
import com.zhy.adapter.recyclerview.CommonAdapter;

/**
 * ThinkSNS Plus
 * Copyright (c) 2018 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @Author Jliuer
 * @Date 2018/08/09/9:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RecyclerViewPopForLetter extends RecyclerViewPopupWindow {

    private boolean showCancle;

    private RecyclerViewPopForLetter() {
    }

    public RecyclerViewPopForLetter(TBuilder builder) {
        super(builder);
        this.showCancle = builder.showCancle;
        View cancle = mContentView.findViewById(R.id.tv_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        cancle.setVisibility(showCancle ? View.VISIBLE : View.GONE);
        mContentView.setBackgroundResource(showCancle ? R.color.more_pop_bg : R.color.white);
    }

    @Override
    protected boolean useShareDecoration() {
        return false;
    }

    public static RecyclerViewPopForLetter.TBuilder builder() {
        return new RecyclerViewPopForLetter.TBuilder();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_recyclerview_pop_more;
    }

    public static class TBuilder extends RecyclerViewPopupWindow.Builder {
        private boolean showCancle;

        public TBuilder() {
        }

        public TBuilder showCancle(boolean showCancle) {
            this.showCancle = showCancle;
            return this;
        }

        @Override
        public TBuilder with(Activity mActivity) {
            super.with(mActivity);
            return this;
        }

        @Override
        public TBuilder adapter(CommonAdapter adapter) {
            super.adapter(adapter);
            return this;
        }

        @Override
        public TBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public TBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public TBuilder iFocus(boolean isFocus) {
            super.iFocus(isFocus);
            return this;
        }

        @Override
        public TBuilder alpha(float alpha) {
            super.alpha(alpha);
            return this;
        }

        @Override
        public TBuilder asVertical() {
            super.asVertical();
            return this;
        }

        @Override
        public TBuilder asHorizontal() {
            super.asHorizontal();
            return this;
        }

        @Override
        public TBuilder asGrid(int spanCount) {
            super.asGrid(spanCount);
            return this;
        }

        @Override
        public TBuilder itemSpacing(int mItemSpacing) {
            super.itemSpacing(mItemSpacing);
            return this;
        }

        @Override
        public RecyclerViewPopForLetter build() {
            super.build();
            return new RecyclerViewPopForLetter(this);
        }
    }
}
