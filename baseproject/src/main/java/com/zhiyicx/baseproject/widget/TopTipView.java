package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @Describe 顶部提示指示器
 * @Author Jungle68
 * @Date 2017/1/25
 * @Contact master.jungle68@gmail.com
 */

public class TopTipView extends FrameLayout {
    protected TextView mTvTipText;
    protected String mTipStr = "";

    public TopTipView(Context context) {
        super(context);
        init(context, null);
    }

    public TopTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_top_tip, this);
        mTvTipText = (TextView) findViewById(R.id.tv_top_tip_text);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.topTipView);
            mTipStr = array.getString(R.styleable.topTipView_tipText);
            array.recycle();
        }
        mTvTipText.setText(mTipStr);
    }

    public String getTipStr() {
        return mTipStr;
    }

    public void setTipStr(String tipStr) {
        mTipStr = tipStr;
    }

    /**
     * 显示控件
     */
    public void show() {
        this.setVisibility(VISIBLE);
    }

    /**
     * 隐藏控件，且不占用位置
     */
    public void hide() {
        this.setVisibility(GONE);
    }

}

