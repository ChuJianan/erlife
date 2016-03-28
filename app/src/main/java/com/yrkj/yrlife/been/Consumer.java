package com.yrkj.yrlife.been;

/**
 * Created by cjn on 2016/3/28.
 */
public class Consumer extends Entity {
    private String costType;
    private String costDate;
    private String costMoney;

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }
}
