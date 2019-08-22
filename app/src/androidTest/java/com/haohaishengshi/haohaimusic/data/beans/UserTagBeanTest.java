package com.haohaishengshi.haohaimusic.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.haohaishengshi.haohaimusic.data.beans.UserTagBean;
import com.haohaishengshi.haohaimusic.data.beans.UserTagBeanDao;

public class UserTagBeanTest extends AbstractDaoTestLongPk<UserTagBeanDao, UserTagBean> {

    public UserTagBeanTest() {
        super(UserTagBeanDao.class);
    }

    @Override
    protected UserTagBean createEntity(Long key) {
        UserTagBean entity = new UserTagBean();
        entity.setId(key);
        entity.setTag_category_id();
        entity.setMine_has();
        entity.setWeight();
        return entity;
    }

}
