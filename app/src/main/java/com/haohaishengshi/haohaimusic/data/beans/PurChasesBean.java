package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author Jliuer
 * @Date 2017/06/26/10:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PurChasesBean implements Parcelable{

    /**
     * id : 46
     * raw : 25
     * channel : file
     * subject : 购买动态附件
     * body : 购买动态《收拾收拾》的图片
     * amount : 0
     * user_id : null
     * extra : download
     * created_at : 2017-06-23 11:40:23
     * updated_at : 2017-06-23 11:40:23
     * paid : false
     */

    private int id;
    private String raw;
    private String channel;
    private String subject;
    private String body;
    private int amount;
    private int user_id;
    private String extra;
    private String created_at;
    private String updated_at;
    private boolean paid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.raw);
        dest.writeString(this.channel);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeInt(this.amount);
        dest.writeInt(this.user_id);
        dest.writeString(this.extra);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
    }

    public PurChasesBean() {
    }

    protected PurChasesBean(Parcel in) {
        this.id = in.readInt();
        this.raw = in.readString();
        this.channel = in.readString();
        this.subject = in.readString();
        this.body = in.readString();
        this.amount = in.readInt();
        this.user_id = in.readInt();
        this.extra = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.paid = in.readByte() != 0;
    }

    public static final Creator<PurChasesBean> CREATOR = new Creator<PurChasesBean>() {
        @Override
        public PurChasesBean createFromParcel(Parcel source) {
            return new PurChasesBean(source);
        }

        @Override
        public PurChasesBean[] newArray(int size) {
            return new PurChasesBean[size];
        }
    };
}
