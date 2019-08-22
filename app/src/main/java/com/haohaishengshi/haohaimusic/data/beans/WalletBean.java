package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * @Describe 钱包的数据类
 * @Author Jungle68
 * @Date 2017/5/24
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class WalletBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 123L;

    /**
     * id : 1
     * user_id : 1
     * balance : 0
     * created_at : 2017-05-22 00:00:00
     * updated_at : 2017-05-22 00:00:00
     * deleted_at : null
     */
    @Id
    private Long id;
    @Unique
    @SerializedName("owner_id")
    private int user_id;
    private double balance;
    private double total_income;
    private double total_expenses;
    private String created_at;
    private String updated_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }

    public double getTotal_expenses() {
        return total_expenses;
    }

    public void setTotal_expenses(double total_expenses) {
        this.total_expenses = total_expenses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.user_id);
        dest.writeDouble(this.balance);
        dest.writeDouble(this.total_income);
        dest.writeDouble(this.total_expenses);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public WalletBean() {
    }

    protected WalletBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readInt();
        this.balance = in.readDouble();
        this.total_income = in.readDouble();
        this.total_expenses = in.readDouble();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    @Generated(hash = 1950383149)
    public WalletBean(Long id, int user_id, double balance, double total_income,
                      double total_expenses, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.balance = balance;
        this.total_income = total_income;
        this.total_expenses = total_expenses;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static final Creator<WalletBean> CREATOR = new Creator<WalletBean>() {
        @Override
        public WalletBean createFromParcel(Parcel source) {
            return new WalletBean(source);
        }

        @Override
        public WalletBean[] newArray(int size) {
            return new WalletBean[size];
        }
    };
}
