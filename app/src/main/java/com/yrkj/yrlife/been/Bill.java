package com.yrkj.yrlife.been;

/**
 * 消费记录
 * Created by cjn on 2016/3/28.
 */
public class Bill extends Entity {
    private String title;
    private  String create_time;
    private  String price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
