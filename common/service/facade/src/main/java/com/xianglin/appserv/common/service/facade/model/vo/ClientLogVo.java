package com.xianglin.appserv.common.service.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientLogVo {
    private Long id;

    private String deviceId;

    private String systemType;

    private String version;

    private String message;

}
