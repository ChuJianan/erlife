package com.yrkj.yrlife.been;

/**
 * Created by cjn on 2016/8/25.
 */
public class CustomerService extends Entity {

    private String cid;
    private String nickName;
    private String headImage;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
}
