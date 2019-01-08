package com.xianglin.appserv.common.service.facade.model.vo;

import java.util.Date;

/**
 * Created by wanglei on 2017/6/9.
 */
public class UserCreaditApplyVo extends BaseVo{

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
        this.applyUsername = applyUsername;
    }

    public String getApplyCardno() {
        return applyCardno;
    }

    public void setApplyCardno(String applyCardno) {
        this.applyCardno = applyCardno;
    }

    public String getApplyMobile() {
        return applyMobile;
    }

    public void setApplyMobile(String applyMobile) {
        this.applyMobile = applyMobile;
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
