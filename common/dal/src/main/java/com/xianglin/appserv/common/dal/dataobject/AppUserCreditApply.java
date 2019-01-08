package com.xianglin.appserv.common.dal.dataobject;

import java.util.Date;

public class AppUserCreditApply {
    private Long id;

    private Long partyId;

    private String applyUsername;

    private String applyCardno;

    private String applyMobile;

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

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getApplyUsername() {
        return applyUsername;
    }

    public void setApplyUsername(String applyUsername) {
        this.applyUsername = applyUsername == null ? null : applyUsername.trim();
    }

    public String getApplyCardno() {
        return applyCardno;
    }

    public void setApplyCardno(String applyCardno) {
        this.applyCardno = applyCardno == null ? null : applyCardno.trim();
    }

    public String getApplyMobile() {
        return applyMobile;
    }

    public void setApplyMobile(String applyMobile) {
        this.applyMobile = applyMobile == null ? null : applyMobile.trim();
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
        this.comments = comments;
    }
}