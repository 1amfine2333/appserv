package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiang yong tao
 * @date 2018/10/8  15:26
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppRecruitJobResumeTipVo implements Serializable {

    private Long jobResumeId;

    private String tip;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
}
