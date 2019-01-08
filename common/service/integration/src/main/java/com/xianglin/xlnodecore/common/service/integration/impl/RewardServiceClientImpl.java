package com.xianglin.xlnodecore.common.service.integration.impl;

import com.alibaba.dubbo.common.utils.Log;
import com.xianglin.te.common.service.facade.enums.Constants;
import com.xianglin.xlnodecore.common.service.facade.RewardService;
import com.xianglin.xlnodecore.common.service.facade.base.CommonListResp;
import com.xianglin.xlnodecore.common.service.facade.base.CommonPageReq;
import com.xianglin.xlnodecore.common.service.facade.base.Response;
import com.xianglin.xlnodecore.common.service.facade.model.RewardVo;
import com.xianglin.xlnodecore.common.service.integration.RewardServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/7
 * Time: 14:15
 */
public class RewardServiceClientImpl implements RewardServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(RewardServiceClientImpl.class);
    @Autowired
    private RewardService rewardService;
    @Override
    public CommonListResp<Map<String, Object>> queryTodayRecordNode(CommonPageReq<RewardVo>req) {

         return rewardService.queryTodayRecordNode(req);
    }

    @Override
    public CommonListResp<Map<String, Object>> queryHistoryRecordNode(CommonPageReq<RewardVo> req) {
        return rewardService.queryHistoryRecordNode(req);
    }


}
