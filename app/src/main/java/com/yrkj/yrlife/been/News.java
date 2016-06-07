package com.yrkj.yrlife.been;

import java.util.Date;

/**
 * Created by cjn on 2016/6/7.
 */
public class News extends Entity {
    /**
     * 消息来源 （0管理员，1程序自动 产生）
     */
    private String how_build;
    /**
     * 消息类型（0系统消息，1新会员消息，2充值消息）
     */
    private String message_type;
    /**
     * 标题
     */
    private String title;
    /**
     * 导航小图片
     */
    private String nav_img;
    /**
     * 发布时间
     */
    private String publish_timeStr;
    /**
     * 点击次数
     */
    private int hit_count;
    /**
     * 是否是热门消息
     */
    private String ishot;

    private String member_id;

	/* 网页地址 */

    private String detail_url;

    public String getHow_build() {
        return how_build;
    }

    public void setHow_build(String how_build) {
        this.how_build = how_build;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNav_img() {
        return nav_img;
    }

    public void setNav_img(String nav_img) {
        this.nav_img = nav_img;
    }

    public String getPublish_timeStr() {
        return publish_timeStr;
    }

    public void setPublish_timeStr(String publish_timeStr) {
        this.publish_timeStr = publish_timeStr;
    }

    public int getHit_count() {
        return hit_count;
    }

    public void setHit_count(int hit_count) {
        this.hit_count = hit_count;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }
}
