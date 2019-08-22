package com.zhiyicx.baseproject.widget.edittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/4
 * @contact email:648129313@qq.com
 */

public class SEditText extends EditText {

    public SEditText(Context context) {
        super(context);
    }

    public SEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //通知父控件不要干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //通知父控件不要干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }
}
