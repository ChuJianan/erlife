package com.yrkj.yrlife.been;

import java.math.BigDecimal;

/**
 * Created by cjn on 2016/6/6.
 */
public class PayConfirm extends Entity {
    private BigDecimal totalmoney;//总共消费金额
    private String belong;//消费账单编号
    private String cardnumber;//卡号
    private String machinenumber;//机器编号
    private String time;//时间
    private String address;//机器详细地址

    public BigDecimal getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(BigDecimal totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMachinenumber() {
        return machinenumber;
    }

    public void setMachinenumber(String machinenumber) {
        this.machinenumber = machinenumber;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }


}
