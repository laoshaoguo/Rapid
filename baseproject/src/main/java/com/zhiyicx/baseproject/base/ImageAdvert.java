package com.zhiyicx.baseproject.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ImageAdvert implements Serializable, Parcelable {
    private static final long serialVersionUID = 124L;
    private String link;
    // net
    private String image;
    // base64
    private String base64Image;
    private String duration;

    public int getDuration() {
        if (duration == null) {
            return 2;
        }
        try {
            return Integer.parseInt(duration.replaceAll("\\.\\d+", ""));
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.link);
        dest.writeString(this.image);
        dest.writeString(this.base64Image);
        dest.writeString(this.duration);
    }

    public ImageAdvert() {
    }

    protected ImageAdvert(Parcel in) {
        this.link = in.readString();
        this.image = in.readString();
        this.base64Image = in.readString();
        this.duration = in.readString();
    }

    public static final Creator<ImageAdvert> CREATOR = new Creator<ImageAdvert>() {
        @Override
        public ImageAdvert createFromParcel(Parcel source) {
            return new ImageAdvert(source);
        }

        @Override
        public ImageAdvert[] newArray(int size) {
            return new ImageAdvert[size];
        }
    };
}