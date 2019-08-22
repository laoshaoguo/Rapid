package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.TouchableBaseSpan;
import com.klinker.android.link_builder.TouchableMovementMethod;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SkinUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/21
 * @Contact master.jungle68@gmail.com
 * @Description 用于动态列表文字
 */
public class SpanTextViewWithEllipsize extends android.support.v7.widget.AppCompatTextView {
    private SpannableStringBuilder spannableStringBuilder;
    private TouchableSpan mTouchableSpan = new TouchableSpan();
    private CharSequence mCharSequence;
    private int mLayoutWidth;
    private int mFutureTextViewWidth;
    private boolean canLookWords = true;
    private boolean needLookMore = true;

    private final String LOOKMORE = "阅读全文";
    private final String SUFFIX = "...";
    private final float TOTALWIDTH = 0.8f;

    private String lookMore;
    private String suffix;
    private float totalWidth;

    public SpanTextViewWithEllipsize(Context context) {
        this(context, null);
    }

    public SpanTextViewWithEllipsize(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpanTextViewWithEllipsize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(TouchableMovementMethod.getInstance());
        spannableStringBuilder
                = new SpannableStringBuilder();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpanTextViewWithEllipsize);
        needLookMore = typedArray.getBoolean(R.styleable.SpanTextViewWithEllipsize_use_look_more, true);
        lookMore = typedArray.getString(R.styleable.SpanTextViewWithEllipsize_text_lookmore);
        suffix = typedArray.getString(R.styleable.SpanTextViewWithEllipsize_text_suffix);
        totalWidth = typedArray.getFloat(R.styleable.SpanTextViewWithEllipsize_text_totalWidth, TOTALWIDTH);
        mFutureTextViewWidth = (int) (DeviceUtils.getScreenWidth(context) * totalWidth);
        typedArray.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (!needLookMore) {
            super.setText(text, type);
            return;
        }
        mCharSequence = text;
        setTextInternal(dealText(), type);
    }

    private CharSequence dealText() {
        if (spannableStringBuilder == null) {
            spannableStringBuilder
                    = new SpannableStringBuilder();
        }
        spannableStringBuilder.clear();
        spannableStringBuilder.clearSpans();
        spannableStringBuilder.append(mCharSequence);
        int lastCharStart, lastCharDown;
        int lineCount;
        boolean lineIsFull = false;
        try {
            if (mLayoutWidth <= 0) {
                if (getWidth() == 0) {
                    if (mFutureTextViewWidth == 0) {
                        return spannableStringBuilder;
                    } else {
                        mLayoutWidth = mFutureTextViewWidth - getPaddingLeft() - getPaddingRight();
                    }
                } else {
                    mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
                }
            }
            DynamicLayout mLayout = new DynamicLayout(spannableStringBuilder, getPaint(), mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f,
                    false);
            lineCount = mLayout.getLineCount();
            int maxLine = getMaxLines();
            lineIsFull = lineCount >= maxLine;

            int needCount = Math.min(maxLine, lineCount) - 1;
            lastCharDown = mLayout.getLineVisibleEnd(needCount);
            lastCharStart = mLayout.getLineStart(needCount);
        } catch (Exception ignored) {
            lastCharDown = 0;
            lastCharStart = 0;
        }

        if (lineIsFull && lastCharDown > 0 && mCharSequence.length() > lastCharDown) {
            CharSequence measureContent = mCharSequence.subSequence(lastCharStart, lastCharDown);
            if (TextUtils.isEmpty(lookMore)) {
                lookMore = LOOKMORE;
            }
            if (TextUtils.isEmpty(suffix)) {
                suffix = SUFFIX;
            }
            int exetra = DeviceUtils.getNeedStrLength(measureContent.toString(), suffix + lookMore);
            if (exetra == 0) {
                return spannableStringBuilder;
            }
            spannableStringBuilder.clear();
            spannableStringBuilder.clearSpans();

            spannableStringBuilder
                    .append(mCharSequence.subSequence(0, lastCharDown - exetra));
            TouchableSpan[] touchableSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), TouchableSpan.class);
            if (canLookWords && (touchableSpans == null || touchableSpans.length == 0)) {

                spannableStringBuilder
                        .append(suffix)
                        .append(lookMore);
                spannableStringBuilder.setSpan(mTouchableSpan, spannableStringBuilder.length() - lookMore.length(),
                        spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableStringBuilder;
    }

    public void updateForRecyclerView(CharSequence text, int futureTextViewWidth) {
        mFutureTextViewWidth = futureTextViewWidth;
        setText(text);
    }

    private void setTextInternal(CharSequence text, TextView.BufferType type) {
        super.setText(text, type);
    }

    /**
     * 注意：spannableString 设置Spannable 的对象到spannableString中时，要用Spannable.SPAN_EXCLUSIVE_EXCLUSIVE的flag值，不然可能会会出现后面的衔接字符串不会显示
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }

    public class TouchableSpan extends TouchableBaseSpan {
        private boolean mIsPressed;

        @Override
        public void setTouched(boolean touched) {
            super.setTouched(touched);
            setPressed(touched);
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void onClick(View widget) {
            if (mOnToucheSpanClickListener == null) {
                return;
            }
            mOnToucheSpanClickListener.onClick(widget);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(SkinUtils.getColor(R.color.themeColor));
            ds.bgColor = mIsPressed ? 0x55999999 : 0;
            ds.setUnderlineText(false);
        }
    }

    public void setCanLookWords(boolean canLookWords) {
        this.canLookWords = canLookWords;
    }

    public void setNeedLookMore(boolean needLookMore) {
        this.needLookMore = needLookMore;
    }

    public void setLayoutWidth(int layoutWidth) {
        mLayoutWidth = layoutWidth;
        invalidate();
    }

    public void setTotalWidth(float totalWidth) {
        this.totalWidth = totalWidth;
    }

    OnToucheSpanClickListener mOnToucheSpanClickListener;

    public void setOnToucheSpanClickListener(OnToucheSpanClickListener onToucheSpanClickListener) {
        mOnToucheSpanClickListener = onToucheSpanClickListener;
    }

    public interface OnToucheSpanClickListener {
        void onClick(View v);
    }
}
