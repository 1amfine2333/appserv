package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**活动礼物
 * Created by wanglei on 2017/5/5.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRewardVo extends BaseVo{

    private Long id;

    private String activityCode;

    private String rewardCategary;

    private String rewardType;

    private String rewardName;

    private int priorityLevel;

    private BigDecimal startAmt;

    private BigDecimal endAmt;

    private BigDecimal rewardAmt;

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
