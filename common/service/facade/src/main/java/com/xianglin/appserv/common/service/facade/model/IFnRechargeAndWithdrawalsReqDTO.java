package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 16:18.
 * Update reason :
 */
public class IFnRechargeAndWithdrawalsReqDTO  implements Serializable {
    private static final long serialVersionUID = -2717712692373987837L;
    private String outAccCode;
    private String cardNo;
    private String amount;
    private String orderNo;
    private String mobilePhone;
    private String pwInd;
    private String transpw;
    private String verInd;
    private String verCode;
    private String verCodeType;
    private String channelCode;

    public IFnRechargeAndWithdrawalsReqDTO() {
    }

    public String getOutAccCode() {
        return this.outAccCode;
    }

    public void setOutAccCode(String outAccCode) {
        this.outAccCode = outAccCode;
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPwInd() {
        return this.pwInd;
    }

    public void setPwInd(String pwInd) {
        this.pwInd = pwInd;
    }

    public String getTranspw() {
        return this.transpw;
    }

    public void setTranspw(String transpw) {
        this.transpw = transpw;
    }

    public String getVerInd() {
        return this.verInd;
    }

    public void setVerInd(String verInd) {
        this.verInd = verInd;
    }

    public String getVerCode() {
        return this.verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getVerCodeType() {
        return this.verCodeType;
    }

    public void setVerCodeType(String verCodeType) {
        this.verCodeType = verCodeType;
    }

    public String getChannelCode() {
        return this.channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
