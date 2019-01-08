package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * Created by wanglei on 2017/9/18.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedbackVo extends BaseVo{

    private Long id;

    private Long partyId;
    
    private String phone;

    private String date;

    private String content;

    private String status;

    private String creater;

    private String remark;

    private String operator;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;


}
