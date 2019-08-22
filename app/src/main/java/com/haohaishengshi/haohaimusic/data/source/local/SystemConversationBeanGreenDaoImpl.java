package com.haohaishengshi.haohaimusic.data.source.local;

import android.app.Application;

import com.haohaishengshi.haohaimusic.data.beans.SystemConversationBean;
import com.haohaishengshi.haohaimusic.data.beans.SystemConversationBeanDao;
import com.haohaishengshi.haohaimusic.data.source.local.db.CommonCacheImpl;
import com.zhiyicx.baseproject.base.TSListFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe (意见反馈 + 系统公告) 表数据库
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationBeanGreenDaoImpl extends CommonCacheImpl<SystemConversationBean> {

    @Inject
    public SystemConversationBeanGreenDaoImpl(Application context) {
        super(context);
    }

    @Override
    public long saveSingleData(SystemConversationBean singleData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.insert(singleData);
    }

    @Override
    public void saveMultiData(List<SystemConversationBean> multiData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.insertOrReplaceInTx(multiData);
    }

    @Override
    public boolean isInvalide() {
        return false;
    }

    @Override
    public SystemConversationBean getSingleDataFromCache(Long primaryKey) {
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.load(primaryKey);
    }

    @Override
    public List<SystemConversationBean> getMultiDataFromCache() {
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.queryDeep(" where "
                        + " T." + SystemConversationBeanDao.Properties._id.columnName + " < ? "
                        + " order by " + " T." + SystemConversationBeanDao.Properties._id.columnName + " DESC"// 按频道id倒序
                , System.currentTimeMillis() + "");
    }

    /**
     * 通过 max_id 获取分页数据
     *
     * @param max_id
     * @return
     */
    public List<SystemConversationBean> getMultiDataFromCacheByMaxId(long max_id) {
        if (max_id <= 0) {
            max_id = System.currentTimeMillis();
        }
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.queryDeep(" where "
                        + " T." + SystemConversationBeanDao.Properties.Created_time.columnName + " < ? "
                        + " order by " + " T." + SystemConversationBeanDao.Properties.Created_time.columnName + " ASC LIMIT " + TSListFragment.DEFAULT_PAGE_DB_SIZE// 按频道id倒序
                , max_id + "");
    }

    /**
     * 通过 max_id 获取分页数据
     *
     * @return
     */
    public SystemConversationBean getLastData() {
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        List<SystemConversationBean> datas = systemConversationBeanDao.queryDeep(" where "
                        + " T." + SystemConversationBeanDao.Properties._id.columnName + " < ? "
                        + " order by " + " T." + SystemConversationBeanDao.Properties._id.columnName + " DESC LIMIT " + TSListFragment.DEFAULT_PAGE_DB_SIZE// 按频道id倒序
                , System.currentTimeMillis() + "");
        if (datas.isEmpty()) {
            return null;
        }
        return datas.get(0);
    }

    /**
     * 通过 max_id 获取分页数据
     *
     * @return
     */
    public SystemConversationBean getSystemConversationById(Long id) {
        SystemConversationBeanDao systemConversationBeanDao = getRDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.queryBuilder()
                .where(SystemConversationBeanDao.Properties.Id.eq(id))
                .unique();

    }

    @Override
    public void clearTable() {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.deleteAll();
    }

    @Override
    public void deleteSingleCache(Long primaryKey) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.deleteByKey(primaryKey);
    }

    @Override
    public void deleteSingleCache(SystemConversationBean dta) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.delete(dta);
    }

    @Override
    public void updateSingleData(SystemConversationBean newData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        systemConversationBeanDao.update(newData);
    }

    @Override
    public long insertOrReplace(SystemConversationBean newData) {
        SystemConversationBeanDao systemConversationBeanDao = getWDaoSession().getSystemConversationBeanDao();
        return systemConversationBeanDao.insertOrReplace(newData);
    }

}
