package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.haohaishengshi.haohaimusic.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 音乐详情
 */
public class MusicDetaisBean extends BaseListBean implements Serializable,Parcelable{
    private static final long serialVersionUID = 5177124821653334394L;
    /**
     * id : 1
     * created_at : 2017-03-16 17:11:26
     * updated_at : 2017-07-20 03:39:00
     * deleted_at : null
     * title : 水手公园
     * singer : {"id":1,"created_at":"2017-03-16 17:22:04","updated_at":"2017-03-16 17:22:08","name":"群星","cover":{"id":108,"size":"3024x3024"}}
     * storage : {"id":105,"amount":100,"type":"download","paid":true,"paid_node":2}
     * last_time : 180
     * lyric : lalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallalalaallal
     * taste_count : 314
     * share_count : 0
     * comment_count : 12
     * has_like : true
     */
    @Id
    private Long id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private String title;
    @Convert(columnType = String.class,converter = SingerConvert.class)
    private SingerBean singer;
    @Convert(columnType = String.class,converter = MusicStorageConvert.class)
    private MusicStorageBean storage;
    private int last_time;
    private String lyric;
    private int taste_count;
    private int share_count;
    private int comment_count;
    private boolean has_like;

    @Override
    public Long getMaxId() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SingerBean getSinger() {
        return singer;
    }

    public void setSinger(SingerBean singer) {
        this.singer = singer;
    }

    public MusicStorageBean getStorage() {
        return storage;
    }

    public void setStorage(MusicStorageBean storage) {
        this.storage = storage;
    }

    public int getLast_time() {
        return last_time;
    }

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
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

    public boolean isHas_like() {
        return has_like;
    }

    public void setHas_like(boolean has_like) {
        this.has_like = has_like;
    }

    public static class SingerBean implements Parcelable,Serializable{

        private static final long serialVersionUID=1243L;

        /**
         * id : 1
         * created_at : 2017-03-16 17:22:04
         * updated_at : 2017-03-16 17:22:08
         * name : 群星
         * cover : {"id":108,"size":"3024x3024"}
         */

        private int id;
        private String created_at;
        private String updated_at;
        private String name;
        private StorageBean cover;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public StorageBean getCover() {
            return cover;
        }

        public void setCover(StorageBean cover) {
            this.cover = cover;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeString(this.name);
            dest.writeParcelable(this.cover, flags);
        }

        public SingerBean() {
        }

        protected SingerBean(Parcel in) {
            this.id = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.name = in.readString();
            this.cover = in.readParcelable(StorageBean.class.getClassLoader());
        }

        public static final Creator<SingerBean> CREATOR = new Creator<SingerBean>() {
            @Override
            public SingerBean createFromParcel(Parcel source) {
                return new SingerBean(source);
            }

            @Override
            public SingerBean[] newArray(int size) {
                return new SingerBean[size];
            }
        };
    }

    public static class MusicStorageBean implements Parcelable,Serializable{

        private static final long serialVersionUID=1243L;
        /**
         * id : 105
         * amount : 100
         * type : download
         * paid : true
         * paid_node : 2
         */

        private int id;
        private int amount;
        private String type;
        private boolean paid;
        private int paid_node;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public int getPaid_node() {
            return paid_node;
        }

        public void setPaid_node(int paid_node) {
            this.paid_node = paid_node;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.amount);
            dest.writeString(this.type);
            dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
            dest.writeInt(this.paid_node);
        }

        public MusicStorageBean() {
        }

        protected MusicStorageBean(Parcel in) {
            this.id = in.readInt();
            this.amount = in.readInt();
            this.type = in.readString();
            this.paid = in.readByte() != 0;
            this.paid_node = in.readInt();
        }

        public static final Creator<MusicStorageBean> CREATOR = new Creator<MusicStorageBean>() {
            @Override
            public MusicStorageBean createFromParcel(Parcel source) {
                return new MusicStorageBean(source);
            }

            @Override
            public MusicStorageBean[] newArray(int size) {
                return new MusicStorageBean[size];
            }
        };
    }

    public static class SingerConvert extends BaseConvert<SingerBean>{}
    public static class MusicStorageConvert extends BaseConvert<MusicStorageBean> {}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeString(this.title);
        dest.writeParcelable(this.singer, flags);
        dest.writeParcelable(this.storage, flags);
        dest.writeInt(this.last_time);
        dest.writeString(this.lyric);
        dest.writeInt(this.taste_count);
        dest.writeInt(this.share_count);
        dest.writeInt(this.comment_count);
        dest.writeByte(this.has_like ? (byte) 1 : (byte) 0);
    }

    public MusicDetaisBean() {
    }

    protected MusicDetaisBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.title = in.readString();
        this.singer = in.readParcelable(SingerBean.class.getClassLoader());
        this.storage = in.readParcelable(MusicStorageBean.class.getClassLoader());
        this.last_time = in.readInt();
        this.lyric = in.readString();
        this.taste_count = in.readInt();
        this.share_count = in.readInt();
        this.comment_count = in.readInt();
        this.has_like = in.readByte() != 0;
    }

    public static final Creator<MusicDetaisBean> CREATOR = new Creator<MusicDetaisBean>() {
        @Override
        public MusicDetaisBean createFromParcel(Parcel source) {
            return new MusicDetaisBean(source);
        }

        @Override
        public MusicDetaisBean[] newArray(int size) {
            return new MusicDetaisBean[size];
        }
    };
}
