package com.yrkj.yrlife.been;

/**
 * Created by cjn on 2016/11/14.
 */

public class Complaints extends Entity {
    private String img_urls;
    private String name;
    private String content;
    private String time;
    private String floor;
    private int star_nub;

    public String getImg_urls() {
        return img_urls;
    }

    public void setImg_urls(String img_urls) {
        this.img_urls = img_urls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getStar_nub() {
        return star_nub;
    }

    public void setStar_nub(int star_nub) {
        this.star_nub = star_nub;
    }
}
