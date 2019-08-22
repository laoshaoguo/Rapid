package com.haohaishengshi.haohaimusic.data.source.local.db;

import android.database.Cursor;
import android.text.TextUtils;

import com.haohaishengshi.haohaimusic.data.beans.DaoMaster;
import com.zhiyicx.common.utils.log.LogUtils;




import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiuChao
 * @describe 数据库升级方法
 * @date 2017/2/18
 * @contact email:450127106@qq.com
 */

public class MigrationHelper {

    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION =
            "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";
    private static MigrationHelper instance;

    public static MigrationHelper getInstance() {
        if (instance == null) {
            instance = new MigrationHelper();
        }
        return instance;
    }

    public void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        generateTempTables(db, daoClasses);// 生成临时表，保存旧数据
        DaoMaster.dropAllTables(db, true);// 删除旧的表
        DaoMaster.createAllTables(db, false);// 生成新版本的表
        restoreData(db, daoClasses);// 从临时表中将数据迁移到新版本的表中
    }

    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String divider = "";
            String tableName = daoConfig.tablename;// 旧表名
            String tempTableName = daoConfig.tablename.concat("_TEMP");// 临时表名
            ArrayList<String> properties = new ArrayList<>();

            StringBuilder createTableStringBuilder = new StringBuilder();

            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");
            // 根据旧表生成临时表的sql语句
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);

                    String type = null;

                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);

                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }

                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");
            // 执行sql语句，创建临时表
            db.execSQL(createTableStringBuilder.toString());
            // 生成将旧表数据迁移到临时表的sql语句
            String insertTableStringBuilder = "INSERT INTO " + tempTableName + " (" +
                    TextUtils.join(",", properties) +
                    ") SELECT " +
                    TextUtils.join(",", properties) +
                    " FROM " + tableName + ";";

            // 迁移旧表数据
            db.execSQL(insertTableStringBuilder);
        }
    }

    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();
            // 临时表的数据迁移到新的表中
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

            // 删除临时表
            try {
                db.execSQL(insertTableStringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            db.execSQL("DROP TABLE " + tempTableName);
        }
    }

    // 通过类类型判断sql语句的数据格式
    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(float.class) || type.equals(Float.class) || type.equals(Double.class) || type.equals(double.class)) {
            return "REAL";
        }
        if (type.equals(Boolean.class)) {
            return "BOOLEAN";
        }
        throw new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
    }

    // 每次从数据库最多取出一条数据
    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            LogUtils.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return columns;
    }
}
