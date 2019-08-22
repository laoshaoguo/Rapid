package com.haohaishengshi.haohaimusic.data.beans;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBeanDao;

public class MusicAlbumDetailsBeanTest extends AbstractDaoTestLongPk<MusicAlbumDetailsBeanDao, MusicAlbumDetailsBean> {

    public MusicAlbumDetailsBeanTest() {
        super(MusicAlbumDetailsBeanDao.class);
    }

    @Override
    protected MusicAlbumDetailsBean createEntity(Long key) {
        MusicAlbumDetailsBean entity = new MusicAlbumDetailsBean();
        entity.setId(key);
        entity.setTaste_count();
        entity.setShare_count();
        entity.setComment_count();
        entity.setCollect_count();
        entity.setHas_collect();
        return entity;
    }

}
