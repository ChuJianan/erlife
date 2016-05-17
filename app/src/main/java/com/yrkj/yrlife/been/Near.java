package com.yrkj.yrlife.been;

import android.graphics.Bitmap;

import org.xutils.view.annotation.Event;

/**
 * Created by cjn on 2016/3/29.
 */
public class Near extends Entity {

    private  String machine_name;//设备名称

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMachine_pic() {
        return machine_pic;
    }

    public void setMachine_pic(String machine_pic) {
        this.machine_pic = machine_pic;
    }

    private String address;//地址
//    private String tel;
    private String lng;//经度
    private String lat;//纬度
    private String machine_pic;//图片地址


}
