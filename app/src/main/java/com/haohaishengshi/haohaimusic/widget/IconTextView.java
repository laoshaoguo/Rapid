package com.haohaishengshi.haohaimusic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;

import com.haohaishengshi.haohaimusic.R;


/**
 * @author Jliuer
 * @Date 2017/3/30/14:18
 * @Email Jliuer@aliyun.com
 * @Description 带图标的文本空间
 */
public class IconTextView extends View {

    private static final String TAG = "IconTextView";

    private Rect mCacheBound;
    private Rect mIconBound;
    private Rect mTextBound;

    private int mTextSize;
    private int mTextColor;

    private String mText;

    private int mGap;
    private int mIconWidth;
    private int mIconHeight;

    private Drawable mDrawable;
    // 图片的方位
    private Direction mDirection;

    private Paint mPaint;

    private int mTouchSlop;
    /**
     * 额外的高度，需求
     */
    private int mExtroHeight = 10;
    private boolean inTapRegion;
    private int mStartX, mStartY;
    private Paint.FontMetricsInt mFontMetricsInt;

    public enum Direction {
        TOP(0), BOTTOM(1), LEFT(2), RIGHT(3);

        int mValue;

        Direction(int value) {
            this.mValue = value;
        }
    }

    public interface OnIconTextClickListener {
        void onIconTextClick(IconTextView view);
    }

    private OnIconTextClickListener mOnIconTextClickListener;

