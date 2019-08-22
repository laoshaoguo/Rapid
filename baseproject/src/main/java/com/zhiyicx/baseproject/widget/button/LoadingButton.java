package com.zhiyicx.baseproject.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;


/**
 * @Describe 涵盖加载动画的 button
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public class LoadingButton extends FrameLayout {

    protected TextView mTvText;
    protected ImageView mIvLoad;
    protected View mContainer;
    protected AnimationDrawable mAnimationDrawable;
    protected OnClickListener mOnClickListener;


    public LoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_loading_button, this);
        mContainer = findViewById(R.id.rl_container);
        mTvText = (TextView) findViewById(R.id.tv_text);
        mIvLoad = (ImageView) findViewById(R.id.iv_load);

        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.loadButton);

        String text = array.getString(R.styleable.loadButton_text);
        array.recycle();
        if (!TextUtils.isEmpty(text)) {
            mTvText.setText(text);
        }
        mAnimationDrawable = (AnimationDrawable) mIvLoad.getDrawable();
        mContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mContainer.setEnabled(enabled);
    }

    /**
     * 设置文字内容
     */
    public void setText(String text) {
        mTvText.setText(text);
    }

    public void setTextLeftDrawable(int res){
        mTvText.setCompoundDrawablesWithIntrinsicBounds(res,0,0,0);
    }

    /**
     * 设置响应监听
     *
     * @param l
     */
    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    /**
     * 设置背景
     *
     * @param resid
     */
    public void setBackground(int resid) {
        mContainer.setBackgroundResource(resid);
    }

    /**
     * 处理动画
     *
     * @param status true 开启动画，false 关闭动画
     */
    public void handleAnimation(boolean status) {
        if (mAnimationDrawable == null) {
            throw new IllegalArgumentException("load animation not be null");
        }
        if (status) {
            if (!mAnimationDrawable.isRunning()) {
                mIvLoad.setVisibility(VISIBLE);
                mAnimationDrawable.start();
            }
        } else {
            if (mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
                mIvLoad.setVisibility(INVISIBLE);
            }
        }
    }

    public AnimationDrawable getAnimationDrawable() {
        return mAnimationDrawable;
    }
}
