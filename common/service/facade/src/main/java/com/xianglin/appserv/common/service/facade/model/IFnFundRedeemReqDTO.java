package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 16:19.
 * Update reason :
 */
public class IFnFundRedeemReqDTO  implements Serializable {
    private static final long serialVersionUID = 6778049880397785141L;
    private String outAccCode;
    private String ransomType;
    private String fundCode;
    private String amount;
    private String mobilePhone;
    private String verInd;
    private String verCode;
    private String verCodeType;
    private String channelCode;

    public IFnFundRedeemReqDTO() {
    }

    public String getOutAccCode() {
        return this.outAccCode;
    }

    public void setOutAccCode(String outAccCode) {
        this.outAccCode = outAccCode;
    }

    public String getRansomType() {
        return this.ransomType;
    }

    public void setRansomType(String ransomType) {
        this.ransomType = ransomType;
    }

    public String getFundCode() {
        return this.fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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
