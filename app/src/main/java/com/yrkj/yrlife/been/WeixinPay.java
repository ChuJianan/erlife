package com.yrkj.yrlife.been;

/**
 * Created by cjn on 2016/5/31.
 */
public class WeixinPay extends Entity {
    private String prePayId;//微信给的预支付交易会话标识
    private String timeStamp;//时间戳
    private String paySign;// 签名
    private String nonceStr;//随机字符串

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    private String orderNumber;//订单号

}