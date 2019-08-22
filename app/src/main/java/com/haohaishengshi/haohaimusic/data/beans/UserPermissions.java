package com.haohaishengshi.haohaimusic.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;

/**
 * ThinkSNS Plus
 * Copyright (c) 2018 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @author Jungle68
 * @describe  user  permissons list data , more to see {https://slimkit.github.io/plus/api-v2/user/}
 * @date 2018/11/26
 * @contact master.jungle68@gmail.com
 */
public class UserPermissions extends BaseListBean implements Serializable {

    private static final long serialVersionUID = 4274717917021187763L;
    /**
     *  {
     *         "name": "[feed] Delete Feed",
     *             "display_name": "[动态]->删除动态",
     *             "description": "删除动态权限"
     *     }
     */

    private String name;
    private String display_name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.display_name);
        dest.writeString(this.description);
    }

    public UserPermissions() {
    }

    protected UserPermissions(Parcel in) {
        super(in);
        this.name = in.readString();
        this.display_name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<UserPermissions> CREATOR = new Creator<UserPermissions>() {
        @Override
        public UserPermissions createFromParcel(Parcel source) {
            return new UserPermissions(source);
        }

        @Override
        public UserPermissions[] newArray(int size) {
            return new UserPermissions[size];
        }
    };
}
