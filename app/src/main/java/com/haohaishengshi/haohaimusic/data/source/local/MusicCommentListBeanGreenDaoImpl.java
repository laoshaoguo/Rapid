package com.haohaishengshi.haohaimusic.data.source.local;

import android.app.Application;

import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.MusicCommentListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicCommentListBeanDao;
import com.haohaishengshi.haohaimusic.data.source.local.db.CommonCacheImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/04/04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentListBeanGreenDaoImpl extends CommonCacheImpl<MusicCommentListBean> {

    @Inject
    public MusicCommentListBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(MusicCommentListBean singleData) {
        return getWDaoSession().getMusicCommentListBeanDao().insertOrReplace(singleData);
    }

    @Override
    public void saveMultiData(List<MusicCommentListBean> multiData) {
        getWDaoSession().getMusicCommentListBeanDao().insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public MusicCommentListBean getSingleDataFromCache(Long primaryKey) {
        return getRDaoSession().getMusicCommentListBeanDao().load(primaryKey);
    }

    @Override
    public List<MusicCommentListBean> getMultiDataFromCache() {
        return getRDaoSession().getMusicCommentListBeanDao().loadAll();
    }

    @Override
    public void clearTable() {
        getWDaoSession().getMusicCommentListBeanDao().deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        getWDaoSession().getMusicCommentListBeanDao().deleteByKey(primaryKey);
    }

    public List<MusicCommentListBean> getMyMusicComment(int music_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }

        return getRDaoSession().getMusicCommentListBeanDao().queryBuilder().where(MusicCommentListBeanDao.Properties.Music_id.eq(music_id)
                , MusicCommentListBeanDao.Properties.User_id.eq
                        (AppApplication.getmCurrentLoginAuth().getUser_id())).build().list();
    }

    public List<MusicCommentListBean> getLocalMusicComment(int music_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        return getRDaoSession().getMusicCommentListBeanDao().queryBuilder().where(MusicCommentListBeanDao.Properties.Music_id.eq(music_id)).build().list();
    }

    public List<MusicCommentListBean> getLocalAblumComment(int special_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        return getRDaoSession().getMusicCommentListBeanDao().queryBuilder().where(MusicCommentListBeanDao.Properties.Special_id.eq(special_id)).build().list();
    }

    public List<MusicCommentListBean> getMyAblumComment(int special_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }

        return getRDaoSession().getMusicCommentListBeanDao().queryBuilder().where(MusicCommentListBeanDao.Properties.Special_id.eq(special_id)
                , MusicCommentListBeanDao.Properties.User_id.eq
                        (AppApplication.getmCurrentLoginAuth().getUser_id())).build().list();
    }

    public MusicCommentListBean getMusicCommentByCommentMark(long mark) {
        return getRDaoSession().getMusicCommentListBeanDao().queryBuilder()
                .where(MusicCommentListBeanDao.Properties.Comment_mark.eq(mark)).build().list().get(0);
    }

    @Override
    public void deleteSingleCache(MusicCommentListBean dta) {
        getWDaoSession().getMusicCommentListBeanDao().delete(dta);
    }

    @Override
    public void updateSingleData(MusicCommentListBean newData) {
        getWDaoSession().getMusicCommentListBeanDao().insertOrReplace(newData);
    }

    @Override
    public long insertOrReplace(MusicCommentListBean newData) {
        return getWDaoSession().getMusicCommentListBeanDao().insertOrReplace(newData);
    }

    public List<MusicCommentListBean> getAblumCommentsCacheDataByType(String channel, long id) {
        return getRDaoSession().getMusicCommentListBeanDao().queryDeep(" where "
                        + " T." + MusicCommentListBeanDao.Properties.Channel.columnName + "= ?" + " AND "
                        + " T." + MusicCommentListBeanDao.Properties.Id.columnName + "= ?"
                        +"< ? order by"+" T." + MusicCommentListBeanDao.Properties.Id.columnName + " DESC"
                , new String[] {channel,String.valueOf(id),System.currentTimeMillis() + ""});
    }
}
