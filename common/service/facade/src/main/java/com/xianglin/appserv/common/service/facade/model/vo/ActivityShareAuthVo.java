package com.xianglin.appserv.common.service.facade.model.vo;/**
 * Created by wanglei on 2017/3/14.
 */

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 * @create 2017-03-14 14:12
 **/
public class ActivityShareAuthVo extends BaseVo{

    private Long id;

    private String openId;

    private String mobilePhone;

    private Long partyId;

    private String outhStatus;

    private String nickName;

    private String headImgUrl;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getOpenId () {
        return openId;
    }

    public void setOpenId (String openId) {
        this.openId = openId;
    }

    public String getMobilePhone () {
        return mobilePhone;
    }

    public void setMobilePhone (String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Long getPartyId () {
        return partyId;
    }

    public void setPartyId (Long partyId) {
        this.partyId = partyId;
    }

    public String getOuthStatus () {
        return outhStatus;
    }

    public void setOuthStatus (String outhStatus) {
        this.outhStatus = outhStatus;
    }

    public String getNickName () {
        return nickName;
    }

    public void setNickName (String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl () {
        return headImgUrl;
    }

    public void setHeadImgUrl (String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getIsDeleted () {
        return isDeleted;
    }

    public void setIsDeleted (String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime () {
        return createTime;
    }

    public void setCreateTime (Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime () {
        return updateTime;
    }

    public void setUpdateTime (Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComments () {
        return comments;
    }

    public void setComments (String comments) {
        this.comments = comments;
    }
}
