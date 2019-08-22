package com.haohaishengshi.haohaimusic.data.source.local;

import android.app.Application;

import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBeanDao;
import com.haohaishengshi.haohaimusic.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.baseproject.base.TSListFragment;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicAlbumListBeanGreenDaoImpl extends CommonCacheImpl<MusicAlbumListBean> {


    @Inject
    public MusicAlbumListBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(MusicAlbumListBean singleData) {
        return getWDaoSession().getMusicAlbumListBeanDao().insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<MusicAlbumListBean> multiData) {
        getWDaoSession().getMusicAlbumListBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicAlbumListBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getMusicAlbumListBeanDao().load(primaryKey);
    }

    @Override
    public List<MusicAlbumListBean> getMultiDataFromCache() {
        return getRDaoSession().getMusicAlbumListBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getMusicAlbumListBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getMusicAlbumListBeanDao().deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(MusicAlbumListBean dta) {
        getWDaoSession().getMusicAlbumListBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(MusicAlbumListBean newData) {
        if (newData == null) {
            return;
        }
        getWDaoSession().getMusicAlbumListBeanDao().insertOrReplace(newData);
    }

    @Override
    public long insertOrReplace(MusicAlbumListBean newData) {
        if (newData == null) {
            return -1;
        }
        return getWDaoSession().getMusicAlbumListBeanDao().insertOrReplace(newData);
    }

    public List<MusicAlbumListBean> getMyCollectAlbum() {
        MusicAlbumListBeanDao musicAlbumListBeanDao = getRDaoSession().getMusicAlbumListBeanDao();
        QueryBuilder queryBuilder = musicAlbumListBeanDao.queryBuilder();
        queryBuilder.where(MusicAlbumListBeanDao.Properties.Has_collect.eq(true))// 已收藏
                .limit(TSListFragment.DEFAULT_PAGE_DB_SIZE)// 每次取20条
                .orderDesc(MusicAlbumListBeanDao.Properties.Id);// 专辑id倒序
        return queryBuilder.list();
    }
}
