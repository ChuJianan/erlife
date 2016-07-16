package com.yrkj.yrlife.been;

/**
 * Created by cjn on 2016/7/16.
 */
public class Upmp extends Entity {
    private String merCode;// 商户号
    private String accCode;// 交易账户号
    private String merBillNo;// 商户订单号
    private String ccyCode;// 币种:156(人民币)
    private String prdCode;// 支付类型：2301(移动无卡支付UPMP)
    private String tranAmt;// 订单金额(单位：元,如10.00)
    private String requestTime;// 请求时间:格式样例(2015-7-30 00:00:00)
    private String ordPerVal;// 订单有效期
    private String merNoticeUrl;// 商户自己系统后台通知URL
    private String orderDesc;// 订单描述
    private String bankCard;// 银行卡号
    private String sign;//签名
    private String merRequestInfo;

    public String getMerRequestInfo() {
        return merRequestInfo;
    }

    public void setMerRequestInfo(String merRequestInfo) {
        this.merRequestInfo = merRequestInfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getAccCode() {
        return accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode;
    }

    public String getMerBillNo() {
        return merBillNo;
    }

    public void setMerBillNo(String merBillNo) {
        this.merBillNo = merBillNo;
    }

    public String getCcyCode() {
        return ccyCode;
    }

    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(String tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getOrdPerVal() {
        return ordPerVal;
    }

    public void setOrdPerVal(String ordPerVal) {
        this.ordPerVal = ordPerVal;
    }

    public String getMerNoticeUrl() {
        return merNoticeUrl;
    }

    public void setMerNoticeUrl(String merNoticeUrl) {
        this.merNoticeUrl = merNoticeUrl;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
}
