package com.haohaishengshi.haohaimusic.data.source.local.db;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.DaoMaster;
import com.haohaishengshi.haohaimusic.data.beans.DaoSession;
import com.zhiyicx.baseproject.cache.IDataBaseOperate;
import com.zhiyicx.baseproject.config.DBConfig;

/**
 * @author LiuChao
 * @describe
 * @date 2016/12/30
 * @contact email:450127106@qq.com
 */

public abstract class CommonCacheImpl<T> implements IDataBaseOperate<T> {
    @SuppressLint("StaticFieldLeak")
    private static final UpDBHelper sUpDBHelper = new UpDBHelper(AppApplication.getContext(), DBConfig.DB_NAME);

    public CommonCacheImpl(Application context) {
    }

    /**
     * 获取可读数据库
     */
    protected SQLiteDatabase getReadableDatabase() {
        return sUpDBHelper.getReadableDatabase();

    }

    /**
     * 获取可写数据库
     */
    protected SQLiteDatabase getWritableDatabase() {
        return sUpDBHelper.getWritableDatabase();
    }

    /**
     * 获取可写数据库的DaoMaster
     */
    protected DaoMaster getWDaoMaster() {
        return new DaoMaster(getWritableDatabase());
    }

    /**
     * 获取可写数据库的DaoSession
     */
    protected DaoSession getWDaoSession() {
        return getWDaoMaster().newSession();
    }

    /**
     * 获取可写数据库的DaoMaster
     */
    protected DaoMaster getRDaoMaster() {
        return new DaoMaster(getWritableDatabase());
    }

    /**
     * 获取可写数据库的DaoSession
     */
    protected DaoSession getRDaoSession() {
        return getRDaoMaster().newSession();
    }
}
