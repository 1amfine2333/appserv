package com.xianglin.appserv.common.dal.dataobject;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("app_client_log_login")
public class AppClientLogLogin extends Base{

    private Long partyId;

    private String deviceId;

    private String type;

    private String ip;

    private String systemType;

    private String systemVersion;

    private String version;

    private String deviceName;

    private String apkSource;
}