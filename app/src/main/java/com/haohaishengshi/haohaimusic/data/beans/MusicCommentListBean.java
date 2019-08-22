package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @Author Jliuer
 * @Date 2017/03/16
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class MusicCommentListBean extends BaseListBean {
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    /**
     * id : 4
     * created_at : 2017-03-15 07:56:22
     * updated_at : 2017-03-15 07:56:22
     * comment_content : 213123
     * user_id : 1
     * reply_to_user_id : 0
     * music_id : 1
     * special_id : 0
     */
    @Id
    private Long _id;
    @Unique
    private Long id;
    @SerializedName("commentable_type")
    private String channel; // 数据所属扩展包名 目前可能的参数有 feeds musics news
    private String created_at;
    private String updated_at;
    @SerializedName("body")
    private String comment_content;
    private long  commentable_id;
    private long target_user;
    private long user_id;
    private long reply_user;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean fromUserInfoBean;
    @ToOne(joinProperty = "reply_user")
    private UserInfoBean toUserInfoBean;
    private int music_id;
    private int special_id;
    private Long comment_mark;
    private int state = SEND_SUCCESS;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 195439560)
    private transient MusicCommentListBeanDao myDao;
    @Generated(hash = 1634012568)
    public MusicCommentListBean(Long _id, Long id, String channel,
                                String created_at, String updated_at, String comment_content,
                                long commentable_id, long target_user, long user_id, long reply_user,
                                int music_id, int special_id, Long comment_mark, int state) {
        this._id = _id;
        this.id = id;
        this.channel = channel;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.comment_content = comment_content;
        this.commentable_id = commentable_id;
        this.target_user = target_user;
        this.user_id = user_id;
        this.reply_user = reply_user;
        this.music_id = music_id;
        this.special_id = special_id;
        this.comment_mark = comment_mark;
        this.state = state;
    }
    @Generated(hash = 541744387)
    public MusicCommentListBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getChannel() {
        return this.channel;
    }
    public void setChannel(String channel) {
        this.channel = channel;
    }
    public String getCreated_at() {
        return this.created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getUpdated_at() {
        return this.updated_at;
    }
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    public String getComment_content() {
        return this.comment_content;
    }
    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }
    public long getCommentable_id() {
        return this.commentable_id;
    }
    public void setCommentable_id(long commentable_id) {
        this.commentable_id = commentable_id;
    }
    public long getTarget_user() {
        return this.target_user;
    }
    public void setTarget_user(long target_user) {
        this.target_user = target_user;
    }
    public long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    public long getReply_user() {
        return this.reply_user;
    }
    public void setReply_user(long reply_user) {
        this.reply_user = reply_user;
    }
    public int getMusic_id() {
        return this.music_id;
    }
    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }
    public int getSpecial_id() {
        return this.special_id;
    }
    public void setSpecial_id(int special_id) {
        this.special_id = special_id;
    }
    public Long getComment_mark() {
        return this.comment_mark;
    }
    public void setComment_mark(Long comment_mark) {
        this.comment_mark = comment_mark;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    @Generated(hash = 262226026)
    private transient Long fromUserInfoBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 662071464)
    public UserInfoBean getFromUserInfoBean() {
        long __key = this.user_id;
        if (fromUserInfoBean__resolvedKey == null
                || !fromUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean fromUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                fromUserInfoBean = fromUserInfoBeanNew;
                fromUserInfoBean__resolvedKey = __key;
            }
        }
        return fromUserInfoBean;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1773681647)
    public void setFromUserInfoBean(@NotNull UserInfoBean fromUserInfoBean) {
        if (fromUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'user_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.fromUserInfoBean = fromUserInfoBean;
            user_id = fromUserInfoBean.getUser_id();
            fromUserInfoBean__resolvedKey = user_id;
        }
    }
    @Generated(hash = 89682145)
    private transient Long toUserInfoBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1850008252)
    public UserInfoBean getToUserInfoBean() {
        long __key = this.reply_user;
        if (toUserInfoBean__resolvedKey == null
                || !toUserInfoBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean toUserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                toUserInfoBean = toUserInfoBeanNew;
                toUserInfoBean__resolvedKey = __key;
            }
        }
        return toUserInfoBean;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1768320233)
    public void setToUserInfoBean(@NotNull UserInfoBean toUserInfoBean) {
        if (toUserInfoBean == null) {
            throw new DaoException(
                    "To-one property 'reply_user' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.toUserInfoBean = toUserInfoBean;
            reply_user = toUserInfoBean.getUser_id();
            toUserInfoBean__resolvedKey = reply_user;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeValue(this.id);
        dest.writeString(this.channel);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.comment_content);
        dest.writeLong(this.commentable_id);
        dest.writeLong(this.target_user);
        dest.writeLong(this.user_id);
        dest.writeLong(this.reply_user);
        dest.writeParcelable(this.fromUserInfoBean, flags);
        dest.writeParcelable(this.toUserInfoBean, flags);
        dest.writeInt(this.music_id);
        dest.writeInt(this.special_id);
        dest.writeValue(this.comment_mark);
        dest.writeInt(this.state);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 870552357)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMusicCommentListBeanDao() : null;
    }
    protected MusicCommentListBean(Parcel in) {
        super(in);
        this._id = (Long) in.readValue(Long.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.channel = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.comment_content = in.readString();
        this.commentable_id = in.readLong();
        this.target_user = in.readLong();
        this.user_id = in.readLong();
        this.reply_user = in.readLong();
        this.fromUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.toUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.music_id = in.readInt();
        this.special_id = in.readInt();
        this.comment_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.state = in.readInt();
    }

    public static final Creator<MusicCommentListBean> CREATOR = new Creator<MusicCommentListBean>() {
        @Override
        public MusicCommentListBean createFromParcel(Parcel source) {
            return new MusicCommentListBean(source);
        }

        @Override
        public MusicCommentListBean[] newArray(int size) {
            return new MusicCommentListBean[size];
        }
    };
}
