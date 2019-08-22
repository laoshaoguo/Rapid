package com.klinker.android.link_builder;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/30
 * @contact master.jungle68@gmail.com
 */
public class NetUrlHandleBean implements Serializable {

    private static final long serialVersionUID = 2430882992428433335L;
    private HashSet<Integer> positions;
    private String text;

    public NetUrlHandleBean(String text) {
        this.text = text;
        caculatePositon();
    }

    /**
     * 计算位置
     */
    private void caculatePositon() {
        positions = new HashSet<>();
        if (!TextUtils.isEmpty(text)) {
            String reg = "<{0,1}((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                    "(:[0-9]{1,4})*(/[#a-zA-Z0-9\\&%_\\./-~-]*)?>{0,1}";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher1 = pattern.matcher(text);
            int urlPosition = 0;
            while (matcher1.find()) {
                int start = matcher1.start();
                if (start >= 0) {
                    String[] tmp = text.substring(0, start + 1).split(Link.DEFAULT_NET_SITE);
                    int realPosition = urlPosition + tmp.length - 1;
                    positions.add(realPosition);
                    urlPosition++;
                }
            }
        }

    }

    public HashSet<Integer> getPositions() {
        return positions;
    }

    public void setPositions(HashSet<Integer> positions) {
        this.positions = positions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
