package com.xianglin.appserv.common.service.facade.model;

/**
 * Describe :
 * Created by xingyali on 2018/3/8 15:35.
 * Update reason :
 */
public class IZyFnProductQueryReqDTO {
    private static final long serialVersionUID = -7758440768750989122L;
    private String fundCode;

    public IZyFnProductQueryReqDTO() {
    }

    public String getFundCode() {
        return this.fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
}
