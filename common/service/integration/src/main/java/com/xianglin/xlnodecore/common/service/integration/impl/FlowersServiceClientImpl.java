package com.xianglin.xlnodecore.common.service.integration.impl;

import com.xianglin.xlnodecore.common.service.facade.FlowersService;
import com.xianglin.xlnodecore.common.service.facade.base.CommonListResp;
import com.xianglin.xlnodecore.common.service.facade.base.CommonPageReq;
import com.xianglin.xlnodecore.common.service.facade.base.Response;
import com.xianglin.xlnodecore.common.service.facade.model.FlowersDTO;
import com.xianglin.xlnodecore.common.service.integration.FlowersServiceClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/7
 * Time: 14:59
 */
public class FlowersServiceClientImpl implements FlowersServiceClient {
    @Autowired
    private FlowersService flowersService;
    @Override
    public Response<FlowersDTO> insertFlowersLog(FlowersDTO flowersDTO) {
        return flowersService.insertFlowersLog(flowersDTO);
    }

    @Override
    public Response<List<Map<String, Object>>> queryTodayRecordNode(Long nodePartyId, Long nodeManagerPartyId) {
        return flowersService.queryTodayRecordNode(nodePartyId,nodeManagerPartyId);
    }

    @Override
    public CommonListResp<Map<String, Object>> queryHistoryRecordNode(CommonPageReq<FlowersDTO> flowersDTO) {
        return flowersService.queryHistoryRecordNode(flowersDTO);
    }
}
