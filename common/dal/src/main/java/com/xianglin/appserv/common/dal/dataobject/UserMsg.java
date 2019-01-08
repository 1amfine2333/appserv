package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMsg {
    private Long id;

    private Long partyId;

    private Long msgId;
    
    private String deviceId;

    private String status;
    
    private String praiseSign;
    
    private String clickSign;
    
    private String pushSign;

    private Integer readNum;//阅读数 20171115

    private Integer shareNum;//分享数 20171115

    private String isDeleted;

    private Date createTime;

    private Date updateTime;
    
    private String comments;

    private String msgType;//备用，不在数据库中

    private String operateType;//操作类型

    private String praiseTread;//点赞，踩
}
