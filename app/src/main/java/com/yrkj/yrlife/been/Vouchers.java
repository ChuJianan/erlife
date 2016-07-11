package com.yrkj.yrlife.been;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券
 * Created by cjn on 2016/3/23.
 */
public class Vouchers extends Entity {
    private String batch;//优惠卷批次
    private String type;//优惠卷类型(0普通优惠卷，1兑换商品卷，2打车卷，3代金卷，4洗车卷，5红包等)
    private String name;//优惠卷名称
    private String memo;//优惠卷描述
    private String img_path;//优惠卷图片
//    private Date start_time;//优惠卷开始时间
    private String start_timeStr;
//    private Date end_time;//优惠卷结束时间
    private String end_timeStr;
    private int represent_point;//代表积分
    private BigDecimal represent_price;//代表金额
    private int total_count;//发行总数量
    private int rest_count;//剩余数量
    private int used_count;//发出数量
    private String isdel;//是否删除
    private String isenable;//是否使用停用
//    private Date create_time;//创建时间
    private int delay_days;//推迟时间天数
//    private Date due_time;//优惠券失效时间
    private String ifdue_time_reset;//是否失效日期自用户领取后再计算
    private String deadline;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStart_time_Str() {
        return start_timeStr;
    }

    public void setStart_time_Str(String start_time_Str) {
        this.start_timeStr = start_time_Str;
    }

    public String getEnd_time_Str() {
        return end_timeStr;
    }

    public void setEnd_time_Str(String end_time_Str) {
        this.end_timeStr = end_time_Str;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

//    public Date getStart_time() {
//        return start_time;
//    }
//
//    public void setStart_time(Date start_time) {
//        this.start_time = start_time;
//    }
//
//    public Date getEnd_time() {
//        return end_time;
//    }
//
//    public void setEnd_time(Date end_time) {
//        this.end_time = end_time;
//    }

    public int getRepresent_point() {
        return represent_point;
    }

    public void setRepresent_point(int represent_point) {
        this.represent_point = represent_point;
    }

    public BigDecimal getRepresent_price() {
        return represent_price;
    }

    public void setRepresent_price(BigDecimal represent_price) {
        this.represent_price = represent_price;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getRest_count() {
        return rest_count;
    }

    public void setRest_count(int rest_count) {
        this.rest_count = rest_count;
    }

    public int getUsed_count() {
        return used_count;
    }

    public void setUsed_count(int used_count) {
        this.used_count = used_count;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getIsenable() {
        return isenable;
    }

    public void setIsenable(String isenable) {
        this.isenable = isenable;
    }

//    public Date getCreate_time() {
//        return create_time;
//    }
//
//    public void setCreate_time(Date create_time) {
//        this.create_time = create_time;
//    }

    public int getDelay_days() {
        return delay_days;
    }

    public void setDelay_days(int delay_days) {
        this.delay_days = delay_days;
    }

//    public Date getDue_time() {
//        return due_time;
//    }
//
//    public void setDue_time(Date due_time) {
//        this.due_time = due_time;
//    }

    public String getIfdue_time_reset() {
        return ifdue_time_reset;
    }

    public void setIfdue_time_reset(String ifdue_time_reset) {
        this.ifdue_time_reset = ifdue_time_reset;
    }
}
