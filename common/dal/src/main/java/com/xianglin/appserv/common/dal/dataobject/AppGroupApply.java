package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroupApply {
    private Long id;

    private Long groupId;

    private Long partyId;

    private String status;

    private Long operator;

    private String type;

    private String auditor;
    
    private String opinion;

    private String remark;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;
    
}
