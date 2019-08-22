package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.common.config.ConstantConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @Describe 用户认证信息
 * @Author Jungle68
 * @Date 2017/7/19
 * @Contact master.jungle68@gmail.com
 */

public class AuthBean extends CacheBean implements Parcelable, Serializable {
    public static final long serialVersionUID = 536871008L;
    private static final long REQUEST_TIME_OFFSET = 10_000;
    /**
     * {
     * "token":
     * "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9wbHVzLmlvL2FwaS92Mi90b2tlbnMiLCJpYXQiOjE1MDAzNjU5MzQsImV4cCI6MTUwMTU3NTUzNCwibmJmIjoxNTAwMzY1OTM0LCJqdGkiOiJ1aXlvdTQwNnJsdU9pa3l3In0.OTM4mbH3QW7busunRsFUsheE5vysuIfrBrwjWnd0J6k",
     * "user": {
     * "id": 1,
     * "name": "创始人",
     * "bio": "我是大管理员",
     * "sex": 0,
     * "location": "成都市 四川省 中国",
     * "created_at": "2017-06-02 08:43:54",
     * "updated_at": "2017-07-06 07:04:06",
     * "avatar": "http://plus.io/api/v2/users/1/avatar",
     * "extra": {
     * "user_id": 1,
     * "likes_count": 0,
     * "comments_count": 0,
     * "followers_count": 0,
     * "followings_count": 1,
     * "updated_at": "2017-07-16 09:44:25",
     * "feeds_count": 0
     * }
     * },
     * "ttl": 20160, 用户token的有效期(单位:分)
     * "refresh_ttl": 40320
     * access_token	授权 Token
     * token_type	Token 类型
     * expires_in	过期时间，单位秒
     * <p>
     * }
     */
    @SerializedName(value = "token", alternate = {"access_token"})
    private String token;
    @SerializedName(value = "refresh_token", alternate = {"refresh_ttl"})
    private long refresh_token; // 刷新 token 过期时间 间隔时间(单位:分钟)
    @SerializedName(value = "ttl", alternate = {"expires_in"})
    private long expires;// 用户 token 的有效期(单位:分钟) 过期时间 ，间隔时间
    private long user_id;
    private UserInfoBean user;
    private String token_type;
    private long token_request_time; // 请求 token 的当前时间
    private List<UserPermissions> mUserPermissions; // 用户权限列表
    private List<String> userPermissionIds =new ArrayList<>(); // 用户权限唯一标识列表


    public List<String> getUserPermissionIds() {
        return userPermissionIds;
    }

    public void setUserPermissionIds(List<String> userPermissionIds) {
        this.userPermissionIds = userPermissionIds;
    }

    public List<UserPermissions> getUserPermissions() {
        return mUserPermissions;
    }

    public void setUserPermissions(List<UserPermissions> userPermissions) {
        this.mUserPermissions = userPermissions;
        userPermissionIds.clear();
        for (UserPermissions userPermisson : userPermissions) {
            userPermissionIds.add(userPermisson.getName());
        }
    }

    public long getToken_request_time() {
        return token_request_time;
    }

    public void setToken_request_time(long token_request_time) {
        this.token_request_time = token_request_time;
    }

    public long getUser_id() {
        if (user != null) {
            return user.getUser_id();
        } else {
            return user_id;
        }
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(long refresh_token) {
        this.refresh_token = refresh_token;
    }

    public boolean getRefresh_token_is_expired() {
        if (System.currentTimeMillis() - token_request_time >= refresh_token * ConstantConfig.MIN) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getToken_is_expired() {
        if (System.currentTimeMillis() - token_request_time >= expires * ConstantConfig.MIN) {
            return true;
        } else {
            return false;
        }
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public AuthBean() {
    }

    public AuthBean(long user_id) {
        this.user_id = user_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeLong(this.refresh_token);
        dest.writeLong(this.expires);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.token_type);
        dest.writeLong(this.token_request_time);
        dest.writeTypedList(this.mUserPermissions);
        dest.writeStringList(this.userPermissionIds);
    }

    protected AuthBean(Parcel in) {
        this.token = in.readString();
        this.refresh_token = in.readLong();
        this.expires = in.readLong();
        this.user_id = in.readLong();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.token_type = in.readString();
        this.token_request_time = in.readLong();
        this.mUserPermissions = in.createTypedArrayList(UserPermissions.CREATOR);
        this.userPermissionIds = in.createStringArrayList();
    }

    public static final Creator<AuthBean> CREATOR = new Creator<AuthBean>() {
        @Override
        public AuthBean createFromParcel(Parcel source) {
            return new AuthBean(source);
        }

        @Override
        public AuthBean[] newArray(int size) {
            return new AuthBean[size];
        }
    };
}
