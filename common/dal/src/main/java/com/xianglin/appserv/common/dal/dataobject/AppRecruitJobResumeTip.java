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
@TableName("app_recruit_job_resume_tip")
public class AppRecruitJobResumeTip extends Base{

    private Long jobResumeId;

    private String tip;

    @Builder
    public AppRecruitJobResumeTip(Long id, String isDeleted, Date createTime, Date updateTime, String comments, Long jobResumeId, String tip) {
        super(id, isDeleted, createTime, updateTime, comments);
        this.jobResumeId = jobResumeId;
        this.tip = tip;
    }
}
