package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/9/11.
 */
public class MerchantOrderVo extends BaseVo{

    private String orderNo;

    private String orderAmount;

    private String orderStatus;

    private String orderInfo;

    /**
     * 付款码
     */
    private String authCode;
    /**
     * 店铺号
     * */
    private String shopCode;

    /**
     * 消息提醒内容
     */
    private String alertTip;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAlertTip() {
        return alertTip;
    }

    public void setAlertTip(String alertTip) {
        this.alertTip = alertTip;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}
