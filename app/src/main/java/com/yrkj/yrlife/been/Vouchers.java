package com.yrkj.yrlife.been;

/**
 * 优惠券
 * Created by cjn on 2016/3/23.
 */
public class Vouchers extends Entity {
    private String money;
    private String date;
    private String type;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
