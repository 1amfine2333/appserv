package com.xianglin.appserv.common.service.facade.req;

import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/1/31 18:43.
 * Update reason :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityInviteDetailReq extends PageReq {
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
