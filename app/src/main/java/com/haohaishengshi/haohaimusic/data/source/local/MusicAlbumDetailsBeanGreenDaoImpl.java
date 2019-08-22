package com.haohaishengshi.haohaimusic.data.source.local;

import android.app.Application;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBeanDao;
import com.haohaishengshi.haohaimusic.data.source.local.db.CommonCacheImpl;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MusicAlbumDetailsBeanGreenDaoImpl extends CommonCacheImpl<MusicAlbumDetailsBean> {

    @Inject
    public MusicAlbumDetailsBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(MusicAlbumDetailsBean singleData) {
        return getWDaoSession().getMusicAlbumDetailsBeanDao().insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<MusicAlbumDetailsBean> multiData) {
        getWDaoSession().getMusicAlbumDetailsBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicAlbumDetailsBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getMusicAlbumDetailsBeanDao().load(primaryKey);
    }

    @Override
    public List<MusicAlbumDetailsBean> getMultiDataFromCache() {
        return getRDaoSession().getMusicAlbumDetailsBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getMusicAlbumDetailsBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getMusicAlbumDetailsBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(MusicAlbumDetailsBean dta) {
        getWDaoSession().getMusicAlbumDetailsBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(MusicAlbumDetailsBean newData) {
        getWDaoSession().getMusicAlbumDetailsBeanDao().update(newData);
    }

    @Override
    public long insertOrReplace(MusicAlbumDetailsBean newData) {
        return getWDaoSession().getMusicAlbumDetailsBeanDao().insertOrReplace(newData);
    }

    public MusicAlbumDetailsBean getAblumByID(int id) {
        List<MusicAlbumDetailsBean> data = getRDaoSession().getMusicAlbumDetailsBeanDao().queryBuilder().where(MusicAlbumDetailsBeanDao.Properties.Id.eq(id)).build().list();
        if (!data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }
}
