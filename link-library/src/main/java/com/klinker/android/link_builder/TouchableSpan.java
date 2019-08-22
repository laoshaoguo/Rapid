/*
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.klinker.android.link_builder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TouchableSpan extends TouchableBaseSpan {

    private final Link link;
    private int textColor;
    private int textColorOfHighlightedLink;

    /**
     * Construct new TouchableSpan using the link
     *
     * @param link
     */
    public TouchableSpan(Context context, Link link) {
        this.link = link;

        if (link.getTextColor() == 0) {
            this.textColor = getDefaultColor(context, R.styleable.LinkBuilder_defaultLinkColor);
        } else {
            this.textColor = link.getTextColor();
        }

        if (link.getTextColorOfHighlightedLink() == 0) {
            this.textColorOfHighlightedLink = getDefaultColor(context, R.styleable.LinkBuilder_defaultTextColorOfHighlightedLink);

            if (this.textColorOfHighlightedLink == Link.DEFAULT_COLOR) {
                // don't use the default of light blue for this color
                this.textColorOfHighlightedLink = textColor;
            }
        } else {
            this.textColorOfHighlightedLink = link.getTextColorOfHighlightedLink();
        }
    }

    /**
     * Finds the default color for links based on the current theme.
     *
     * @param context activity
     * @param index   index of attribute to retrieve based on current theme
     * @return color as an integer
     */
    private int getDefaultColor(Context context, int index) {
        TypedArray array = obtainStyledAttrsFromThemeAttr(context, R.attr.linkBuilderStyle, R.styleable.LinkBuilder);
        int color = array.getColor(index, Link.DEFAULT_COLOR);
        array.recycle();

        return color;
    }

    /**
     * This TouchableSpan has been clicked.
     *
     * @param widget TextView containing the touchable span
     */
    @Override
    public void onClick(View widget) {
        // handle the click
        if (link.getClickListener() != null) {
            List<String> splitTextList = new ArrayList<>();
            if (link.getLinkMetadata() != null) {
                NetUrlHandleBean netUrlHandleBean = (NetUrlHandleBean) link.getLinkMetadata().getSeriObj(LinkMetadata.METADATA_KEY_COTENT);
                String targetStr = netUrlHandleBean.getText();
                String reg = "<{0,1}((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                        "(:[0-9]{1,4})*(/[#a-zA-Z0-9\\&%_\\./-~-]*)?>{0,1}";
                Pattern pattern = Pattern.compile(reg);
                Matcher matcher1 = pattern.matcher(targetStr);
                while (matcher1.find()) {
                    String result = targetStr.substring(matcher1.start(), matcher1.end());
                    result = result.replaceFirst("<", "");
                    result = result.replaceFirst(">", "");
                    splitTextList.add(result);
                }
            }
            try {
                String backText = splitTextList.isEmpty() ? link.getText() : splitTextList.get(position);
                link.getClickListener().onClick(backText, link.getLinkMetadata());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
        super.onClick(widget);
    }

    /**
     * This TouchableSpan has been long clicked.
     *
     * @param widget TextView containing the touchable span
     */
    @Override
    public void onLongClick(View widget) {
        // handle the long click
        if (link.getLongClickListener() != null) {
            List<String> splitTextList = new ArrayList<>();
            if (link.getLinkMetadata() != null) {
                NetUrlHandleBean netUrlHandleBean = (NetUrlHandleBean) link.getLinkMetadata().getSeriObj(LinkMetadata.METADATA_KEY_COTENT);
                String targetStr = netUrlHandleBean.getText();
                String reg = "<{0,1}((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                        "(:[0-9]{1,4})*(/[#a-zA-Z0-9\\&%_\\./-~-]*)?>{0,1}";
                Pattern pattern = Pattern.compile(reg);
                Matcher matcher1 = pattern.matcher(targetStr);
                while (matcher1.find()) {
                    String result = targetStr.substring(matcher1.start(), matcher1.end());
                    result = result.replaceFirst("<", "");
                    result = result.replaceFirst(">", "");
                    splitTextList.add(result);
                }
            }
            try {
                String backText = splitTextList.isEmpty() ? link.getText() : splitTextList.get(position);
                link.getLongClickListener().onLongClick(backText, link.getLinkMetadata());
                Log.d("onLongClick:", position + "");
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        super.onLongClick(widget);
    }

    /**
     * Set the alpha for the color based on the alpha factor
     *
     * @param color  original color
     * @param factor how much we want to scale the alpha to
     * @return new color with scaled alpha
     */
    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Draw the links background and set whether or not we want it to be underlined or bold
     *
     * @param ds the link
     */
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setUnderlineText(link.isUnderlined());
        ds.setFakeBoldText(link.isBold());
        ds.setColor(touched ? textColorOfHighlightedLink : textColor);
        ds.bgColor = touched ? adjustAlpha(textColor, link.getHighlightAlpha()) : Color.TRANSPARENT;
        if (link.getTypeface() != null) {
            ds.setTypeface(link.getTypeface());
        }
    }

    protected static TypedArray obtainStyledAttrsFromThemeAttr(Context context, int themeAttr, int[] styleAttrs) {
        // Need to get resource id of style pointed to from the theme attr
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(themeAttr, outValue, true);
        final int styleResId = outValue.resourceId;

        // Now return the values (from styleAttrs) from the style
        return context.obtainStyledAttributes(styleResId, styleAttrs);
    }
}