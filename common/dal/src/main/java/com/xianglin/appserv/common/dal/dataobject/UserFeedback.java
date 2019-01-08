package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedback {
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
