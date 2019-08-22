package com.zhiyicx.baseproject.widget.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.ConvertUtils;

/**
 * @author LiuChao
 * @describe 个人中心的组合控件，图片-文字-图片
 * @date 2017/1/7
 * @contactemail:450127106@qq.com
 */
public class CombinationButton extends FrameLayout {
    ImageView mCombinedButtonImgLeft;
    ImageView mCombinedButtonImgRight;

    TextView mCombinedButtonLeftText;
    TextView mCombinedButtonRightText;
    View mVLine;
    View mVLineTop;

    public CombinationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_combination_button, this);
        mCombinedButtonImgLeft = findViewById(R.id.iv_left_img);
        mCombinedButtonImgRight = findViewById(R.id.iv_right_img);
        mCombinedButtonLeftText = findViewById(R.id.tv_left_text);
        mCombinedButtonRightText = findViewById(R.id.tv_right_text);
        mVLine = findViewById(R.id.v_line);
        mVLineTop = findViewById(R.id.v_line_top);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.combinationBtn);
        Drawable leftImage = array.getDrawable(R.styleable.combinationBtn_leftImage);
        Drawable rightImage = array.getDrawable(R.styleable.combinationBtn_rightImage);
        String leftText = array.getString(R.styleable.combinationBtn_leftText);
        String rightText = array.getString(R.styleable.combinationBtn_rightText);
        String rightHint = array.getString(R.styleable.combinationBtn_rightHintText);
        ColorStateList leftTextColor = array.getColorStateList(R.styleable.combinationBtn_leftTextColor);
        ColorStateList rightTextColor = array.getColorStateList(R.styleable.combinationBtn_rightTextColor);
        ColorStateList rightHintColor = array.getColorStateList(R.styleable.combinationBtn_rightHintColor);
        int bgColor = array.getColor(R.styleable.combinationBtn_bgColor, -1);

        boolean showLine = array.getBoolean(R.styleable.combinationBtn_showLine, true);
        boolean showTopLine = array.getBoolean(R.styleable.combinationBtn_showTopLine, false);
        int lineColor = array.getColor(R.styleable.combinationBtn_lineColor, -1);
        Drawable lineBg = array.getDrawable(R.styleable.combinationBtn_lineBg);

        int dividerLeftMargin = array.getDimensionPixelSize(R.styleable.combinationBtn_dividerLeftMargin, 0);
        float leftTextSize = array.getDimension(R.styleable.combinationBtn_leftTextSize, 14);
        float rightTextSize = array.getDimension(R.styleable.combinationBtn_rightTextSize, 13);
        int dividerRightMargin = array.getDimensionPixelSize(R.styleable.combinationBtn_dividerRightMargin, 0);
        int leftTextLeftPadding = array.getDimensionPixelOffset(R.styleable.combinationBtn_leftTextLeftPadding, ConvertUtils.dp2px(context, 10));
        array.recycle();
        if (!TextUtils.isEmpty(leftText)) {
            mCombinedButtonLeftText.setText(leftText);
        }
        if (leftTextColor != null) {
            mCombinedButtonLeftText.setTextColor(leftTextColor);
        }
        if (rightTextColor != null) {
            mCombinedButtonRightText.setTextColor(rightTextColor);
        }
        if (rightHintColor != null) {
            mCombinedButtonRightText.setHintTextColor(rightHintColor);
        }
        if (bgColor != -1) {
            findViewById(R.id.rl_container).setBackgroundColor(bgColor);
        }
        if (!TextUtils.isEmpty(rightText)) {
            mCombinedButtonRightText.setText(rightText);
        }
        if (!TextUtils.isEmpty(rightHint)) {
            mCombinedButtonRightText.setHint(rightHint);
        }
        mCombinedButtonLeftText.setPadding(leftTextLeftPadding, 0, 0, 0);
        if (leftImage == null) {
            mCombinedButtonImgLeft.setVisibility(GONE);
        } else {
            mCombinedButtonImgLeft.setVisibility(VISIBLE);
            mCombinedButtonImgLeft.setImageDrawable(leftImage);
        }
        mCombinedButtonImgRight.setImageDrawable(rightImage);
        mCombinedButtonLeftText.setTextSize(leftTextSize);
        mCombinedButtonRightText.setTextSize(rightTextSize);
        if (showLine) {
            mVLine.setVisibility(VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
            layoutParams.setMargins(dividerLeftMargin, 0, dividerRightMargin, 0);
            if (lineColor != -1) {
                mVLine.setBackground(null);
                mVLine.setBackgroundColor(lineColor);
            }
            if (lineBg != null) {
                mVLine.setBackground(leftImage);
            }
        } else {
            mVLine.setVisibility(INVISIBLE);
        }

        if (showTopLine) {
            mVLineTop.setVisibility(VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVLineTop.getLayoutParams();
            layoutParams.setMargins(dividerLeftMargin, 0, dividerRightMargin, 0);
            if (lineColor != -1) {
                mVLineTop.setBackground(null);
                mVLineTop.setBackgroundColor(lineColor);
            }
            if (lineBg != null) {
                mVLineTop.setBackground(leftImage);
            }
        } else {
            mVLineTop.setVisibility(INVISIBLE);
        }
    }

    public void setTopLineVisible(int visible) {
        if (mVLineTop != null) {
            mVLineTop.setVisibility(visible);
        }
    }

    /**
     * 设置左边文字内容
     */
    public void setLeftText(String leftText) {
        mCombinedButtonLeftText.setText(leftText);
    }

    public void setLeftText(CharSequence leftText) {
        mCombinedButtonLeftText.setText(leftText);
    }

    public void setLeftTextSize(float leftTextSize) {
        mCombinedButtonLeftText.setTextSize(TypedValue.COMPLEX_UNIT_SP, leftTextSize);
    }

    /**
     * 设置右边文字内容
     */
    public void setRightText(String rightText) {
        mCombinedButtonRightText.setText(rightText);
    }

    /**
     * 设置文字内容颜色
     */
    public void setRightTextColor(int color) {
        mCombinedButtonRightText.setTextColor(color);
    }

    public void setLeftTextColor(int color) {
        mCombinedButtonLeftText.setTextColor(color);
    }

    public TextView getCombinedButtonRightTextView() {
        return mCombinedButtonRightText;
    }

    public String getRightText() {
        return mCombinedButtonRightText.getText().toString();
    }

    public void setRightImageClickListener(OnClickListener listener) {
        mCombinedButtonImgRight.setOnClickListener(listener);
    }

    public ImageView getCombinedButtonImgRight() {
        return mCombinedButtonImgRight;
    }

    public void setRightImage(int res) {
        if (res == 0) {
            mCombinedButtonImgRight.setVisibility(INVISIBLE);
            return;
        } else {
            mCombinedButtonImgRight.setVisibility(VISIBLE);
        }
        mCombinedButtonImgRight.setImageResource(res);
    }
}
