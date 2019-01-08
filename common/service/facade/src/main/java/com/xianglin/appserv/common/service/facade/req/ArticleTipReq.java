package com.xianglin.appserv.common.service.facade.req;/**
 * Created by wanglei on 2017/2/21.
 */

import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;

/**
 * @author
 * @create 2017-02-21 17:43
 **/
public class ArticleTipReq extends PageReq{

    private String startDate;

    private String endDate;

    private String tipType;

    private String dealStatus;

    private String userNickName;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTipType() {
        return tipType;
    }

    public void setTipType(String tipType) {
        this.tipType = tipType;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}
