package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppPush {
    private Long id;

    private String deviceId;

    private Long partyId;

    private String pushType;

    private String pushToken;

    private String systemType;

    private String deviceInfo;

    private String status;
    
    private String version;

    private Integer pushReceives;
    
    private Integer pushClick;

    private String isDeleted;

    private String creator;

    private String updater;

    private Date createTime;

    private Date updateTime;

    private String comments;
}