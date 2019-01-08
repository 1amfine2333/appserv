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
public class RecruitResumeReq extends PageReq {
    private Long id;

    private Long partyId;

    private String type;

    private String name;

    private String gender;

    private String ethnicity;

    private String education;

    private String criminalRecord;

    private String contactPhone;

    private String certificatesNumber;

    private String district;

    private String leaderSign;

    private String recommendPhone;

    private String expectJob;

    private String payType;

    private Integer payStart;

    private Integer payEnd;

    private String marriageSign;

    private String location;

    private AreaVo areaVo;

    private String provinceSign;

    private String manageStatus;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
    
    private String status;
    
    private String orderBy;
}
