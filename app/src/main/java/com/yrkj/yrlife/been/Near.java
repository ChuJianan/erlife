package com.yrkj.yrlife.been;

import android.graphics.Bitmap;

import org.xutils.view.annotation.Event;

/**
 * Created by cjn on 2016/3/29.
 */
public class Near extends Entity {

    private String machine_name;//设备名称
    private String machine_number;//设备编号

    public String getMachine_number() {
        return machine_number;
    }

    public void setMachine_number(String machine_number) {
        this.machine_number = machine_number;
    }

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
    private String detailUrl;//详情地址
    private String isWashing;//状态
    private int orders;//订单数
    private String qrCodeUrl;
    private String[] machineImages;

    public String[] getMachineImages() {
        return machineImages;
    }

    public void setMachineImages(String[] machineImages) {
        this.machineImages = machineImages;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getIsWashing() {
        return isWashing;
    }

    public void setIsWashing(String isWashing) {
        this.isWashing = isWashing;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
