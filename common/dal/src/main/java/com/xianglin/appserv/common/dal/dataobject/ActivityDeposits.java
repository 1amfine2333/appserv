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
public class ActivityDeposits {
    private Long id;

    private Long partyId;

    private BigDecimal startAchieve;

    private BigDecimal currentAchieve;

    private BigDecimal goalAchieve;

    private String rewardSign;

    private String rewardResult;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

}