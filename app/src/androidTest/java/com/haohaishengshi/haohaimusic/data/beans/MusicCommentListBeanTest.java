package com.haohaishengshi.haohaimusic.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class MusicCommentListBeanTest extends AbstractDaoTestLongPk<MusicCommentListBeanDao, MusicCommentListBean> {

    public MusicCommentListBeanTest() {
        super(MusicCommentListBeanDao.class);
    }

    @Override
    protected MusicCommentListBean createEntity(Long key) {
        MusicCommentListBean entity = new MusicCommentListBean();
        entity.set_id(key);
        entity.setCommentable_id();
        entity.setTarget_user();
        entity.setUser_id();
        entity.setReply_user();
        entity.setMusic_id();
        entity.setSpecial_id();
        entity.setState();
        return entity;
    }

}
