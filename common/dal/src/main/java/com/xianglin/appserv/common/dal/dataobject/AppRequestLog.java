package com.xianglin.appserv.common.dal.dataobject;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppRequestLog {
    private Long id;

    private String deviceId;

    private String sessionId;

    private Long partyId;

    private String requestId;

    private String operationType;

    private Long reqId;

    private Long reqTimestamp;

    private Long respId;

    private Long respTimestamp;

    private String isDeleted;

    private Date createTime;

    private Date updateTime;

    private String comments;

}
