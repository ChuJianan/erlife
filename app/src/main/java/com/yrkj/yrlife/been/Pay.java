package com.yrkj.yrlife.been;

/**
 * Created by cjn on 2016/3/25.
 */
public class Pay extends Entity {
     private String payType;
    private  String payDate;
    private  String payMoney;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}
