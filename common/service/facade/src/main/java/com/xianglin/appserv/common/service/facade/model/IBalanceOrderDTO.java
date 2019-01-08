package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 17:23.
 * Update reason :
 */
public class IBalanceOrderDTO  implements Serializable {
    private static final long serialVersionUID = 4277986815095395264L;
    private String orderNo;
    private String partyId;
    private String accCode;
    private String cardNo;
    private String mobilePhone;
    private String amount;
    private String transType;
    private String orderStatus;
    private String orderInfo;
    private String channelCode;
    private String channelName;
    private String isDelete;
    private Date createTime;
    private String comments;

    public IBalanceOrderDTO() {
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null?null:orderNo.trim();
    }

    public String getPartyId() {
        return this.partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId == null?null:partyId.trim();
    }

    public String getAccCode() {
        return this.accCode;
    }

    public void setAccCode(String accCode) {
        this.accCode = accCode == null?null:accCode.trim();
    }

    public String getCardNo() {
        return this.cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null?null:cardNo.trim();
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null?null:mobilePhone.trim();
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return this.transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null?null:transType.trim();
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null?null:orderStatus.trim();
    }

    public String getOrderInfo() {
        return this.orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo == null?null:orderInfo.trim();
    }

    public String getChannelCode() {
        return this.channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null?null:channelCode.trim();
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName == null?null:channelName.trim();
    }

    public String getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null?null:isDelete.trim();
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null?null:comments.trim();
    }
}
