/**
 *
 */
package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wanglei 2016年12月13日下午3:09:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInviteDetailVo extends BaseVo {
    private Long id;

    private String activityCode;

    private String loginName;

    private Long partyId;

    private Long recPartyId;

    private String deviceId;

    private String commentName;

    private String source;

    private String status;

    private String msgStatus;

    private BigDecimal amt;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
}
