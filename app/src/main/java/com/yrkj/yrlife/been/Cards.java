package com.yrkj.yrlife.been;

import org.xutils.view.annotation.Event;

/**
 * 会员卡
 * Created by Administrator on 2016/3/23.
 */
public class Cards extends Entity {
    private String cardNumber;
    private String type_name;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
