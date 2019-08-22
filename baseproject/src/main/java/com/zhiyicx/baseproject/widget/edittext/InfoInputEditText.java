package com.zhiyicx.baseproject.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @author Catherine
 * @describe 认证信息输入框，可以设置是否必填，以后如果要扩展的话，还可以设置文字颜色大小 背景颜色等
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public class InfoInputEditText extends LinearLayout {

    public static final String INPUT_TYPE_PHONE = "phone";
    public static final String INPUT_TYPE_NUMBER = "number";
    public static final String INPUT_TYPE_TEXT = "text";

    private TextView mTvFlagRequired;
    private TextView mTvLeftText;
    private EditText mEditInput;
    private View mIvBottomDiver;

    private int mVisiable = VISIBLE; // 是否必填 默认必填
    private boolean isShowDiver = true; // 是否显示分割线 默认必填

    public InfoInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_info_input, this);
        mTvFlagRequired = (TextView) findViewById(R.id.tv_flag_required);
        mTvLeftText = (TextView) findViewById(R.id.tv_left_text);
        mEditInput = (EditText) findViewById(R.id.edit_input);
        mIvBottomDiver = findViewById(R.id.iv_bottom_diver);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.inputContainHint);
        String leftText = array.getString(R.styleable.inputContainHint_leftHintText);
        String hint = array.getString(R.styleable.inputContainHint_rightHint);
        mVisiable = array.getInteger(R.styleable.inputContainHint_isRequired, VISIBLE);
        isShowDiver = array.getBoolean(R.styleable.inputContainHint_showDiver, true);
        String inputType = array.getString(R.styleable.inputContainHint_rightInputType);
        int maxLines = array.getInteger(R.styleable.inputContainHint_rightMaxLines, 0);
        int maxLength = array.getInteger(R.styleable.inputContainHint_rightMaxLength, 0);
        int minHeight = array.getDimensionPixelOffset(R.styleable.inputContainHint_totalMinHeight, 0);
        array.recycle();
        mTvFlagRequired.setVisibility(mVisiable);
        mIvBottomDiver.setVisibility(isShowDiver ? VISIBLE : GONE);
        if (!TextUtils.isEmpty(leftText)) {
            mTvLeftText.setText(leftText);
        }
        if (!TextUtils.isEmpty(hint)) {
            mEditInput.setHint(hint);
        }
        if (maxLines != 0) {
            mEditInput.setLines(maxLines);
        }
        if (maxLength != 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            mEditInput.setFilters(filters);
        }
        if (minHeight != 0) {
            mEditInput.setMinHeight(minHeight);
        }
        if (!TextUtils.isEmpty(inputType)) {
            switch (inputType) {
                case INPUT_TYPE_PHONE:
                    mEditInput.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                case INPUT_TYPE_NUMBER:
                    mEditInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case INPUT_TYPE_TEXT:
                    mEditInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                default:
            }
        }
    }

    public void setRequired(int visiable) {
        this.mVisiable = visiable;
        mTvFlagRequired.setVisibility(visiable);
    }

    public void setShowDiver(boolean showDiver) {
        this.isShowDiver = showDiver;
        mIvBottomDiver.setVisibility(isShowDiver ? VISIBLE : GONE);
    }

    public void setLeftText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            mTvLeftText.setText(text);
        }
    }

    public void setInputHint(CharSequence hint) {
        if (TextUtils.isEmpty(hint)) {
            mEditInput.setHint(hint);
        }
    }

    public void setInputType(String type) {
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case INPUT_TYPE_PHONE:
                    mEditInput.setInputType(InputType.TYPE_CLASS_PHONE);
                    InputFilter[] filters = {new InputFilter.LengthFilter(11)};
                    mEditInput.setFilters(filters);
                    break;
                case INPUT_TYPE_NUMBER:
                    mEditInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case INPUT_TYPE_TEXT:
                    mEditInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                default:
            }
        }
    }

    public EditText getEditInput() {
        return mEditInput;
    }

    public void setEditInputString(String inputString) {
        mEditInput.setText(inputString);
    }
}
