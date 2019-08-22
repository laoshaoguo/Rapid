package com.haohaishengshi.haohaimusic.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBeanDao;

public class MusicAlbumListBeanTest extends AbstractDaoTestLongPk<MusicAlbumListBeanDao, MusicAlbumListBean> {

    public MusicAlbumListBeanTest() {
        super(MusicAlbumListBeanDao.class);
    }

    @Override
    protected MusicAlbumListBean createEntity(Long key) {
        MusicAlbumListBean entity = new MusicAlbumListBean();
        entity.set_id(key);
        entity.setId();
        entity.setTaste_count();
        entity.setShare_count();
        entity.setComment_count();
        entity.setCollect_count();
        entity.setHas_collect();
        return entity;
    }

}
