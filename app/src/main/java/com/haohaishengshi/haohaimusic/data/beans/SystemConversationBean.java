package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.TimeUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class SystemConversationBean extends BaseListBean {
    public static final long DEFAULT_TSHELPER_SYSTEMCONVERSATION_ID = -1;
    /**
     * id : 2
     * type : system
     * user_id : 1
     * to_user_id : 0
     * content : 123123
     * options : null
     * created_at : 2017-03-02 08:14:13
     * updated_at : 2017-03-02 08:14:13
     */

    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private Long system_mark;  //标记状态
    @Unique
    private Long id;                  //  数据 id
    private String type;            // 会话类型 system-系统通知 feedback-用户意见反馈
    private Long user_id;             // 发送者 id 系统通知时为 0
    @ToOne(joinProperty = "user_id")
    private UserInfoBean userInfo;
    private Long to_user_id;             // 接收者 id 系统广播通知及意见反馈时为 0
    @ToOne(joinProperty = "to_user_id")
    private UserInfoBean toUserInfo;
    private String content;             // 内容
    private String options;             //  系统通知额外扩展参数
    private String created_at;
    private Long created_time;
    private String updated_at;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1970441704)
    private transient SystemConversationBeanDao myDao;

    @Generated(hash = 873800144)
    public SystemConversationBean(Long _id, Long system_mark, Long id, String type,
                                  Long user_id, Long to_user_id, String content, String options,
                                  String created_at, Long created_time, String updated_at) {
        this._id = _id;
        this.system_mark = system_mark;
        this.id = id;
        this.type = type;
        this.user_id = user_id;
        this.to_user_id = to_user_id;
        this.content = content;
        this.options = options;
        this.created_at = created_at;
        this.created_time = created_time;
        this.updated_at = updated_at;
    }

    @Generated(hash = 2093826292)
    public SystemConversationBean() {
    }


    @Generated(hash = 2066097151)
    private transient Long userInfo__resolvedKey;
    @Generated(hash = 815130429)
    private transient Long toUserInfo__resolvedKey;

    @Override
    public String toString() {
        return "SystemConversationBean{" +
                "_id=" + _id +
                ", system_mark=" + system_mark +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", user_id=" + user_id +
                ", userInfo=" + userInfo +
                ", to_user_id=" + to_user_id +
                ", toUserInfo=" + toUserInfo +
                ", content='" + content + '\'' +
                ", options='" + options + '\'' +
                ", created_at='" + created_at + '\'' +
                ", created_time=" + created_time +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    @Override
    public Long getMaxId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this._id);
        dest.writeValue(this.system_mark);
        dest.writeValue(this.id);
        dest.writeString(this.type);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeValue(this.to_user_id);
        dest.writeParcelable(this.toUserInfo, flags);
        dest.writeString(this.content);
        dest.writeString(this.options);
        dest.writeString(this.created_at);
        dest.writeLong(this.created_time);
        dest.writeString(this.updated_at);
    }


    public Long get_id() {
        return this._id;
    }


    public void set_id(Long _id) {
        this._id = _id;
    }


    public Long getSystem_mark() {
        return this.system_mark;
    }


    public void setSystem_mark(Long system_mark) {
        this.system_mark = system_mark;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getType() {
        return this.type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Long getUser_id() {
        return this.user_id;
    }


    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }


    public Long getTo_user_id() {
        return this.to_user_id;
    }


    public void setTo_user_id(Long to_user_id) {
        this.to_user_id = to_user_id;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getOptions() {
        return this.options;
    }


    public void setOptions(String options) {
        this.options = options;
    }


    public String getCreated_at() {
        return this.created_at;
    }


    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getCreated_time() {
        return TimeUtils.utc2LocalLong(created_at);
    }

    public void setCreated_time(Long created_time) {
        this.created_time = created_time;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }


    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1544759941)
    public UserInfoBean getUserInfo() {
        Long __key = this.user_id;
        if (userInfo__resolvedKey == null || !userInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userInfoNew = targetDao.load(__key);
            synchronized (this) {
                userInfo = userInfoNew;
                userInfo__resolvedKey = __key;
            }
        }
        return userInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1576466957)
    public void setUserInfo(UserInfoBean userInfo) {
        synchronized (this) {
            this.userInfo = userInfo;
            user_id = userInfo == null ? null : userInfo.getUser_id();
            userInfo__resolvedKey = user_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1921703876)
    public UserInfoBean getToUserInfo() {
        Long __key = this.to_user_id;
        if (toUserInfo__resolvedKey == null
                || !toUserInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean toUserInfoNew = targetDao.load(__key);
            synchronized (this) {
                toUserInfo = toUserInfoNew;
                toUserInfo__resolvedKey = __key;
            }
        }
        return toUserInfo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1067491086)
    public void setToUserInfo(UserInfoBean toUserInfo) {
        synchronized (this) {
            this.toUserInfo = toUserInfo;
            to_user_id = toUserInfo == null ? null : toUserInfo.getUser_id();
            toUserInfo__resolvedKey = to_user_id;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1997120082)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSystemConversationBeanDao() : null;
    }


}
