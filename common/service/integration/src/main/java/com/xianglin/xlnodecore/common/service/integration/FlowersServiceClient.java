package com.xianglin.xlnodecore.common.service.integration;

import com.xianglin.xlnodecore.common.service.facade.base.CommonListResp;
import com.xianglin.xlnodecore.common.service.facade.base.CommonPageReq;
import com.xianglin.xlnodecore.common.service.facade.base.Response;
import com.xianglin.xlnodecore.common.service.facade.model.FlowersDTO;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/7
 * Time: 14:58
 */
public interface FlowersServiceClient {
    /**
     * 插入送花记录
     *
     * @param flowersDTO
     * @return
     */
    Response<FlowersDTO> insertFlowersLog(FlowersDTO flowersDTO);

    /**
     * 查询今日对 nodePartyId 献过花
     * 并且nodePartyId没有对他回赠的 站点
     *
     * @param nodePartyId
     * @param nodeManagerPartyId
     * @return
     */
    Response<List<Map<String, Object>>> queryTodayRecordNode(Long nodePartyId, Long nodeManagerPartyId);
    /**
     * 查询历史的对某个站点献花的记录
     *
     * @param flowersDTO
     * @return
     */
    CommonListResp<Map<String, Object>> queryHistoryRecordNode(CommonPageReq<FlowersDTO> flowersDTO);
}

