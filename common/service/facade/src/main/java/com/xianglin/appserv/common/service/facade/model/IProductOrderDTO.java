package com.xianglin.appserv.common.service.facade.model;

import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 17:31.
 * Update reason :
 */
public class IProductOrderDTO {
    private static final long serialVersionUID = -6348539119830894913L;
    private String orderNo;
    private String partyId;
    private String accCode;
    private String productCode;
    private String productName;
    private String mobilePhone;
    private String amount;
    private String transType;
    private String ransomType;
    private String orderStatus;
    private String orderInfo;
    private String channelCode;
    private String channelName;
    private String isDelete;
    private Date createTime;
    private Date updateTime;
    private String comments;

    public IProductOrderDTO() {
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

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null?null:productCode.trim();
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null?null:productName.trim();
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

    public String getRansomType() {
        return this.ransomType;
    }

    public void setRansomType(String ransomType) {
        this.ransomType = ransomType == null?null:ransomType.trim();
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

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null?null:comments.trim();
    }
}
