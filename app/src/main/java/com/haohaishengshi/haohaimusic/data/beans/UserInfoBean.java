package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.integration.IntegrationBean;
import com.haohaishengshi.haohaimusic.data.source.local.data_convert.AvatarConvert;
import com.haohaishengshi.haohaimusic.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;


/**
 * @Describe 文档查阅 @see{ https://github.com/slimkit/thinksns-plus/blob/master/docs/api/v2/user/show.md
 * }
 * @Author Jungle68
 * @Date 2017/7/18
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class UserInfoBean extends BaseListBean implements Parcelable, Serializable {
    private static final long serialVersionUID = 536871008;

    /**
     * {
     * "id": 1, // 用户id
     * "name": "创始人", // 用户名
     * "phone": "187xxxxxxxx", // 用户手机号码
     * "email": "shiweidu@outlook.com" // 用户邮箱
     * "bio": "我是大管理员", // 用户简介
     * "sex": 0, // 用户性别，0 - 未知，1 - 男，2 - 女
     * "location": "成都市 四川省 中国", // 用户位置
     * "created_at": "2017-06-02 08:43:54",
     * "updated_at": "2017-07-06 07:04:06",
     * "avatar": "http://plus.io/api/v2/users/1/avatar", // 头像
     * "following": false,  基于这条消息的用户是否关注了我
     * "follower": false,   基于这条消息的用户是否被我关注了
     * "extra": {
     * "user_id": 1,
     * "likes_count": 0, // 被喜欢统计数
     * "comments_count": 0, // 用户发出的评论统计
     * "followers_count": 0, // 用户粉丝数
     * "followings_count": 1, // 用户关注数
     * "updated_at": "2017-07-16 09:44:25", // 更新时间
     * "feeds_count": 0 // 发布的动态统计，没有安装 动态应用则不存在
     * },
     * "wallet": {
     * "id": 1,
     * "user_id": 1,
     * "balance": 90, // 用户余额
     * "created_at": "2017-06-02 08:43:54",
     * "updated_at": "2017-07-05 08:29:49",
     * "deleted_at": null
     * }
     * }
     */
    // 定义四种性别状态
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int SECRET = 0;

    @Id
    @SerializedName(value = "user_id", alternate = {"id"})
    private Long user_id;
    private String name;
    @SerializedName(value = "phone", alternate = {"mobi"})
    private String phone;
    private String email;
    @SerializedName(value = "intro", alternate = {"bio"})
    private String intro;
    private int sex;            // 1 2 3  1男 2女 3其他
    @Transient
    private String sexString;   // sex编号对应的具体值，不保存到数据库中
    private String location;
    @Transient
    private String province;    // 省
    @Transient
    private String city;        // 城市
    @Transient
    private String area;        // 区
    /**
     * 基于这条消息的用户是否关注了我
     */
    private boolean following;
    private boolean follower;
    private String created_at;
    private String updated_at;
    private long local_update;
    private String deleted_at;
    @Convert(converter = AvatarConvert.class, columnType = String.class)
    private Avatar avatar;      // 头像 地址
    private String localAvatar;
    @SerializedName(value = "cover", alternate = {"bg"})
    @Convert(converter = AvatarConvert.class, columnType = String.class)
    private Avatar cover;// 封面
    @Transient
    @SerializedName("new_wallet")
    private WalletBean wallet;
    /**
     * 积分
     */
    @Convert(converter = IntegrationBeanConverter.class, columnType = String.class)
    private IntegrationBean currency;

    @Convert(converter = ExtraParamsConverter.class, columnType = String.class)
    private UserInfoExtraBean extra;
    @Convert(converter = VerifiedBeanConverter.class, columnType = String.class)
    private VerifiedBean verified;

    @Convert(converter = UserTagsBeanConverter.class, columnType = String.class)
    private List<UserTagBean> tags;

    /**
     * 1.5.0新增 环信登陆的密码  用户名是uid
     */
    private String im_pwd_hash;
    /**
     * 1.5.1新增 好友数量
     */
    private int friends_count;
    /**
     * 1.5.1新增 选中状态 用于选择好友列表 不存数据库
     * 1-选中 0-未选中 -1-不可修改（即已经选中，不可改变状态
     */
    @Transient
    private int isSelected;

    private boolean initial_password = true; // 在登陆信息中返回，用来判断是否需要设置密码，给个默认值true，false才需要设置

    private boolean has_deleted; // 标记用户是否被删除了，默认没有被删除

    /**
     * 是否是黑名单
     */
    private boolean blacked;

    public long getLocal_update() {
        return local_update;
    }

    public void setLocal_update(long local_update) {
        this.local_update = local_update;
    }

    public boolean isBlacked() {
        return blacked;
    }

    public boolean getBlacked() {
        return blacked;
    }

    public void setBlacked(boolean blacked) {
        this.blacked = blacked;
    }

    public boolean isHas_deleted() {
        return has_deleted;
    }

    public void setHas_deleted(boolean has_deleted) {
        this.has_deleted = has_deleted;
    }

    public boolean isInitial_password() {
        return initial_password;
    }

    public void setInitial_password(boolean initial_password) {
        this.initial_password = initial_password;
    }

    public List<UserTagBean> getTags() {
        return tags;
    }

    public void setTags(List<UserTagBean> tags) {
        this.tags = tags;
    }

    public String getSexString() {
        switch (getSex()) {
            case MALE:
                sexString = AppApplication.getContext().getString(R.string.male);
                break;
            case FEMALE:
                sexString = AppApplication.getContext().getString(R.string.female);
                break;
            case SECRET:
                sexString = AppApplication.getContext().getString(R.string.keep_secret);
                break;
            default:
                sexString = "";
                break;
        }
        return sexString;
    }

    public void setSexString(String sexString) {
        this.sexString = sexString;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        if (TextUtils.isEmpty(deleted_at)) {
            return name;
        } else {
            // 该用户已被删除
            return AppApplication.getContext().getResources().getString(R.string.default_delete_user_name);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntro() {
        if (TextUtils.isEmpty(intro)) {
            return AppApplication.getContext().getResources().getString(R.string.intro_default);
        } else {
            return intro;
        }
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getProvince() {
        if (TextUtils.isEmpty(province) && !TextUtils.isEmpty(location)) {
            String[] data = location.split(" ");
            if (data.length > 0) {
                province = data[0];
            }
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        if (TextUtils.isEmpty(city) && !TextUtils.isEmpty(location)) {
            String[] data = location.split(" ");
            if (data.length > 1) {
                city = data[1];
            }
        }

        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        if (TextUtils.isEmpty(area) && !TextUtils.isEmpty(location)) {
            String[] data = location.split(" ");
            if (data.length > 2) {
                area = data[2];
            }
        }
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getLocalAvatar() {
        return localAvatar;
    }

    public void setLocalAvatar(String localAvatar) {
        this.localAvatar = localAvatar;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Avatar getCover() {
        return cover;
    }

    public void setCover(Avatar cover) {
        this.cover = cover;
    }

    public WalletBean getWallet() {
        return wallet;
    }

    public void setWallet(WalletBean wallet) {
        this.wallet = wallet;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isFollower() {
        return follower;
    }

    public void setFollower(boolean follower) {
        this.follower = follower;
    }

    public String getIm_pwd_hash() {
        return im_pwd_hash;
    }

    public void setIm_pwd_hash(String im_pwd_hash) {
        this.im_pwd_hash = im_pwd_hash;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return 格式化后的用户积分
     */
    public long getFormatCurrencyNum() {

        return currency == null ? 0 : currency.getSum();
    }

    public IntegrationBean getCurrency() {
        return currency;
    }

    public void setCurrency(IntegrationBean currency) {
        this.currency = currency;
    }

    public UserInfoExtraBean getExtra() {
        if (extra == null) {
            extra = new UserInfoExtraBean();
        }
        return extra;
    }

    public void setExtra(UserInfoExtraBean extra) {
        this.extra = extra;
    }

    public UserInfoBean() {
    }

    public UserInfoBean(String name) {
        this.name = name;
    }
    public UserInfoBean(Long id) {
        this.user_id = id;
    }


    /**
     * {
     * "user_id": 1,
     * "likes_count": 0, // 被喜欢统计数
     * "comments_count": 0, // 用户发出的评论统计
     * "followers_count": 0, // 用户粉丝数
     * "followings_count": 1, // 用户关注数
     * "updated_at": "2017-07-16 09:44:25", // 更新时间
     * "feeds_count": 0 // 发布的动态统计，没有安装 动态应用则不存在
     * extra.count	int	浏览量
     * }
     */
    public static class UserInfoExtraBean implements Serializable, Parcelable {
        private static final long serialVersionUID = 8468324804308698269L;
        private Long user_id;
        private int likes_count;
        private int comments_count;
        private int followers_count;
        private int followings_count;
        private int questions_count;
        private int answers_count;
        private int feeds_count;
        private int count; // 排行数量 如粉丝排行榜即为粉丝数 根据点赞数来排行的 则为点赞的数量
        private int rank;
        private int checkin_count;
        private int last_checkin_count;
        private String updated_at;

        public UserInfoExtraBean() {
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public int getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(int followers_count) {
            this.followers_count = followers_count;
        }

        public int getFollowings_count() {
            return followings_count;
        }

        public void setFollowings_count(int followings_count) {
            this.followings_count = followings_count;
        }

        public int getFeeds_count() {
            return feeds_count;
        }

        public void setFeeds_count(int feeds_count) {
            this.feeds_count = feeds_count;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public int getQuestions_count() {
            return questions_count;
        }

        public void setQuestions_count(int questions_count) {
            this.questions_count = questions_count;
        }

        public int getAnswers_count() {
            return answers_count;
        }

        public void setAnswers_count(int answers_count) {
            this.answers_count = answers_count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getCheckin_count() {
            return checkin_count;
        }

        public void setCheckin_count(int checkin_count) {
            this.checkin_count = checkin_count;
        }

        public int getLast_checkin_count() {
            return last_checkin_count;
        }

        public void setLast_checkin_count(int last_checkin_count) {
            this.last_checkin_count = last_checkin_count;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.user_id);
            dest.writeInt(this.likes_count);
            dest.writeInt(this.comments_count);
            dest.writeInt(this.followers_count);
            dest.writeInt(this.followings_count);
            dest.writeInt(this.questions_count);
            dest.writeInt(this.answers_count);
            dest.writeInt(this.feeds_count);
            dest.writeInt(this.count);
            dest.writeInt(this.rank);
            dest.writeInt(this.checkin_count);
            dest.writeInt(this.last_checkin_count);
            dest.writeString(this.updated_at);
        }

        protected UserInfoExtraBean(Parcel in) {
            this.user_id = (Long) in.readValue(Long.class.getClassLoader());
            this.likes_count = in.readInt();
            this.comments_count = in.readInt();
            this.followers_count = in.readInt();
            this.followings_count = in.readInt();
            this.questions_count = in.readInt();
            this.answers_count = in.readInt();
            this.feeds_count = in.readInt();
            this.count = in.readInt();
            this.rank = in.readInt();
            this.checkin_count = in.readInt();
            this.last_checkin_count = in.readInt();
            this.updated_at = in.readString();
        }

        public static final Creator<UserInfoExtraBean> CREATOR = new Creator<UserInfoExtraBean>() {
            @Override
            public UserInfoExtraBean createFromParcel(Parcel source) {
                return new UserInfoExtraBean(source);
            }

            @Override
            public UserInfoExtraBean[] newArray(int size) {
                return new UserInfoExtraBean[size];
            }
        };

        @Override
        public String toString() {
            return "UserInfoExtraBean{" +
                    "user_id=" + user_id +
                    ", likes_count=" + likes_count +
                    ", comments_count=" + comments_count +
                    ", followers_count=" + followers_count +
                    ", followings_count=" + followings_count +
                    ", questions_count=" + questions_count +
                    ", answers_count=" + answers_count +
                    ", feeds_count=" + feeds_count +
                    ", count=" + count +
                    ", rank=" + rank +
                    ", checkin_count=" + checkin_count +
                    ", last_checkin_count=" + last_checkin_count +
                    ", updated_at='" + updated_at + '\'' +
                    '}';
        }
    }


    /**
     * UserInfoExtraBean 转 String 形式存入数据库
     */
    public static class ExtraParamsConverter extends BaseConvert<UserInfoExtraBean> {

    }

    /**
     * VerifiedBean 转 String 形式存入数据库
     */
    public static class VerifiedBeanConverter extends BaseConvert<VerifiedBean> {

    }

    /**
     * UsertagBean 转 String 形式存入数据库
     */
    public static class UserTagsBeanConverter extends BaseConvert<List<UserTagBean>> {

    }

    /**
     * IntegrationBean 转 String 形式存入数据库
     */
    public static class IntegrationBeanConverter extends BaseConvert<IntegrationBean> {

    }

    @Override
    public Long getMaxId() {
        return user_id;
    }

    public boolean getFollowing() {
        return this.following;
    }

    public boolean getFollower() {
        return this.follower;
    }

    public VerifiedBean getVerified() {
        return verified;
    }

    public void setVerified(VerifiedBean verified) {
        this.verified = verified;
    }

    public boolean getInitial_password() {
        return this.initial_password;
    }

    public boolean getHas_deleted() {
        return this.has_deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserInfoBean that = (UserInfoBean) o;

        if (user_id != null ? !user_id.equals(that.user_id) : that.user_id != null) {
            return false;
        }
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = user_id != null ? user_id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


    @Generated(hash = 483732844)
    public UserInfoBean(Long user_id, String name, String phone, String email, String intro, int sex,
                        String location, boolean following, boolean follower, String created_at, String updated_at,
                        long local_update, String deleted_at, Avatar avatar, String localAvatar, Avatar cover,
                        IntegrationBean currency, UserInfoExtraBean extra, VerifiedBean verified, List<UserTagBean> tags,
                        String im_pwd_hash, int friends_count, boolean initial_password, boolean has_deleted,
                        boolean blacked) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.intro = intro;
        this.sex = sex;
        this.location = location;
        this.following = following;
        this.follower = follower;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.local_update = local_update;
        this.deleted_at = deleted_at;
        this.avatar = avatar;
        this.localAvatar = localAvatar;
        this.cover = cover;
        this.currency = currency;
        this.extra = extra;
        this.verified = verified;
        this.tags = tags;
        this.im_pwd_hash = im_pwd_hash;
        this.friends_count = friends_count;
        this.initial_password = initial_password;
        this.has_deleted = has_deleted;
        this.blacked = blacked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.user_id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.intro);
        dest.writeInt(this.sex);
        dest.writeString(this.sexString);
        dest.writeString(this.location);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeByte(this.following ? (byte) 1 : (byte) 0);
        dest.writeByte(this.follower ? (byte) 1 : (byte) 0);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeLong(this.local_update);
        dest.writeString(this.deleted_at);
        dest.writeParcelable(this.avatar, flags);
        dest.writeString(this.localAvatar);
        dest.writeParcelable(this.cover, flags);
        dest.writeParcelable(this.wallet, flags);
        dest.writeSerializable(this.currency);
        dest.writeParcelable(this.extra, flags);
        dest.writeParcelable(this.verified, flags);
        dest.writeTypedList(this.tags);
        dest.writeString(this.im_pwd_hash);
        dest.writeInt(this.friends_count);
        dest.writeInt(this.isSelected);
        dest.writeByte(this.initial_password ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_deleted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.blacked ? (byte) 1 : (byte) 0);
    }

    protected UserInfoBean(Parcel in) {
        super(in);
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.intro = in.readString();
        this.sex = in.readInt();
        this.sexString = in.readString();
        this.location = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.following = in.readByte() != 0;
        this.follower = in.readByte() != 0;
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.local_update = in.readLong();
        this.deleted_at = in.readString();
        this.avatar = in.readParcelable(Avatar.class.getClassLoader());
        this.localAvatar = in.readString();
        this.cover = in.readParcelable(Avatar.class.getClassLoader());
        this.wallet = in.readParcelable(WalletBean.class.getClassLoader());
        this.currency = (IntegrationBean) in.readSerializable();
        this.extra = in.readParcelable(UserInfoExtraBean.class.getClassLoader());
        this.verified = in.readParcelable(VerifiedBean.class.getClassLoader());
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
        this.im_pwd_hash = in.readString();
        this.friends_count = in.readInt();
        this.isSelected = in.readInt();
        this.initial_password = in.readByte() != 0;
        this.has_deleted = in.readByte() != 0;
        this.blacked = in.readByte() != 0;
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
