package com.zhiyicx.baseproject.widget.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.zhiyicx.baseproject.R;


/**
 * @author LiuChao
 * @describe 能够显示和隐藏密码（密码明文，暗文切换）
 * @date 2017/1/6
 * @contact email:450127106@qq.com
 */
public class PasswordEditText extends EditText {
    // 当前密码可见时的图片
    private static final int SHOW_ICON = R.mipmap.login_ico_copeneye;
    // 当前密码不可见时的图片
    private static final int HIDE_ICON = R.mipmap.login_ico_closeye;

    //注意设置密码的明暗文，需要组合使用InputType
    private static final int hide = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    private static final int show = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

    private Drawable mHideDrawable;
    private Drawable mShowDrawable;

    /**
     * 是否显示密码
     */
    private boolean hasShowPassWord = false;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initDrawable();
        /**
         * 默认隐藏密码
         */
        setInputType(hide);
        setRightIcon(false);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
            if (touchable) {
                setInputType(hasShowPassWord ? show : hide);
                setRightIcon(hasShowPassWord);
            }
        }
        return super.onTouchEvent(event);
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param hasShowPassWord
     */
    protected void setRightIcon(boolean hasShowPassWord) {

        Drawable right = hasShowPassWord ? mShowDrawable : mHideDrawable;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
        this.hasShowPassWord = !hasShowPassWord;
    }

    private void initDrawable() {
        mHideDrawable = getResources().getDrawable(HIDE_ICON);
        mShowDrawable = getResources().getDrawable(SHOW_ICON);
        mHideDrawable.setBounds(0, 0, mHideDrawable.getIntrinsicWidth(), mHideDrawable.getIntrinsicHeight());
        mShowDrawable.setBounds(0, 0, mShowDrawable.getIntrinsicWidth(), mShowDrawable.getIntrinsicHeight());
    }

}
