package com.xianglin.appserv.common.service.facade.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanglei on 2017/5/12.
 */
public class AppTransactionVo extends BaseVo{

    private static final long serialVersionUID = -7251140768922929464L;

    private Long id;

    private String transNo;

    private String subTransNo;

    private Long partyId;

    private String mobilePhone;

    private String activityCode;

    private String activityCategary;

    private Long taskId;

    private String transType;

    private String amtType;

    private BigDecimal amount;

    private String transStatus;

    private String daily;

    private String transRemark;

    private String transResult;

    private Long toPartyId;

    private String toMobilePhone;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getSubTransNo() {
        return subTransNo;
    }

    public void setSubTransNo(String subTransNo) {
        this.subTransNo = subTransNo;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityCategary() {
        return activityCategary;
    }

    public void setActivityCategary(String activityCategary) {
        this.activityCategary = activityCategary;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getAmtType() {
        return amtType;
    }

    public void setAmtType(String amtType) {
        this.amtType = amtType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getTransRemark() {
        return transRemark;
    }

    public void setTransRemark(String transRemark) {
        this.transRemark = transRemark;
    }

    public String getTransResult() {
        return transResult;
    }

    public void setTransResult(String transResult) {
        this.transResult = transResult;
    }

    public Long getToPartyId() {
        return toPartyId;
    }

    public void setToPartyId(Long toPartyId) {
        this.toPartyId = toPartyId;
    }

    public String getToMobilePhone() {
        return toMobilePhone;
    }

    public void setToMobilePhone(String toMobilePhone) {
        this.toMobilePhone = toMobilePhone;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
