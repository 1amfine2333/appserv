package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class ActivityInviteRanking {
    private Long id;

    private String rankingDate;

    private Long partyId;

    private Integer ranking;

    private Integer inviteCount;

    private BigDecimal inviteAmt;

    private BigDecimal rewardAmt;

    private String status;

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

    public String getRankingDate() {
        return rankingDate;
    }

    public void setRankingDate(String rankingDate) {
        this.rankingDate = rankingDate == null ? null : rankingDate.trim();
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Integer inviteCount) {
        this.inviteCount = inviteCount;
    }

    public BigDecimal getInviteAmt() {
        return inviteAmt;
    }

    public void setInviteAmt(BigDecimal inviteAmt) {
        this.inviteAmt = inviteAmt;
    }

    public BigDecimal getRewardAmt() {
        return rewardAmt;
    }

    public void setRewardAmt(BigDecimal rewardAmt) {
        this.rewardAmt = rewardAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
}