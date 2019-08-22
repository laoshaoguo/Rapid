package com.zhiyicx.common.widget;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @Describe  无下划线超链接，
 *              使用 textColorLink、textColorHighlight
 *              分别修改超链接前景色和按下时的颜色
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */
public class NoLineClickSpan extends ClickableSpan {

    public NoLineClickSpan() {
        super();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false); //去掉下划线
    }

    @Override
    public void onClick(View widget) {

    }

}