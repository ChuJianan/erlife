package com.yrkj.yrlife.been;

import java.util.Date;

/**
 * Created by cjn on 2016/6/28.
 */
public class MyIdear {
    private String title;//标题
    private String content;//内容
    private String create_timeStr;//时间
    private int member_id;//会员id

    public String getCreate_timeStr() {
        return create_timeStr;
    }

    public void setCreate_timeStr(String create_timeStr) {
        this.create_timeStr = create_timeStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }
}
