package me.iwf.photopicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import me.iwf.photopicker.R;

/**
 * @Describe show #mPressedColor color when pressed
 * @Author Jungle68
 * @Date 2017/2/29
 * @Contact master.jungle68@gmail.com
 */
public class GifTagImageView extends android.support.v7.widget.AppCompatImageView {
    private static final int SHAPE_SQUARE = 0;
    private static final int SHAPE_CIRLCE = 1;
    private static final int DEFAULT_PRESSED_COLOR = 0x26000000; // cover：#000000 alpha 15%
    private int mPressedColor = DEFAULT_PRESSED_COLOR; // pressed color
    private int mShape = SHAPE_SQUARE;
    private Paint mPaint;


    private Bitmap mGifImageBitmap;

    private boolean mIshowGifTag;


    public GifTagImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }


    public GifTagImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifTagImageView(Context context) {
        this(context, null);
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifTagImageView);
        mPressedColor = array.getInteger(R.styleable.GifTagImageView_pressColor, DEFAULT_PRESSED_COLOR);
        mShape = array.getInteger(R.styleable.GifTagImageView_pressShape, SHAPE_SQUARE);
        array.recycle();
        mPaint = new TextPaint();
        mPaint.setColor(mPressedColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed()) {
            switch (mShape) {
                // square
                case SHAPE_SQUARE:
                    canvas.drawColor(mPressedColor);
                    break;
                // circle
                case SHAPE_CIRLCE:
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
                    break;
                default:

            }
        }
        if (mIshowGifTag && mGifImageBitmap != null) {
            canvas.drawBitmap(mGifImageBitmap, getWidth() - mGifImageBitmap.getWidth(), getHeight() - mGifImageBitmap.getHeight(), null);
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        invalidate();
    }

    /**
     * @param ishowGifTag 是否显示 gif 标识
     */
    public void setIshowGifTag(boolean ishowGifTag) {
        mIshowGifTag = ishowGifTag;
        if (ishowGifTag) {
            if (mGifImageBitmap == null) {
                mGifImageBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_gif);
            }
            postInvalidate();
        }
    }

}
