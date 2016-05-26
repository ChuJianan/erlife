package com.yrkj.yrlife.been;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户信息
 * Created by cjn on 2016/5/9.
 */
@Table(name = "user")
public class User extends Entity {

    @Column(name="account")
    private String account;//登录帐号
    @Column(name="pwd")
    private String pwd;//登录密码
    @Column(name="real_name")
    private String real_name;//姓名
    @Column(name="nick_name")
    private String nick_name;//昵称
    @Column(name="sex")
    private String sex;//性别
    @Column(name="phone")
    private String phone;//手机
    @Column(name="email")
    private String email;//邮箱
    @Column(name="member_grade_type_id")
    private int member_grade_type_id;//会员等级分表
    @Column(name="member_type")
    private String member_type;//会员类型
    @Column(name="head_image")
    private String head_image;//头像
    @Column(name="isenable")
    private String isenable;//是否禁用或启用
    @Column(name="province")
    private String province;//省
    @Column(name="city")
    private String city;//市
    @Column(name="zone")
    private String zone;//区
    @Column(name="detail_address")
    private String detail_address;//详情地址
    @Column(name="card_total_point")
    private int card_total_point;//卡总积分
    @Column(name="memo")
    private String memo;//备注
    @Column(name="sort")
    private int sort;//排序
    @Column(name="isdel")
    private String isdel;//是否删除
    @Column(name="create_time")
    private Date create_time;//创建时间
    @Column(name="create_id")
    private int create_id;//创建人编号
    @Column(name="update_time")
    private Date update_time;//修改时间
    @Column(name="update_id")
    private int update_id;//修改人编号
    @Column(name="total_balance")
    private BigDecimal total_balance;//会员总余额
    @Column(name="total_consume")
    private BigDecimal total_consume;//会员累计消费金
    @Column(name="card_number")
    private String card_number;//
    @Column(name="due_time")
    private Date due_time;//过期时间
    @Column(name="secret_code")
    private String secret_code;//用户密钥
    @Column(name="unique_phone_code")
    private String unique_phone_code;//手机唯一码
    private String wx_head_pic;//微信头像
    private String isBind;//是否绑定会员卡

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public Date getDue_time() {
        return due_time;
    }

    public void setDue_time(Date due_time) {
        this.due_time = due_time;
    }

    public String getSecret_code() {
        return secret_code;
    }

    public void setSecret_code(String secret_code) {
        this.secret_code = secret_code;
    }

    public String getUnique_phone_code() {
        return unique_phone_code;
    }

    public void setUnique_phone_code(String unique_phone_code) {
        this.unique_phone_code = unique_phone_code;
    }


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

    public String getWx_head_pic() {
        return wx_head_pic;
    }

    public void setWx_head_pic(String wx_head_pic) {
        this.wx_head_pic = wx_head_pic;
    }

    private String have_card;
    private String merchant_id;

}
