package com.xianglin.appserv.common.service.facade.model.vo;

/**
 * Created by wanglei on 2017/7/21.
 */
public class DistrictVo extends BaseVo{

    /**
     *区域代号
     */
    private String code;

    /**
     * 区域名
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
