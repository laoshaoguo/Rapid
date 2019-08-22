package com.zhiyicx.common.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.KeyListener;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ThinkSNS Plus
 * Copyright (c) 2018 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @Author Jliuer
 * @Date 2018/08/22/10:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SpanNoCursorEditTextView extends AppCompatEditText {

    private KeyListener mKeyListener;

    public SpanNoCursorEditTextView(Context context) {
        super(context);
    }

    public SpanNoCursorEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpanNoCursorEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mKeyListener == null) {
            mKeyListener = getKeyListener();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                setKeyListener(mKeyListener);
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_DOWN:
                setKeyListener(null);
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setKeyListener(mKeyListener);
                getParent().requestDisallowInterceptTouchEvent(false);
            default:
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        CharSequence text = getText();
        Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            x += getScrollX();
            y += getScrollY();

            Layout layout = getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);
            return (link.length != 0) || super.onTouchEvent(event);

        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
