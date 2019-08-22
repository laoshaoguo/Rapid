package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.cache.CacheBean;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/24
 * @Contact master.jungle68@gmail.com
 */

public class VerifiedBean extends CacheBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 8258245331800324562L;
    /**
     * "type" : "user"    user 个人, org 企业
     * "icon" : "http:hahhdgh.jpg"
     * "status" :0 - 待审核, 1 - 通过, 2 - 拒绝。
     */

    private String type;
    private String icon;
    private int status;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public VerifiedBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.icon);
        dest.writeInt(this.status);
        dest.writeString(this.description);
    }

    protected VerifiedBean(Parcel in) {
        this.type = in.readString();
        this.icon = in.readString();
        this.status = in.readInt();
        this.description = in.readString();
    }

    public static final Creator<VerifiedBean> CREATOR = new Creator<VerifiedBean>() {
        @Override
        public VerifiedBean createFromParcel(Parcel source) {
            return new VerifiedBean(source);
        }

        @Override
        public VerifiedBean[] newArray(int size) {
            return new VerifiedBean[size];
        }
    };

    @Override
    public String toString() {
        return "VerifiedBean{" +
                "type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
