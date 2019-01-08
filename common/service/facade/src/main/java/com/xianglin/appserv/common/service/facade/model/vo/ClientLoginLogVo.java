package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.*;

import java.util.Date;


/**
 * 用户登陆日志
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoginLogVo extends BaseVo{

    private Long id;

    private Long partyId;

    private String deviceId;

    private String type;

    private String ip;

    private String systemType;

    private String systemVersion;

    private String version;

    private String deviceName;

    private String apkSource;

    private Date createTime;

    private Date updateTime;

    private String comments;
}
