package com.trycatch.mysnackbar;

public enum Prompt {
    /**
     * 红色,错误
     */
    ERROR(R.drawable.msg_box_remind, R.color.white),

    /**
     * 红色,警告
     */
    WARNING(R.drawable.msg_box_remind, R.color.white),

    /**
     * 绿色,成功
     */
    SUCCESS(R.drawable.msg_box_succeed, R.color.white),

    /**
     * 绿色,成功,完成后退出
     */
    DONE(R.drawable.msg_box_succeed, R.color.white);

    private int resIcon;
    private int backgroundColor;

    Prompt(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
