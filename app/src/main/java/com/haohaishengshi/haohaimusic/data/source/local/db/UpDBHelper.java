package com.haohaishengshi.haohaimusic.data.source.local.db;

import android.content.Context;

import com.haohaishengshi.haohaimusic.data.beans.DaoMaster;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBeanDao;
import com.zhiyicx.common.utils.log.LogUtils;

import org.greenrobot.greendao.database.Database;

/**
 * @author LiuChao
 * @describe 数据库升级
 * @date 2017/2/18
 * @contact email:450127106@qq.com
 */

public class UpDBHelper extends DaoMaster.OpenHelper {

    public UpDBHelper(Context context, String name) {
        super(context, name);
    }

    // 注意选择GreenDao参数的onUpgrade方法
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtils.i("greenDAO",
                "Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");
        // 每次升级，将需要更新的表进行更新，第二个参数为要升级的Dao文件.
        MigrationHelper.getInstance().migrate(db, UserInfoBeanDao.class);
    }
}
