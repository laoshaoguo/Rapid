package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.R;

import rx.functions.Action1;

/**
 * ThinkSNS Plus
 * Copyright (c) 2018 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @author Jliuer
 * @Date 18/08/15 16:17
 * @Email Jliuer@aliyun.com
 * @Description 支付輸入密碼
 */
public class InputPasswordView extends RelativeLayout {

    TextView mTvCancle;
    EditText mEtContent;
    TextView mBtSend;
    TextView mTvErrorTip;
    ImageView mIvLoading;
    TextView mTvForgetPsd;

    PayNote mPayNote;

    private OnClickListener mOnClickListener;
    private AnimationDrawable mLoadingAnimationDrawable;

    public InputPasswordView(Context context) {
        super(context);
        init(context, null);
    }

    public InputPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InputPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_input_password_viewgroup, this);
        mTvCancle = findViewById(R.id.tv_cancle);
        mEtContent = findViewById(R.id.et_content);
        mBtSend = findViewById(R.id.bt_send);
        mTvErrorTip = findViewById(R.id.tv_error_tip);
        mIvLoading = findViewById(R.id.iv_loading);
        mTvForgetPsd = findViewById(R.id.tv_forget_psd);

        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputPsd = mEtContent.getText().toString().trim();
                if (!TextUtils.isEmpty(inputPsd) && mOnClickListener != null) {
                    getPayNote().psd = inputPsd;
                    mOnClickListener.onSureClick(view, inputPsd, getPayNote());
                    setLoading(true);
                }
            }
        });

        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onCancle();
                    setLoading(false);
                }
            }
        });

        mTvForgetPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onForgetPsdClick();
                    setLoading(false);
                }
            }
        });

        RxTextView.textChanges(mEtContent)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        mBtSend.setEnabled(!TextUtils.isEmpty(charSequence));
                        setErrorTip("");
                    }
                });

        mLoadingAnimationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE && getVisibility() != GONE) {
            mEtContent.setText("");
            setErrorTip("");
        }
        super.setVisibility(visibility);
    }

    public boolean trySetVisibility(int visibility) {
        if (visibility == GONE && mLoadingAnimationDrawable.isRunning()) {
            return false;
        }
        return true;
    }

    public void setErrorTip(String errorTip) {
        mTvErrorTip.setText(errorTip);
        setLoading(false);
    }

    public void setLoading(boolean loading) {
        if (loading) {
            mIvLoading.setVisibility(VISIBLE);
            if (mLoadingAnimationDrawable != null && !mLoadingAnimationDrawable.isRunning()) {
                mLoadingAnimationDrawable.start();
            }
            mBtSend.setText("");
            mBtSend.setEnabled(false);
        } else {
            if (mLoadingAnimationDrawable != null && mLoadingAnimationDrawable.isRunning()) {
                mLoadingAnimationDrawable.stop();
            }
            mIvLoading.setVisibility(GONE);
            mBtSend.setText(getResources().getString(R.string.sure));
            mBtSend.setEnabled(!TextUtils.isEmpty(mEtContent.getText()));
        }
        mTvForgetPsd.setEnabled(mBtSend.isEnabled());
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public View getEtContent() {
        return mEtContent;
    }

    public PayNote getPayNote() {
        return mPayNote;
    }

    public void setPayNote(PayNote payNote) {
        mPayNote = payNote;
    }

    public interface OnClickListener {
        void onSureClick(View v, String text, PayNote payNote);

        void onForgetPsdClick();

        void onCancle();
    }

    public static class PayNote {
        public int dynamicPosition;
        public int imagePosition;
        public int note;
        public int amount;
        public boolean isImage;
        public String psd;
        public Long id;
        public Long parent_id;

        public PayNote(int dynamicPosition, int imagePosition, int note, boolean isImage, String psd) {
            this.dynamicPosition = dynamicPosition;
            this.imagePosition = imagePosition;
            this.note = note;
            this.isImage = isImage;
            this.psd = psd;
        }

        public PayNote(String psd, Long id, Long parent_id) {
            this.psd = psd;
            this.id = id;
            this.parent_id = parent_id;
        }

        public PayNote(String psd) {
            this.psd = psd;
        }

        public PayNote(String psd, Long id) {
            this.psd = psd;
            this.id = id;
        }

        public PayNote() {
        }

        public int getDynamicPosition() {
            return dynamicPosition;
        }

        public void setDynamicPosition(int dynamicPosition) {
            this.dynamicPosition = dynamicPosition;
        }

        public int getImagePosition() {
            return imagePosition;
        }

        public void setImagePosition(int imagePosition) {
            this.imagePosition = imagePosition;
        }

        public int getNote() {
            return note;
        }

        public void setNote(int note) {
            this.note = note;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public boolean isImage() {
            return isImage;
        }

        public void setImage(boolean image) {
            isImage = image;
        }

        public String getPsd() {
            return psd;
        }

        public void setPsd(String psd) {
            this.psd = psd;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getParent_id() {
            return parent_id;
        }

        public void setParent_id(Long parent_id) {
            this.parent_id = parent_id;
        }
    }
}
