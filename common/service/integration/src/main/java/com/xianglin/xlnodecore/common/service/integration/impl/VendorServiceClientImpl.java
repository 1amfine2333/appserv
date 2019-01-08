package com.xianglin.xlnodecore.common.service.integration.impl;

import com.xianglin.xlnodecore.common.service.facade.VendorService;
import com.xianglin.xlnodecore.common.service.facade.base.Response;
import com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums;
import com.xianglin.xlnodecore.common.service.integration.VendorServiceClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/12/14
 * Time: 11:45
 */
public class VendorServiceClientImpl implements VendorServiceClient {
    @Autowired
    private VendorService vendorService;

    @Override
    public Boolean queryVendorIsExistByAreaCode(String areaCode) {
        Response<Boolean> response = vendorService.queryVendorIsExistByAreaCode(areaCode);
        if(FacadeEnums.OK.getCode() == response.getCode()){
            return response.getResult();
        }
        return false;
    }
}
