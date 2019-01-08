package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroupMember {
    private Long id;

    private Long groupId;

    private Long partyId;
    
    private String type;

    private String tempName;

    private String tempMobilePhone;

    private Long operator;

    private String status;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

}
