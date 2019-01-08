package com.xianglin.xlnodecore.common.service.integration.impl;

import com.xianglin.xlnodecore.common.service.facade.AccountNodeManagerService;
import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;
import com.xianglin.xlnodecore.common.service.integration.AccountNodeManagerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/1
 * Time: 17:15
 */
public class AccountNodeManagerClientImpl implements AccountNodeManagerClient {
    @Autowired
    private AccountNodeManagerService accountNodeManagerService;
    @Override
    public AccountNodeManagerResp queryNodeManagerByPartyId(Long var1) {
        return accountNodeManagerService.queryNodeManagerByPartyId(var1);
    }
}
