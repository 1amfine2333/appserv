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
public class AppActivityReward {
    private Long id;

    private String activityCode;

    private String rewardCategary;

    private String rewardType;

    private String rewardName;
    
    private int priorityLevel;

    private BigDecimal startAmt;

    private BigDecimal endAmt;

    private Integer startSection;

    private Integer endSection;

    private Integer dailyLimit;

    private Integer limit;

    private String rewardStatus;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private String descs;

    private String subCategary;
    
    private String showStatus;
}
