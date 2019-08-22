package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Author Jliuer
 * @Date 2017/07/25/16:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TestTextView extends AppCompatTextView {

    private Paint mPaint;
    private int mDefaultColor;
    private int size;
    private int cx, cy, r;
    private String text;
    private CircleImageDrawable mHeaderImage;

    public TestTextView(Context context) {
        super(context);
    }

    public TestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        text = "匿";
        size = getResources().getDimensionPixelSize(R.dimen.headpic_for_assist);
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int minSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
        cy = cx = minSize / 2;
        r = cx * 2 / 3;
        size = Math.min(size, minSize);
        Bitmap newBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ico_edit_pen), size, size, true);
        mHeaderImage = new CircleImageDrawable(newBmp);
        mHeaderImage.setBounds(0, 0, size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mHeaderImage != null) {
            canvas.save();
            canvas.translate(0, (getMeasuredHeight() - size) / 2);
            mHeaderImage.draw(canvas);
            canvas.restore();
            canvas.translate(size, 0);
        } else {
            canvas.drawCircle(cx, cx, r, mPaint);
            canvas.drawText(text, cx - getPaint().measureText(text) / 2, cy - (getPaint().descent() + getPaint().ascent()) / 2, getPaint());
        }
        for (int i = 0; i < getLayout().getLineCount(); i++) {
            LogUtils.d(getLayout().getLineWidth(i));
        }
        super.onDraw(canvas);
    }

    public void setHeaderImage(CircleImageDrawable headerImage) {
        mHeaderImage = headerImage;
        invalidate();
    }
}
