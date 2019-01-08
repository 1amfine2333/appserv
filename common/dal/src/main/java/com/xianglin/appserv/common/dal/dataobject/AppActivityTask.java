package com.xianglin.appserv.common.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppActivityTask {
    private Long id;

    private Long partyId;

    private String deviceId;

    private String mobilePhone;

    private String activityCode;

    private String taskCode;

    private String daily;

    private String taskDailyId;

    private String taskName;

    private String taskStatus;

    private String useStatus;

    private String taskResult;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private Page page;

    private String alertStatus;

}