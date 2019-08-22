package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * ThinkSNS Plus
 * Copyright (c) 2018 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @Author Jliuer
 * @Date 2018/09/06/13:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class Avatar implements Parcelable,Serializable{


    private static final long serialVersionUID = -59675161960691570L;
    /**
     * url :  ?rule=w_100,h_100,b_60 可变参数，指定宽高和模糊：w_100,h_100,b_60
     * vendor : aliyun-oss
     * mime : image/png
     * size : 802930
     * dimension : {"width":2800,"height":1867}
     */

    private String url;
    private String vendor;
    private String mime;
    private int size;
    private DimensionBean dimension;



    public Avatar(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DimensionBean getDimension() {
        return dimension;
    }

    public void setDimension(DimensionBean dimension) {
        this.dimension = dimension;
    }

    public static class DimensionBean implements Parcelable,Serializable{
        private static final long serialVersionUID = -519873982595346076L;
        /**
         * width : 2800
         * height : 1867
         */

        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.width);
            dest.writeInt(this.height);
        }

        public DimensionBean() {
        }

        protected DimensionBean(Parcel in) {
            this.width = in.readInt();
            this.height = in.readInt();
        }

        public static final Creator<DimensionBean> CREATOR = new Creator<DimensionBean>() {
            @Override
            public DimensionBean createFromParcel(Parcel source) {
                return new DimensionBean(source);
            }

            @Override
            public DimensionBean[] newArray(int size) {
                return new DimensionBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.vendor);
        dest.writeString(this.mime);
        dest.writeInt(this.size);
        dest.writeParcelable(this.dimension, flags);
    }

    public Avatar() {
    }

    protected Avatar(Parcel in) {
        this.url = in.readString();
        this.vendor = in.readString();
        this.mime = in.readString();
        this.size = in.readInt();
        this.dimension = in.readParcelable(DimensionBean.class.getClassLoader());
    }

    public static final Creator<Avatar> CREATOR = new Creator<Avatar>() {
        @Override
        public Avatar createFromParcel(Parcel source) {
            return new Avatar(source);
        }

        @Override
        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Avatar avatar = (Avatar) o;

        return url != null ? url.equals(avatar.url) : avatar.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
