package com.xianglin.xlnodecore.common.service.integration;


import com.xianglin.xlnodecore.common.service.facade.base.CommonListResp;
import com.xianglin.xlnodecore.common.service.facade.base.CommonPageReq;
import com.xianglin.xlnodecore.common.service.facade.base.Response;
import com.xianglin.xlnodecore.common.service.facade.model.RewardVo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangyong
 * Date: 2016/11/7
 * Time: 14:11
 */
public interface RewardServiceClient {

    /**
     * 条件查询
     *
     * @param req
     * @return
     */
    CommonListResp<Map<String, Object>> queryTodayRecordNode(CommonPageReq<RewardVo> req);
    /**
     * 查询历史的对某个站点打赏的记录
     *
     * @param req
     * @return
     */
    CommonListResp<Map<String, Object>> queryHistoryRecordNode(CommonPageReq<RewardVo> req);
}
