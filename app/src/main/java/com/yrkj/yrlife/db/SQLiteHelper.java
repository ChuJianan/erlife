package com.yrkj.yrlife.db;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * 数据库操作类
 * Created by cjn on 2016/5/19.
 */
public class SQLiteHelper {
    static DbManager.DaoConfig daoConfig;
    private static String DB_NAME = "erkj.db";
    private static int DB_VERSION = 1;
    public static DbManager.DaoConfig getDaoConfig(){
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        if(daoConfig==null){
             daoConfig = new DbManager.DaoConfig().setDbName(DB_NAME).setDbDir(file)  //数据库路径
                    .setDbVersion(DB_VERSION)  //数据库版本
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            // TODO: ...
                            // db.addColumn(...);
                            // db.dropTable(...);
                        }
                    });
        }
        return daoConfig;
    }
}
