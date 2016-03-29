package com.yrkj.yrlife.been;

import android.graphics.Bitmap;

import org.xutils.view.annotation.Event;

/**
 * Created by cjn on 2016/3/29.
 */
public class Near extends Entity {

    private  String nid;
    private String adr;
    private String tel;
    private String dis;
    private Bitmap bitmap;

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
