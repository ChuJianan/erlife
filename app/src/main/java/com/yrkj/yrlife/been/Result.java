package com.yrkj.yrlife.been;

import com.yrkj.yrlife.app.AppException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by cjn on 2016/5/17.
 */
public class Result {
    public List<Near> nears = Collections.emptyList();
    public List<Consumer> consumers = Collections.emptyList();
    public List<Bill> bill = Collections.emptyList();
    public List<Pay> pays = Collections.emptyList();
    public List<News> news = Collections.emptyList();
    public List<MyIdear> myFeedBackList = Collections.emptyList();
    public List<Vouchers> couponList=Collections.emptyList();
    public List<CustomerService> customersList=Collections.emptyList();
    public RemarkStarSection remarkStarSection;
    private HomePage homePage;
    private int code;
    private String message;
    private int totalPage;
    private int count;
    private WeixinPay wxpay;
    private String result;
    private Washing_no_card_record washing_no_card_record;
    private BigDecimal spend_money;
    private PayConfirm payconfirm;
    private String[] imageAbsoluteUrl = {};
    private AliPay aliPay;
    private Upmp ipsPay;

    public HomePage getHomePage(){
        return homePage;
    }

    public Upmp ipsPay(){
        return ipsPay;
    }

    public boolean OK() {
        return code == 1;
    }

    public boolean isOK() {
        return code == 3;
    }
    public boolean isLoadOK() {
        return code == 4;
    }
    public String Message() {
        return message;
    }

    public int totalPage() {
        return totalPage;
    }

    public WeixinPay getWxpay() {
        return wxpay;
    }

    public String Result() {
        return result;
    }

    public Washing_no_card_record washing() {
        return washing_no_card_record;
    }

    public BigDecimal spend_money() {
        return spend_money;
    }

    public PayConfirm payconfirm() {
        return payconfirm;
    }

    public String[] getImg_urls() {
        return imageAbsoluteUrl;
    }

    public int count() {
        return count;
    }

    public AliPay aliPay(){
        return aliPay;
    }

    /**
     * 解析调用结果
     *
     * @param jsonString
     * @return
     * @throws IOException
     */
    public static Result parse(String jsonString) throws IOException, AppException {
        Result result = null;
        return result;
    }

    @Override
    public String toString() {
        return String.format("RESULT: CODE:%d,MSG:%s", code, message);
    }

}
