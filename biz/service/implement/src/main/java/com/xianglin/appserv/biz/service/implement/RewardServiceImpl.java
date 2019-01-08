package com.xianglin.appserv.biz.service.implement;

import com.xianglin.appserv.biz.shared.util.LoginAttrUtil;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.RewardService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.FacadeEnums;
import com.xianglin.appserv.common.service.facade.model.vo.CommonResp;
import com.xianglin.appserv.common.service.facade.model.vo.RewardVo;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.xlnodecore.common.service.facade.base.CommonPageReq;
import com.xianglin.xlnodecore.common.service.integration.RewardServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 打赏
 *
 * @dateTime 2016年11月3日 下午1:45:41
 */
@ServiceInterface
public class RewardServiceImpl implements RewardService {
    private final static Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);
    @Autowired
    private RewardServiceClient rewardServiceClient;

    @Autowired
    private LoginAttrUtil loginAttrUtil;
    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RewardService.queryTodayRecordNode", description = "当天站点打赏列表查询")
    public Response<CommonResp<List<Map<String, Object>>>> queryTodayRecordNode(RewardVo reRewardVo) {
        Response<CommonResp<List<Map<String, Object>>>> response = ResponseUtils.successResponse();

        try {
           // reRewardVo.setToNodePartyId(loginAttrUtil.getPartyId());
            CommonPageReq<com.xianglin.xlnodecore.common.service.facade.model.RewardVo> req  = DTOUtils.map(reRewardVo, CommonPageReq.class);
            com.xianglin.xlnodecore.common.service.facade.model.RewardVo coreReWardVo =DTOUtils.map(reRewardVo, com.xianglin.xlnodecore.common.service.facade.model.RewardVo.class);

            req.setReqVo(coreReWardVo);
            com.xianglin.xlnodecore.common.service.facade.base.CommonListResp<Map<String, Object>> responseNc =
                    rewardServiceClient.queryTodayRecordNode(req);
            if (responseNc.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code) {
                logger.error("当天打赏列表查询失败");
                response.setFacade(FacadeEnums.RETURN_EMPTY);
                return response;
            }
            CommonResp<List<Map<String,Object>>> resp =new CommonResp<>();
            resp.setResult(responseNc.getVos());
            resp.setCurPage(responseNc.getCurPage());
            resp.setTotalCount(responseNc.getTotalCount());
            response.setResult(resp);
        } catch (Exception e) {
            logger.error("查询打赏记录出现问题", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }


        return response;
    }

    @Override
    @ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.RewardService.queryHistoryRecordNode", description = "打赏列表查询")
    public Response<CommonResp<List<Map<String, Object>>>> queryHistoryRecordNode(RewardVo rewardVo) {
        Response<CommonResp<List<Map<String, Object>>>> response = ResponseUtils.successResponse();

        try {
            CommonPageReq<com.xianglin.xlnodecore.common.service.facade.model.RewardVo> req  = DTOUtils.map(rewardVo, CommonPageReq.class);
            com.xianglin.xlnodecore.common.service.facade.model.RewardVo coreReWardVo =DTOUtils.map(rewardVo, com.xianglin.xlnodecore.common.service.facade.model.RewardVo.class);

            req.setReqVo(coreReWardVo);
            com.xianglin.xlnodecore.common.service.facade.base.CommonListResp<Map<String, Object>> responseNc =
                    rewardServiceClient.queryHistoryRecordNode(req);
            if (responseNc.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code) {
                logger.error("打赏列表查询失败");
                response.setFacade(FacadeEnums.RETURN_EMPTY);
                return response;
            }
            CommonResp<List<Map<String,Object>>> resp =new CommonResp<>();
            resp.setResult(responseNc.getVos());
            resp.setCurPage(responseNc.getCurPage());
            resp.setTotalCount(responseNc.getTotalCount());
            response.setResult(resp);
        } catch (Exception e) {
            logger.error("查询打赏记录出现问题", e);
            response.setFacade(FacadeEnums.RETURN_EMPTY);
        }


        return response;
    }


}
