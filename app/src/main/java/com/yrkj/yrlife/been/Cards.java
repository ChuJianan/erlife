package com.yrkj.yrlife.been;

import org.xutils.view.annotation.Event;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Cards extends Entity {
    private String cardNumber;
    private String cardType;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
