package com.haohaishengshi.haohaimusic.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBeanDao;

public class UserInfoBeanTest extends AbstractDaoTestLongPk<UserInfoBeanDao, UserInfoBean> {

    public UserInfoBeanTest() {
        super(UserInfoBeanDao.class);
    }

    @Override
    protected UserInfoBean createEntity(Long key) {
        UserInfoBean entity = new UserInfoBean();
        entity.setUser_id(key);
        entity.setSex();
        entity.setFollowing();
        entity.setFollower();
        entity.setLocal_update();
        entity.setFriends_count();
        entity.setInitial_password();
        entity.setHas_deleted();
        entity.setBlacked();
        return entity;
    }

}
