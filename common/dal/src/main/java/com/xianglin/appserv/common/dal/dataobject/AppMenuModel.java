package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class AppMenuModel {
    private String mid;

    private String pid;

    private String mname;

    private BigDecimal mlevel;

    private BigDecimal morder;

    private String murl;

    private String imgurl;

    private String mstatus;

    private BigDecimal busitype;

    private Date createTime;

    private Date updateTime;

    private String comments;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname == null ? null : mname.trim();
    }

    public BigDecimal getMlevel() {
        return mlevel;
    }

    public void setMlevel(BigDecimal mlevel) {
        this.mlevel = mlevel;
    }

    public BigDecimal getMorder() {
        return morder;
    }

    public void setMorder(BigDecimal morder) {
        this.morder = morder;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl == null ? null : murl.trim();
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getMstatus() {
        return mstatus;
    }

    public void setMstatus(String mstatus) {
        this.mstatus = mstatus == null ? null : mstatus.trim();
    }

    public BigDecimal getBusitype() {
        return busitype;
    }

    public void setBusitype(BigDecimal busitype) {
        this.busitype = busitype;
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