    public IconTextView(Context context) {
        this(context, null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconTextView);

        mTextSize = ta.getDimensionPixelSize(R.styleable.IconTextView_itv_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context
                        .getResources().getDisplayMetrics()));
        mTextColor = ta.getColor(R.styleable.IconTextView_itv_textColor, 0xff0099cc);
        mGap = ta.getDimensionPixelSize(R.styleable.IconTextView_itv_gap,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context
                        .getResources().getDisplayMetrics()));
        mIconWidth = ta.getDimensionPixelSize(R.styleable.IconTextView_itv_iconWidth, 0);
        mIconHeight = ta.getDimensionPixelSize(R.styleable.IconTextView_itv_iconHeight, 0);
        mExtroHeight = ta.getDimensionPixelSize(R.styleable.IconTextView_itv_extroHeight, 10);

        mDrawable = ta.getDrawable(R.styleable.IconTextView_itv_icon);
        mDirection = Direction.values()[ta.getInt(R.styleable.IconTextView_itv_direction, 0)];
        mText = ta.getString(R.styleable.IconTextView_itv_text);

        ta.recycle();

        if (mTextBound == null) {
            mTextBound = new Rect();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        // 计算出最小的滑动距离
        int touchSlop;
        if (context == null) {
            touchSlop = ViewConfiguration.getTouchSlop();
        } else {
            final ViewConfiguration config = ViewConfiguration.get(context);
            touchSlop = config.getScaledTouchSlop();
        }
        mTouchSlop = touchSlop * touchSlop;
        inTapRegion = false;
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        fixDrawableSize();
        mFontMetricsInt =  mPaint.getFontMetricsInt();
    }

    private void fixDrawableSize() {
        if (mDrawable == null) {
            return;
        }
        // 修正给定图标的宽度高度值
        int drawableWidth = mDrawable.getIntrinsicWidth();
        int drawableHeight = mDrawable.getIntrinsicHeight();
        if (mIconWidth == 0 || drawableWidth < mIconWidth) {
            mIconWidth = drawableWidth;
        }
        if (mIconHeight == 0 || drawableHeight < mIconHeight) {
            mIconHeight = drawableHeight;
        }
    }

    public void setOnIconTextClickListener(OnIconTextClickListener onIconTextClickListener) {
        mOnIconTextClickListener = onIconTextClickListener;
    }

    public void setTextSize(int textSize) {
        if (mTextSize != textSize) {
            mTextSize = textSize;
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            requestLayout();
            invalidate();
        }
    }

    private void setTextSize(int unit, int size) {
        mPaint.setTextSize(TypedValue.applyDimension(unit, size, getResources().getDisplayMetrics
                ()));
    }

    public void setTextColor(int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            invalidate();
        }
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(mText)) {
            mText = text;
            return;
        }

        if (!mText.equals(text)) {
            mText = text;
            mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
            requestLayout();
            invalidate();
        }
    }

    public String getText() {
        return mText;
    }

    public void setGap(int gap) {
        if (mGap != gap) {
            mGap = gap;
            requestLayout();
            invalidate();
        }
    }

    public void setIconWidth(int iconWidth) {
        if (mIconWidth != iconWidth) {
            mIconWidth = iconWidth;
            requestLayout();
            invalidate();
        }
    }

    public void setIconHight(int iconHeight) {
        if (mIconHeight != iconHeight) {
            mIconWidth = iconHeight;
            requestLayout();
            invalidate();
        }
    }

    public void setDrawable(Drawable drawable) {
        if (mDrawable != drawable) {
            mDrawable = drawable;
            fixDrawableSize();
            requestLayout();
            invalidate();
        }
    }

    public void setIconRes(int iconRes) {
        if (iconRes > 0) {
            mDrawable = getResources().getDrawable(iconRes);
            fixDrawableSize();
            requestLayout();
            invalidate();
        }
    }

    public void setDirection(Direction direction) {
        if (mDirection != direction) {
            mDirection = direction;
            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize - getPaddingLeft() - getPaddingRight();
        } else {
            width = measureAdjustSize(widthSize, widthMode);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize - getPaddingTop() - getPaddingBottom();
        } else {
            height = measureAdjustHeight(heightSize, heightMode);
        }

        if (mCacheBound == null) {
            mCacheBound = new Rect();
        }
        mCacheBound.left = getPaddingLeft();
        mCacheBound.top = getPaddingTop();
        mCacheBound.right = getPaddingLeft() + width;
        mCacheBound.bottom = getPaddingTop() + height;


        composeDrawableBound();
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height +
                getPaddingBottom() + getPaddingTop() + mExtroHeight);
    }

    private int measureAdjustHeight(int heightSize, int heightMode) {
        int height;
        int needHeight;
        int realHeight = heightSize - getPaddingTop() - getPaddingBottom();

        if (mDirection == Direction.TOP || mDirection == Direction.BOTTOM) {
            needHeight = mIconHeight + mGap + mTextBound.height();
        } else {
            needHeight = Math.max(mIconHeight, mTextBound.height());
        }

        height = needHeight;

        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(needHeight, realHeight);
        }
        return height;
    }

    private int measureAdjustSize(int widthSize, int widthMode) {
        int width;
        int needWidth;
        int realWidth = widthSize - getPaddingLeft() - getPaddingRight();

        if (mDirection == Direction.TOP || mDirection == Direction.BOTTOM) {
            needWidth = Math.max(mTextBound.width(), mIconWidth);
        } else {
            needWidth = mIconWidth + mTextBound.width() + mGap;
        }
        width = needWidth;

        if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(needWidth, realWidth);
        }
        return width;
    }

    /**
     * @Author Jliuer
     * @Date 2017/3/30/15:19
     * @Email Jliuer@aliyun.com
     * @Description 确定Icon的位置
     */
    private void composeDrawableBound() {
        if (mCacheBound == null || mDrawable == null) {
            return;
        }

        int tempGap;
        if (mIconBound == null) {
            mIconBound = new Rect();
        }

        switch (mDirection) {
            case TOP:
                tempGap = (int) ((mCacheBound.width() - mIconWidth) / 2.0f + 0.5f);
                mIconBound.left = mCacheBound.left + tempGap;
                tempGap = (int) ((mCacheBound.height() - (mIconHeight + mGap + mTextBound.height
                        ())) / 2 + 0.5f);
                mIconBound.top = mCacheBound.top + tempGap;
                mIconBound.right = mIconBound.left + mIconWidth;
                mIconBound.bottom = mIconBound.top + mIconHeight;
                break;
            case LEFT:
                tempGap = (int) ((mCacheBound.width() - (mIconWidth + mGap + mTextBound.width()))
                        / 2 + 0.5f);
                mIconBound.left = mCacheBound.left + tempGap;
                tempGap = (int) ((mCacheBound.height() - mIconHeight) / 2.0f + 0.5f);
                mIconBound.top = mCacheBound.top + tempGap + (mFontMetricsInt.ascent-mFontMetricsInt.top)/2;
                mIconBound.right = mIconBound.left + mIconWidth;
                mIconBound.bottom = mIconBound.top + mIconHeight;
                break;
            case RIGHT:
                tempGap = (int) ((mCacheBound.width() - (mIconWidth + mGap + mTextBound.width()))
                        / 2 + 0.5f);
                mIconBound.left = mCacheBound.right - tempGap - mIconWidth;
                tempGap = (int) ((mCacheBound.height() - mIconHeight) / 2.0f + 0.5f);
                mIconBound.top = mCacheBound.top + tempGap+ (mFontMetricsInt.ascent-mFontMetricsInt.top)/2;
                mIconBound.right = mCacheBound.right - tempGap;
                mIconBound.bottom = mIconBound.top + mIconHeight;
                break;
            case BOTTOM:
                tempGap = (int) ((mCacheBound.width() - mIconWidth) / 2.0f + 0.5f);
                mIconBound.left = mCacheBound.left + tempGap;
                tempGap = (int) ((mCacheBound.height() - (mIconHeight + mGap + mTextBound.height
                        ())) / 2 + 0.5f);
                mIconBound.top = mCacheBound.bottom - tempGap - mIconHeight;
                mIconBound.right = mIconBound.left + mIconWidth;
                mIconBound.bottom = mCacheBound.bottom - tempGap;
                break;
            default:
                break;
        }
        mDrawable.setBounds(mIconBound);
    }
    // 区域可控点击
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mStartX = (int) event.getX();
//                mStartY = (int) event.getY();
//                inTapRegion = true;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int dx = (int) (event.getX() - mStartX);
//                int dy = (int) (event.getY() - mStartY);
//
//                int touchSlop = dx * dx + dy * dy;
//                if (touchSlop > mTouchSlop) {
//                    inTapRegion = false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (inTapRegion) {
//                    if ((mStartX > mCacheBound.left && mStartX < mCacheBound.right)
//                            && (mStartY > mCacheBound.top && mStartY < mCacheBound.bottom)) {
//                        if (mOnIconTextClickListener != null) {
//                            mOnIconTextClickListener.onIconTextClick(this);
//                        }
//                    }
//                }
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null) {
            return;
        }
        switch (mDirection) {
            case TOP:
                mDrawable.draw(canvas);
                canvas.drawText(mText, 0, mText.length(), mCacheBound.centerX()-mTextBound.width()/2,
                        mIconBound.bottom + mGap + mTextBound.height(), mPaint);
                break;
            case LEFT:
                mDrawable.draw(canvas);
                canvas.drawText(mText, 0, mText.length(), mIconBound.right + mGap,
                        mCacheBound.top + mTextBound.height() + (mCacheBound.height() -
                                mTextBound.height()) / 2, mPaint);
                break;
            case RIGHT:
                mDrawable.draw(canvas);
                canvas.drawText(mText, 0, mText.length(), mCacheBound.left,
                        mCacheBound.top + mTextBound.height() + (mCacheBound.height() -
                                mTextBound.height()) / 2, mPaint);
                break;
            case BOTTOM:
                mDrawable.draw(canvas);
                canvas.drawText(mText, 0, mText.length(),
                        mCacheBound.centerX()-mTextBound.width()/2,
                        mIconBound.top - mGap, mPaint);  // 文字的起点是第一个文字的左下角位置开始
                break;
            default:
                break;
        }
    }
}