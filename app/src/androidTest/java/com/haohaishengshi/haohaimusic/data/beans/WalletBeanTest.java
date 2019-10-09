package com.haohaishengshi.haohaimusic.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class WalletBeanTest extends AbstractDaoTestLongPk<WalletBeanDao, WalletBean> {

    public WalletBeanTest() {
        super(WalletBeanDao.class);
    }

    @Override
    protected WalletBean createEntity(Long key) {
        WalletBean entity = new WalletBean();
        entity.setId(key);
        entity.setUser_id();
        entity.setBalance();
        entity.setTotal_income();
        entity.setTotal_expenses();
        return entity;
    }

}
