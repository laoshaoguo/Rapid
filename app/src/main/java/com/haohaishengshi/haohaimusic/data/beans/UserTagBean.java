package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */
@Entity
public class UserTagBean extends BaseListBean implements Serializable,Parcelable{

    @Transient
    private static final long serialVersionUID = -7587963206423470086L;

    /**
     * {
     * "id": 1,
     * "name": "标签1",
     * "tag_category_id": 1
     * }
     */
    @Id
    private Long id;
    @SerializedName("name")
    private String tagName;
    private long tag_category_id;
    private boolean mine_has; // 用于本地标签当前用户上否有这个标签
    private int weight;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTag_category_id() {
        return tag_category_id;
    }

    public void setTag_category_id(long tag_category_id) {
        this.tag_category_id = tag_category_id;
    }

    public boolean isMine_has() {
        return mine_has;
    }

    public void setMine_has(boolean mine_has) {
        this.mine_has = mine_has;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public UserTagBean() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserTagBean that = (UserTagBean) o;

        if (tag_category_id != that.tag_category_id) {
            return false;
        }
        return tagName.equals(that.tagName);

    }

    @Override
    public int hashCode() {
        int result = tagName.hashCode();
        result = 31 * result + (int) (tag_category_id ^ (tag_category_id >>> 32));
        return result;
    }

    public boolean getMine_has() {
        return this.mine_has;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.tagName);
        dest.writeLong(this.tag_category_id);
        dest.writeByte(this.mine_has ? (byte) 1 : (byte) 0);
        dest.writeInt(this.weight);
    }

    protected UserTagBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.tagName = in.readString();
        this.tag_category_id = in.readLong();
        this.mine_has = in.readByte() != 0;
        this.weight = in.readInt();
    }

    @Generated(hash = 912960929)
    public UserTagBean(Long id, String tagName, long tag_category_id,
                       boolean mine_has, int weight) {
        this.id = id;
        this.tagName = tagName;
        this.tag_category_id = tag_category_id;
        this.mine_has = mine_has;
        this.weight = weight;
    }

    public static final Creator<UserTagBean> CREATOR = new Creator<UserTagBean>() {
        @Override
        public UserTagBean createFromParcel(Parcel source) {
            return new UserTagBean(source);
        }

        @Override
        public UserTagBean[] newArray(int size) {
            return new UserTagBean[size];
        }
    };

    @Override
    public String toString() {
        return "UserTagBean{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                ", tag_category_id=" + tag_category_id +
                ", mine_has=" + mine_has +
                ", weight=" + weight +
                '}';
    }
}
