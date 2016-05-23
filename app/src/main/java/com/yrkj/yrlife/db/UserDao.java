package com.yrkj.yrlife.db;


import com.yrkj.yrlife.been.User;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;


/**
 * 用户信息表
 * Created by cjn on 2016/5/19.
 */
public class UserDao {

    static DbManager.DaoConfig daoConfig = SQLiteHelper.getDaoConfig();
    static DbManager db = x.getDb(daoConfig);

    /**
     * 存储用户信息
     *
     * @param user
     */
    public static void insert(User user) {
        try {
            db.save(user);
        } catch (DbException e) {

        }
    }

    /**
     * 查找用户信息
     *
     * @param id 用户id
     * @return
     */
    public static User query(int id) {
        User user = new User();
        try {
            user = db.findById(User.class, id);
        } catch (DbException e) {

        }
        return user;
    }

    /**
     * 查找用户信息
     *
     * @param phone 手机号
     * @return
     */
    public static User query(String phone) {
        try {
            User user = db.selector(User.class).where("phone", "=", phone).findFirst();
            return user;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新
     *
     * @param phone
     * @param name
     * @param key
     * @return
     */
    public static boolean update(String phone, String name, String key) {
        try {
            User user = db.selector(User.class).where("phone","=",phone).findFirst();
            user.setReal_name(name);
            db.update(name,key);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    public static boolean delete(int id) {
        try {
            db.deleteById(User.class, id);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据手机号删除
     *
     * @param phone
     * @return
     */
    public static boolean delete(String phone) {
        try {
            User user = db.selector(User.class).where("phone", "=", phone).findFirst();
            db.delete(user);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除整个表中的数据
     *
     * @return
     */
    public static boolean delete() {
        try {
            db.delete(User.class);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }
}
