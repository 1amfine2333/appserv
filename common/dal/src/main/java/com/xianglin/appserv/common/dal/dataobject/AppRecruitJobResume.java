package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;

import java.util.Date;

/**
 * Describe :
 * Created by xingyali on 2018/4/19 16:59.
 * Update reason :
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("app_recruit_job_resume")
public class AppRecruitJobResume extends Base{

    private Long partyId;

    private Long jobId;

    private Long recruitId;

    private Integer commission;

    private String remark;

    private String commissionStatus;

    private String status;

    @Builder
    public AppRecruitJobResume(Long id, String isDeleted, Date createTime, Date updateTime, String comments, Long partyId, Long jobId, Long recruitId, Integer commission, String remark,String commissionStatus, String status) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.partyId = partyId;
        this.jobId = jobId;
        this.recruitId = recruitId;
        this.commission = commission;
        this.remark = remark;
        this.commissionStatus = commissionStatus;
        this.status = status;
    }
}
