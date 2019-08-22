package com.zhiyicx.common.utils;

import android.graphics.BlurMaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.TouchableMovementMethod;

/**
 * @Describe for TextView click spannable  and Blur effect
 * @Author Jungle68
 * @Date 2015/5/12
 * @Contact master.jungle68@gmail.com
 */
public class TextViewUtils {

    private OnSpanTextClickListener mSpanTextClickListener;
    private OnTextSpanComplete mOnTextSpanComplete;

    private TextView mTextView;//显示富文本的控件
    private String mOriMsg;//全部文本信息

    private boolean canNotRead = true;// 未付费状态

    private int mStartPos;

    private int mEndPos;

    private int mAlpha;

    private int mDynamicPosition;// 动态位置

    private int mNote;// 付费节点

    private long mAmount;// 付费金额

    private Integer mSpanTextColor;
    private boolean mCanRead;

    private int mMaxLineNums = 3;

    public static TextViewUtils newInstance(TextView textView, String oriMsg) {
        return new TextViewUtils(textView, oriMsg);
    }

    private TextViewUtils(TextView textView, String oriMsg) {
        this.mTextView = textView;
        mTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.mOriMsg = oriMsg;
    }

    public TextViewUtils spanTextColor(int spanTextColor) {
        mSpanTextColor = spanTextColor;
        return this;
    }

    public TextViewUtils onSpanTextClickListener(OnSpanTextClickListener spanTextClickListener) {
        mSpanTextClickListener = spanTextClickListener;
        return this;
    }

    public TextViewUtils onTextSpanComplete(OnTextSpanComplete onTextSpanComplete) {
        mOnTextSpanComplete = onTextSpanComplete;
        return this;
    }

    public TextViewUtils position(int startPos, int endPos) {
        mStartPos = startPos;
        mEndPos = endPos;
        return this;
    }

    public TextViewUtils oriMsg(String oriMsg) {
        this.mOriMsg = oriMsg;
        return this;
    }

    public TextViewUtils alpha(int alpha) {
        mAlpha = alpha;
        return this;
    }

    public TextViewUtils amount(int amount) {
        mAmount = amount;
        return this;
    }

    public TextViewUtils note(int note) {
        mNote = note;
        return this;
    }

    public TextViewUtils dataPosition(int dynamicPosition) {
        mDynamicPosition = dynamicPosition;
        return this;
    }

    public TextViewUtils maxLines(int maxlines) {
        mMaxLineNums = maxlines;
        return this;
    }

    /**
     * 设置文字
     *
     * @param canRead 是否可见
     * @return
     */
    public TextViewUtils disPlayText(boolean canRead) {
        mCanRead = canRead;
        return this;
    }

    public TextViewUtils build() {
        if (mTextView == null) {
            throw new IllegalArgumentException("textView not be null");
        }
        handleTextDisplay();
        return this;
    }

    private void handleTextDisplay() {
        mTextView.setVisibility(View.INVISIBLE);
        if (!mCanRead) {
            mTextView.setText(getSpannableString(mOriMsg));
//            dealTextViewClickEvent(mTextView);
        } else {
            mTextView.setText(mOriMsg);
        }
        if (mOnTextSpanComplete != null) {
            mOnTextSpanComplete.onComplete();
        }

    }

    class BlurSpanTextClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            if (mSpanTextClickListener != null) {
                mSpanTextClickListener.setSpanText(mDynamicPosition, mNote, mAmount, mTextView,
                        canNotRead);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (mSpanTextColor != null) {
                ds.setColor(mSpanTextColor);
            }
            ds.setAlpha(mAlpha > 0 ? mAlpha : 0xff);
            ds.setUnderlineText(false);    //去除超链接的下划线
            if (canNotRead) {
                BlurMaskFilter blurMaskFilter = new BlurMaskFilter(mTextView.getTextSize() / 3,
                        BlurMaskFilter.Blur.NORMAL);
                ds.setMaskFilter(blurMaskFilter);
            } else {
                ds.setMaskFilter(null);
            }
        }
    }

    private SpannableString getSpannableString(CharSequence temp) {
        SpannableString spanableInfo = SpannableString.valueOf(temp);
        if (mEndPos > temp.length()) {
            mEndPos = temp.length();
        }
        try {
            spanableInfo.setSpan(new BlurSpanTextClickable(), mStartPos,
                    mEndPos, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            spanableInfo.setSpan(new BlurSpanTextClickable(), 0, temp.length(), Spanned
                    .SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanableInfo;
    }

    public static void addLinkMovementMethod(TextView textView) {
        MovementMethod m = textView.getMovementMethod();

        if ((m == null) || !(m instanceof TouchableMovementMethod)) {
            if (textView.getLinksClickable()) {
                textView.setMovementMethod(TouchableMovementMethod.getInstance());
            }
        }
    }

    // clickSpan 的点击事件分发处理
    public static void dealTextViewClickEvent(TextView textView) {
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CharSequence text = ((TextView) v).getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                TextView widget = (TextView) v;
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static int getStringLenght(String src) {
        int count = 0;
        char[] chars = src.toCharArray();
        for (char c : chars) {
            if (c < 128) {// 英文ascii码值都是128以下
                count += 1;
            }
        }
        return src.length() - count;
    }

    /**
     * @param src
     * @return 字母个数
     */
    public static int getLetterLenght(CharSequence src) {
        int count = 0;
        char[] chars = src.toString().toCharArray();
        for (char c : chars) {
            if (c < 128) {// 英文ascii码值都是128以下完成
                count += 1;
            }
        }
        return count;
    }

    /**
     * 添加自定义 Ellipsis
     *
     * @param textView
     * @param strLabel 后缀
     * @param maxLines
     */
    public static void setLabelAfterEllipsis(TextView textView,String strLabel, int maxLines) {

        if (textView.getLayout().getEllipsisCount(maxLines - 1) == 0) {
            return; // Nothing to do
        }

        int start = textView.getLayout().getLineStart(0);
        int end = textView.getLayout().getLineEnd(textView.getLineCount() - 1);
        String displayed = textView.getText().toString().substring(start, end);
        int displayedWidth = getTextWidth(displayed, textView.getTextSize());

        String ellipsis = "...";
        String suffix = ellipsis + strLabel;

        int textWidth;
        String newText = displayed;
        textWidth = getTextWidth(newText + suffix, textView.getTextSize());

        while (textWidth > displayedWidth) {
            newText = newText.substring(0, newText.length() - 1).trim();
            textWidth = getTextWidth(newText + suffix, textView.getTextSize());
        }
        String result = newText + suffix;
        textView.setText(result);
    }

    private static int getTextWidth(String text, float textSize) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        return (int) Math.ceil(bounds.width());
    }

    public interface OnSpanTextClickListener {
        void setSpanText(int position, int note, long amount, TextView view, boolean canNotRead);
    }

    public interface OnTextSpanComplete {
        void onComplete();
    }
}

