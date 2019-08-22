package com.haohaishengshi.haohaimusic.data.beans;


import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.haohaishengshi.haohaimusic.data.source.local.data_convert.PaidNoteConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description 专辑列表
 */
@Entity
public class MusicAlbumListBean extends BaseListBean implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;
    /**
     * id : 1
     * created_at : 2017-03-10 18:05:02
     * updated_at : 2017-03-10 18:05:03
     * title : 专辑1
     * intro : 这里是简介
     * storage : 1
     * taste_count : 0
     * share_count : 0
     * comment_count : 0
     * collect_count : 0
     */
    @Id(autoincrement = true)
    Long _id;
    @Unique
    private int id;
    private String created_at;
    private String updated_at;
    private String title;
    @Convert(converter = MusicAlbumLIstConverter.class, columnType = String.class)
    private StorageBean storage;
    private int taste_count;
    private String intro;
    private int share_count;
    private int comment_count;
    private int collect_count;
    private boolean has_collect;
    @Convert(converter = PaidNoteConverter.class, columnType = String.class)
    private PaidNote paid_node;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StorageBean getStorage() {
        return storage;
    }

    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }

    public int getTaste_count() {
        return taste_count;
    }

    public void setTaste_count(int taste_count) {
        this.taste_count = taste_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public static class MusicAlbumLIstConverter implements PropertyConverter<StorageBean, String> {
        @Override
        public StorageBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(StorageBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.title);
        dest.writeParcelable(this.storage, flags);
        dest.writeInt(this.taste_count);
        dest.writeString(this.intro);
        dest.writeInt(this.share_count);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.collect_count);
        dest.writeByte(this.has_collect ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.paid_node, flags);
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean getHas_collect() {
        return this.has_collect;
    }

    public void setHas_collect(boolean has_collect) {
        this.has_collect = has_collect;
    }

    public PaidNote getPaid_node() {
        return this.paid_node;
    }

    public void setPaid_node(PaidNote paid_node) {
        this.paid_node = paid_node;
    }

    public MusicAlbumListBean() {
    }

    protected MusicAlbumListBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.title = in.readString();
        this.storage = in.readParcelable(StorageBean.class.getClassLoader());
        this.taste_count = in.readInt();
        this.intro = in.readString();
        this.share_count = in.readInt();
        this.comment_count = in.readInt();
        this.collect_count = in.readInt();
        this.has_collect = in.readByte() != 0;
        this.paid_node = in.readParcelable(PaidNote.class.getClassLoader());
    }

    @Generated(hash = 78686052)
    public MusicAlbumListBean(Long _id, int id, String created_at, String updated_at, String title,
                              StorageBean storage, int taste_count, String intro, int share_count, int comment_count,
                              int collect_count, boolean has_collect, PaidNote paid_node) {
        this._id = _id;
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.title = title;
        this.storage = storage;
        this.taste_count = taste_count;
        this.intro = intro;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.collect_count = collect_count;
        this.has_collect = has_collect;
        this.paid_node = paid_node;
    }

    public static final Creator<MusicAlbumListBean> CREATOR = new Creator<MusicAlbumListBean>() {
        @Override
        public MusicAlbumListBean createFromParcel(Parcel source) {
            return new MusicAlbumListBean(source);
        }

        @Override
        public MusicAlbumListBean[] newArray(int size) {
            return new MusicAlbumListBean[size];
        }
    };
}
