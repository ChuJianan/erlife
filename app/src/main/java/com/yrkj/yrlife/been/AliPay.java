package com.yrkj.yrlife.been;

import org.xutils.view.annotation.Event;

/**
 * Created by cjn on 2016/7/6.
 */
public class AliPay extends Entity {
    private String out_trade_no;//订单编号
    private String sign;//签名
    private String notify_url;//回调地址
    private String subject;//商品名称
    private String body;//商品详情
    private String service;//接口名称
    private String partner;//商户PID
    private String createLinkString;//拼接字符串

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getCreateLinkString() {
        return createLinkString;
    }

    public void setCreateLinkString(String createLinkString) {
        this.createLinkString = createLinkString;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
