package com.zhiyicx.baseproject.impl.photoselector;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Toll implements Parcelable, Serializable {

    private static final long serialVersionUID = 123L;

    public static final int LOOK_TOLL = 1000;// 查看收费
    public static final String LOOK_TOLL_TYPE = "read";// 查看收费
    public static final String LOOK_TOLL_TYPE_NONE = "none";// 查看收费
    public static final int DOWNLOAD_TOLL = 2000;// 下载收费
    public static final String DOWNLOAD_TOLL_TYPE = "download";// 下载收费

    int toll_type;
    int paid_node;
    String toll_type_string;
    long toll_money;
    long custom_money;
    Boolean isPaid;

    public Toll() {
    }

    public Toll(int toll_type, long toll_money, long custom_money) {
        this.toll_type = toll_type;
        this.toll_money = toll_money;
        this.custom_money = custom_money;
    }

    public int getPaid_node() {
        return paid_node;
    }

    public void setPaid_node(int paid_node) {
        this.paid_node = paid_node;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getToll_type_string() {
        if (toll_type_string == null) {
            toll_type_string = "";
        }
        return toll_type_string;
    }

    public void setToll_type_string(String toll_type_string) {
        this.toll_type_string = toll_type_string;
    }

    public void setToll_type(int toll_type) {
        this.toll_type = toll_type;
    }

    public void setToll_money(long toll_money) {
        this.toll_money = toll_money;
    }

    public long getCustom_money() {
        return custom_money;
    }

    public void setCustom_money(long custom_money) {
        this.custom_money = custom_money;
    }

    public int getToll_type() {
        return toll_type;
    }

    public long getToll_money() {
        return toll_money;
    }

    private Toll(int toll_type, long toll_money) {
        this.toll_type = toll_type;
        this.toll_money = toll_money;
    }


    public void reset() {
        custom_money = 0;
        toll_money = 0;
        toll_type = 0;
    }

    @Override
    public String toString() {
        return "toll_type=" + (toll_type == LOOK_TOLL ? LOOK_TOLL_TYPE : DOWNLOAD_TOLL_TYPE)
                + ",toll_money=" + toll_money
                + ",custom_money=" + custom_money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Toll toll = (Toll) o;

        if (toll_type != toll.toll_type) {
            return false;
        }
        if (toll_money != toll.toll_money) {
            return false;
        }
        return custom_money == toll.custom_money;
    }

    @Override
    public int hashCode() {
        int result = toll_type;
        result = 31 * result + (int) (toll_money ^ (toll_money >>> 32));
        result = 31 * result + (int) (custom_money ^ (custom_money >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.toll_type);
        dest.writeInt(this.paid_node);
        dest.writeString(this.toll_type_string);
        dest.writeLong(this.toll_money);
        dest.writeLong(this.custom_money);
        dest.writeValue(this.isPaid);
    }

    protected Toll(Parcel in) {
        this.toll_type = in.readInt();
        this.paid_node = in.readInt();
        this.toll_type_string = in.readString();
        this.toll_money = in.readLong();
        this.custom_money = in.readLong();
        this.isPaid = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<Toll> CREATOR = new Creator<Toll>() {
        @Override
        public Toll createFromParcel(Parcel source) {
            return new Toll(source);
        }

        @Override
        public Toll[] newArray(int size) {
            return new Toll[size];
        }
    };
}