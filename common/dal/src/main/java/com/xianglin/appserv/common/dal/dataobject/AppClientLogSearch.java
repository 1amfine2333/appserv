package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppClientLogSearch {
    private Long id;

    private String deviceId;

    private Long partyId;

    private String systemType;
    
    private String type;

    private String version;

    private String content;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

}
