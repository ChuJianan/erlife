package com.yrkj.yrlife.been;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户信息
 * Created by cjn on 2016/5/9.
 */
public class User extends Entity {

    private String account;
    private String pwd;
    private String real_name;
    private String nick_name;
    private String sex;
    private String phone;
    private String email;
    private int member_grade_type_id;
    private String member_type;
    private String head_image;
    private String isenable;
    private String province;
    private String city;
    private String zone;
    private String detail_address;
    private int card_total_point;
    private String memo;
    private int sort;
    private String isdel;
    private Date create_time;
    private int create_id;
    private Date update_time;
    private int update_id;

    private BigDecimal total_balance;
    private BigDecimal total_consume;

    private String card_number;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMember_grade_type_id() {
        return member_grade_type_id;
    }

    public void setMember_grade_type_id(int member_grade_type_id) {
        this.member_grade_type_id = member_grade_type_id;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getIsenable() {
        return isenable;
    }

    public void setIsenable(String isenable) {
        this.isenable = isenable;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public int getCard_total_point() {
        return card_total_point;
    }

    public void setCard_total_point(int card_total_point) {
        this.card_total_point = card_total_point;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getCreate_id() {
        return create_id;
    }

    public void setCreate_id(int create_id) {
        this.create_id = create_id;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public int getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(int update_id) {
        this.update_id = update_id;
    }

    public BigDecimal getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(BigDecimal total_balance) {
        this.total_balance = total_balance;
    }

    public BigDecimal getTotal_consume() {
        return total_consume;
    }

    public void setTotal_consume(BigDecimal total_consume) {
        this.total_consume = total_consume;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getHave_card() {
        return have_card;
    }

    public void setHave_card(String have_card) {
        this.have_card = have_card;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    private String have_card;
    private String merchant_id;

}
