package com.xianglin.appserv.common.service.facade.req;

import com.xianglin.appserv.common.service.facade.model.vo.AreaVo;
import com.xianglin.appserv.common.service.facade.model.vo.req.PageReq;
import lombok.*;

import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/4/25 10:36.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecruitJobResumeReq extends PageReq {
    private Long id;

    private Long partyId;

    private Long jobId;

    private Long recruitId;

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments; 
}
