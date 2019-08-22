package com.zhiyicx.baseproject.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.RegexUtils;

/**
 * @Author Jliuer
 * @Date 2018/05/12/11:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ByteLimitEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher {

    protected boolean needWordByteLenghtLimit;
    protected int wordByteLenghtLimit;

    public ByteLimitEditText(Context context) {
        this(context, null);
    }

    public ByteLimitEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ByteLimitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ByteLimitEditText);
        wordByteLenghtLimit = typedArray.getInt(R.styleable.ByteLimitEditText_word_byte_limit_lengh, 0);
        needWordByteLenghtLimit = typedArray.getBoolean(R.styleable.ByteLimitEditText_need_word_byte_limit, false);
        typedArray.recycle();
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (needWordByteLenghtLimit && wordByteLenghtLimit > 0) {
            String inputStr = editable.toString().trim();


            int chineseCount = RegexUtils.getChineseCouns(inputStr);
            int totalCount = inputStr.length();

            int length = 2 * chineseCount + (totalCount - chineseCount);

            if (length > wordByteLenghtLimit) {
                // 截取取字节
                int surplus = length - wordByteLenghtLimit;

                String newStr = getNeedStr(inputStr, surplus);
                setText(newStr);
                //将光标定位到最后
                Selection.setSelection(getEditableText(), newStr.length());
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public String getNeedStr(String string, int surplus) {
        int position = -1;
        for (int i = string.length() - 1; i >= 0; i--) {
            char a = string.charAt(i);
            String b = String.valueOf(a);
            if (RegexUtils.isContainChinese(b)) {
                surplus = surplus - 2;
            } else {
                surplus = surplus - 1;
            }
            if (surplus == 0) {
                position = i;
                break;
            }
        }
        if (position > 0 && position <= string.length()) {
            return string.subSequence(0, position).toString();
        }
        return string;
    }
}
