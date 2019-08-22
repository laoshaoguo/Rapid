package com.zhiyicx.baseproject.widget.imageview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @Describe 正方形的 ImageView
 * @Author Jungle68
 * @Date 2017/2/21
 * @Contact master.jungle68@gmail.com
 */

public class RectImageView extends FilterImageView {

    public RectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RectImageView(Context context) {
        super(context);
    }

    public RectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width * 0.6f);
        setMeasuredDimension(width, height);
    }

}
