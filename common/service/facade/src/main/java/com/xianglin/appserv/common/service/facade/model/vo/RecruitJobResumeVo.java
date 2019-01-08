package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/4/24 18:28.
 * Update reason :
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecruitJobResumeVo implements Serializable {

    private static final long serialVersionUID = -5102293077715242258L;

    private Long id;

    private Long partyId;

    private Long jobId;

    private Long recruitId;

    private String manageStatus;//简历的管理状态

    private String status;
    
    private String remark;//备注
    
    private String type;//有奖推荐类型、求职人员管理类型

    private Integer commission;

    private String commissionStatus;

    private String idDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
}
