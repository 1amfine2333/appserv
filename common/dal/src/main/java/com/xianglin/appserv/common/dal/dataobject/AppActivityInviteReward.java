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
public class AppActivityInviteReward {
    private Long id;

    private String rewardId;

    private String activityCode;

    private Long partyId;

    private String day;

    private Integer amount;

    private Integer scale;

    private Integer reward;

    private Long recPartyId;

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
}