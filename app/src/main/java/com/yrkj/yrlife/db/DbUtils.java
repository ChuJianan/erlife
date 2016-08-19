package com.yrkj.yrlife.db;

import android.os.Environment;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;

import java.io.File;

/**
 * Created by cjn on 2016/8/19.
 */
public class DbUtils  {
    static DbManager.DaoConfig daoConfig;
    public static DbManager.DaoConfig getDaoConfig(){
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbName("yrkj.db")
                    .setDbDir(file)
                    .setDbVersion(1)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            LogUtil.e("数据库更新了！");
                        }
                    });
        }
        return daoConfig;
    }
}
