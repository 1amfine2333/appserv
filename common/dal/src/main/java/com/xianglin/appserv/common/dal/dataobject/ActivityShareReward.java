package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class ActivityShareReward {
    private Long id;

    private Long dailyId;

    private String mobilePhone;

    private Long partyId;

    private String daily;

    private String rewardType;

    private BigDecimal rewardAmt;

    private String relationId;

    private String remark;

    private String rewardStatus;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private ActivityShareAuth activityShareAuth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDailyId() {
        return dailyId;
    }

    public void setDailyId(Long dailyId) {
        this.dailyId = dailyId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType == null ? null : rewardType.trim();
    }

    public BigDecimal getRewardAmt() {
        return rewardAmt;
    }

    public void setRewardAmt(BigDecimal rewardAmt) {
        this.rewardAmt = rewardAmt;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId == null ? null : relationId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus == null ? null : rewardStatus.trim();
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
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
        this.comments = comments == null ? null : comments.trim();
    }

    public String getDaily () {
        return daily;
    }

    public void setDaily (String daily) {
        this.daily = daily;
    }

    public ActivityShareAuth getActivityShareAuth () {
        return activityShareAuth;
    }

    public void setActivityShareAuth (ActivityShareAuth activityShareAuth) {
        this.activityShareAuth = activityShareAuth;
    }
}