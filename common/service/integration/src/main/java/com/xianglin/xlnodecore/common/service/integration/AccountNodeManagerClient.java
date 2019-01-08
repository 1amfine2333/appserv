package com.xianglin.xlnodecore.common.service.integration;

import com.xianglin.xlnodecore.common.service.facade.resp.AccountNodeManagerResp;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/1
 * Time: 16:29
 */
public interface AccountNodeManagerClient {

    AccountNodeManagerResp queryNodeManagerByPartyId(Long var1);
}
