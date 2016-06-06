package com.yrkj.yrlife.been;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by cjn on 2016/6/6.
 */
public class Washing_no_card_record extends Entity {
    private String machine_number;//机器编号
    private int machine_id;//机器id
    private int member_id;//会员id
    private String card_number;//卡编号
    private int card_id;//卡id
    private BigDecimal total_money;//卡总金额
    private Date create_time;//创建时间
    private String isfinish;//本次洗车是否结算完，1代表结算完，默认是0
    private String belong;//本次洗车单号
    private String notice;//存放提示语，如“洗车机距离过远”
    private String ispay;//是否付钱。1付钱，0未付钱
    private String issend;//socket中使用
    private String noCardWashingType;//表明是无卡洗车中的有卡会员 ，还是使用虚拟卡\
    private String address;//地址

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMachine_number() {
        return machine_number;
    }

    public void setMachine_number(String machine_number) {
        this.machine_number = machine_number;
    }

    public int getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(int machine_id) {
        this.machine_id = machine_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public BigDecimal getTotal_money() {
        return total_money;
    }

    public void setTotal_money(BigDecimal total_money) {
        this.total_money = total_money;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getIsfinish() {
        return isfinish;
    }

    public void setIsfinish(String isfinish) {
        this.isfinish = isfinish;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getIspay() {
        return ispay;
    }

    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public String getNoCardWashingType() {
        return noCardWashingType;
    }

    public void setNoCardWashingType(String noCardWashingType) {
        this.noCardWashingType = noCardWashingType;
    }
}
