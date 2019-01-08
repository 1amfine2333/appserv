package com.xianglin.appserv.common.service.facade.model;

import java.io.Serializable;

/**
 * Created by wanglei on 2017/12/5.
 */
public interface AppSessionConstants extends Serializable{

    /**
     * 登陆用户partyId
     * Long 类型
     */
    public static final String PARTY_ID = "partyId";

    /**
     * 设备号
     * String
     */
    public static final String DEVICE_ID = "did";
    /**
     * 用户开通业务类型
     * Set<String>类型
     */
    public static final String BUSINESS_OPEN_INFO = "businessOpenInfo";

    /**
     * 用户手机号
     * String
     */
    public static final String LOGIN_NAME = "loginName";

    /**
     * 客户端版本号
     */
    public static final String CLIENT_VERSION = "clientVersion";

    /**
     * 社保类型，区分android和ios
     */
    public static final String SYSTEM_TYPE = "systemType";

}
