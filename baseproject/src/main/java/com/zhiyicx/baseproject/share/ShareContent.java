package com.zhiyicx.baseproject.share;


import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

import java.io.File;
import java.io.Serializable;


/**
 * @Describe 分享内容
 * @Author Jungle68
 * @Date 2016/12/20
 * @Contact master.jungle68@gmail.com
 */

public class ShareContent implements Serializable {

    /**
     * title : [uname]-正在直播
     * content : 我在智播直播啦!来一起看我直播吧
     * url : http://zhibo.zhiyicx.com/index/index/show?user_id=[uid]
     * image :
     */

    private String title;
    private String content;
    private String url;
    private String image;
    private Bitmap bitmap;
    private
    @DrawableRes
    int resImage;
    private File file;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getResImage() {
        return resImage;
    }

    public void setResImage(int resImage) {
        this.resImage = resImage;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "ShareContent{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", bitmap=" + bitmap +
                ", resImage=" + resImage +
                ", file=" + file +
                '}';
    }
}
