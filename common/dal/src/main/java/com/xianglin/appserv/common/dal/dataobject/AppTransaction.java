package com.xianglin.appserv.common.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppTransaction {

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

}