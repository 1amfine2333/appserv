package com.xianglin.appserv.biz.service.implement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.LuckWheelServivce;
import com.xianglin.appserv.common.service.facade.model.AppActiveShareDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizeUserRelDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizesActivityRuleDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.CommonResp;
import com.xianglin.appserv.core.service.LuckWheelCoreServivce;
import com.xianglin.appserv.core.service.impl.LuckWheelCoreServiceImpl;

@Service
public class LuckWheelServiceImpl implements LuckWheelServivce {
	private final Logger logger = LoggerFactory.getLogger(LuckWheelCoreServiceImpl.class);
	@Autowired
	private LuckWheelCoreServivce luckWheelCoreServivce;

	@Override
	public Response<List<AppPrizesActivityRuleDTO>> getAppPrizesActivity(AppPrizesActivityRuleDTO appPrizesActivityRuleDTO) {
		Response<List<AppPrizesActivityRuleDTO>> response = ResponseUtils.successResponse();
		try {
			List<AppPrizesActivityRuleDTO> appPrizesActivityRuleDTOs = luckWheelCoreServivce.getAppPrizesActivityRule(appPrizesActivityRuleDTO);
			List<AppPrizesActivityRuleDTO> appPrizesActivityRuleDTOs2 = new LinkedList<>();
			AppPrizesActivityRuleDTO appPrizesActivityRuleDTONew = null;
			for (AppPrizesActivityRuleDTO appPrizesActivityRuleDTO2 : appPrizesActivityRuleDTOs) {//排除权重展示
				appPrizesActivityRuleDTONew = new AppPrizesActivityRuleDTO();
				BeanUtils.copyProperties(appPrizesActivityRuleDTO2, appPrizesActivityRuleDTONew, "pamount","pweight");
				appPrizesActivityRuleDTOs2.add(appPrizesActivityRuleDTONew);
			}
			response.setResult(new ArrayList<>(appPrizesActivityRuleDTOs2));
		} catch (BusiException e) {
			logger.error("获取活动列表失败",e);
			response = ResponseUtils.toResponse(e.getResponseEnum());
		} catch (Exception e) {
			logger.error("获取活动列表失败",e);
			response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
		}
		return response;
	}

	@Override
	public Response<AppPrizeUserRelDTO> makeLuckWheel(AppPrizeUserRelDTO appPrizeUserRelDTO) {
		Response<AppPrizeUserRelDTO> response = ResponseUtils.successResponse();
		try {
			AppPrizeUserRelDTO appPrizeUserRelDTOResult = luckWheelCoreServivce.makeLuckWheel(appPrizeUserRelDTO);
			response.setResult(appPrizeUserRelDTOResult);
		} catch (BusiException e) {
			logger.error("抽奖失败",e);
			response = ResponseUtils.toResponse(e.getResponseEnum());
		} catch (Exception e) {
			logger.error("抽奖失败",e);
			response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
		}
		return response;
	}

	@Override
	public Response<AppActiveShareDTO> insertAppActiveShare(AppActiveShareDTO appActiveShareDTO) {
		Response<AppActiveShareDTO> response = ResponseUtils.successResponse();
		try {
			AppActiveShareDTO appActiveShareDTOResult = luckWheelCoreServivce.insertAppActiveShare(appActiveShareDTO);
			response.setResult(appActiveShareDTOResult);
		} catch (BusiException e) {
			logger.error("分享失败",e);
			response = ResponseUtils.toResponse(e.getResponseEnum());
		} catch (Exception e) {
			logger.error("分享失败",e);
			response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
		}
		return response;
	}

	@Override
	public Response<CommonResp<List<AppPrizeUserRelDTO>>> getUserLuckWheelPrizes(AppPrizeUserRelDTO appPrizeUserRelDTO) {
		Response<CommonResp<List<AppPrizeUserRelDTO>>> response = ResponseUtils.successResponse();
		try {
			CommonResp<List<AppPrizeUserRelDTO>> appActiveShareDTOResult = luckWheelCoreServivce.getUserLuckWheelPrizes(appPrizeUserRelDTO);
			response.setResult(appActiveShareDTOResult);
		} catch (BusiException e) {
			logger.error("获奖记录查询失败",e);
			response = ResponseUtils.toResponse(e.getResponseEnum());
		} catch (Exception e) {
			logger.error("获奖记录查询失败",e);
			response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
		}
		return response;
	}

	@Override
	public Response<Integer> getLastCount(AppPrizeUserRelDTO appPrizeUserRelDTO) {
		Response<Integer> response = ResponseUtils.successResponse();
		try {
			int appLastCount = luckWheelCoreServivce.getLastCount(appPrizeUserRelDTO);
			response.setResult(Integer.valueOf(appLastCount));
		} catch (BusiException e) {
			logger.error("查询剩余抽奖次数失败",e);
			response = ResponseUtils.toResponse(e.getResponseEnum());
		} catch (Exception e) {
			logger.error("查询剩余抽奖次数失败",e);
			response = ResponseUtils.toResponse(ResponseEnum.SYSTEM_EXCEPTION);
		}
		return response;
	}
	
}
