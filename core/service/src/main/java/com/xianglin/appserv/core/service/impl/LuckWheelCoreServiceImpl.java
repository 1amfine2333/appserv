package com.xianglin.appserv.core.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xianglin.appserv.common.dal.daointerface.AppActiveShareMapper;
import com.xianglin.appserv.common.dal.daointerface.AppPrizeUserRelModelMapper;
import com.xianglin.appserv.common.dal.daointerface.AppPrizesActivityRuleDAO;
import com.xianglin.appserv.common.dal.dataobject.AppActiveShare;
import com.xianglin.appserv.common.dal.dataobject.AppActiveShareExample;
import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelDO;
import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelModelExample;
import com.xianglin.appserv.common.dal.dataobject.AppPrizeUserRelModelExample.Criteria;
import com.xianglin.appserv.common.dal.dataobject.AppPrizesActivityRuleDO;
import com.xianglin.appserv.common.service.facade.model.AppActiveShareDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizeUserRelDTO;
import com.xianglin.appserv.common.service.facade.model.AppPrizesActivityRuleDTO;
import com.xianglin.appserv.common.service.facade.model.enums.ActivePrizeRuleEnums;
import com.xianglin.appserv.common.service.facade.model.enums.Constant.UserType;
import com.xianglin.appserv.common.service.facade.model.enums.DataStatusEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.CommonResp;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.common.util.SysConfigUtil;
import com.xianglin.appserv.common.util.XLCommonUtils;
import com.xianglin.appserv.core.service.LuckWheelCoreServivce;
import com.xianglin.appserv.core.service.LuckWheelMakeCoreService;
import com.xianglin.xlnodecore.common.service.facade.NodeService;
import com.xianglin.xlnodecore.common.service.facade.base.Response;

@Service
@Transactional(rollbackFor = {BusiException.class,RuntimeException.class})
public class LuckWheelCoreServiceImpl implements LuckWheelCoreServivce {
	private final Logger logger = LoggerFactory.getLogger(LuckWheelCoreServiceImpl.class);
	private static final String REDIS_LUCKWHEEL_LOCK = "REDIS_LUCKWHEEL_LOCK";
	
	@Autowired
	private AppPrizesActivityRuleDAO appPrizesActivityRuleDAO;
	@Autowired
	private AppActiveShareMapper appActiveShareMapper;
	@Autowired
	private LuckWheelMakeCoreService luckWheelMakeCoreService;
	@Autowired
    private NodeService nodeService;
	@Autowired
	private AppPrizeUserRelModelMapper appPrizeUserRelModelMapper;

	@Override
	@Transactional(readOnly = true)
	public List<AppPrizesActivityRuleDTO> getAppPrizesActivityRule(AppPrizesActivityRuleDTO appPrizesActivityRuleDTO) throws BusiException{
		AppPrizesActivityRuleDO appPrizesActivityRuleDO = new AppPrizesActivityRuleDO();
		appPrizesActivityRuleDO.setActiveId(appPrizesActivityRuleDTO.getActiveId());
		List<AppPrizesActivityRuleDO> appPrizesActivityRuleDOs = appPrizesActivityRuleDAO.queryActivePrizeRule(appPrizesActivityRuleDO);
		
		AppPrizesActivityRuleDTO appPrizesActivityRuleDTO2 = null;
		Map<String, AppPrizesActivityRuleDTO> map = new TreeMap<>();
		for (AppPrizesActivityRuleDO appPrizesActivityRuleDO2 : appPrizesActivityRuleDOs) {
			if(map.containsKey(appPrizesActivityRuleDO2.getPrizeId().toPlainString())){
				appPrizesActivityRuleDTO2 = map.get(appPrizesActivityRuleDO2.getPrizeId().toPlainString());
			}else{
				appPrizesActivityRuleDTO2 = new AppPrizesActivityRuleDTO();
				map.put(appPrizesActivityRuleDO2.getPrizeId().toPlainString(), appPrizesActivityRuleDTO2);
			}
			appPrizesActivityRuleDTO2.setActiveId(appPrizesActivityRuleDO2.getActiveId());
			appPrizesActivityRuleDTO2.setRuleID(appPrizesActivityRuleDO2.getRuleID());
			appPrizesActivityRuleDTO2.setPrizeId(appPrizesActivityRuleDO2.getPrizeId());
			appPrizesActivityRuleDTO2.setPtype(appPrizesActivityRuleDO2.getPtype());
			appPrizesActivityRuleDTO2.setPname(appPrizesActivityRuleDO2.getPname());
			switch (ActivePrizeRuleEnums.valueOf(appPrizesActivityRuleDO2.getRuleCode())) {
				case D0001:
					appPrizesActivityRuleDTO2.setPamount(new BigDecimal(appPrizesActivityRuleDO2.getRuleValue()));
					break;
				case P0001:
					appPrizesActivityRuleDTO2.setPweight(new BigDecimal(appPrizesActivityRuleDO2.getRuleValue()));
					break;
				case M0001:
					appPrizesActivityRuleDTO2.setPmoney(new BigDecimal(appPrizesActivityRuleDO2.getRuleValue()));
					break;
				default:
					break;
			}
		}
		return new ArrayList<>(map.values());
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public AppPrizeUserRelDTO makeLuckWheel(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException{
		return luckWheelMakeCoreService.synLock(REDIS_LUCKWHEEL_LOCK.concat(appPrizeUserRelDTO.getPartyid()), appPrizeUserRelDTO);
	}

	@Override
	public AppActiveShareDTO insertAppActiveShare(AppActiveShareDTO appActiveShareDTO) throws BusiException {
		
		AppActiveShare appActiveShare = new AppActiveShare();
		try {
			BeanUtils.copyProperties(appActiveShare, appActiveShareDTO);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("对象转换异常",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
		
		appActiveShare.setCreateTime(new Date());
		appActiveShare.setSharetime(new Date());
		appActiveShare.setDataStatus(DataStatusEnums.Y.getCode());
		
		appActiveShareMapper.insertSelective(appActiveShare);
		return appActiveShareDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public CommonResp<List<AppPrizeUserRelDTO>> getUserLuckWheelPrizes(AppPrizeUserRelDTO appPrizeUserRelDTO)
			throws BusiException {
		AppPrizeUserRelDO appPrizeUserRelDO = new AppPrizeUserRelDO();
		try {
			BeanUtils.copyProperties(appPrizeUserRelDO, appPrizeUserRelDTO);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("对象转换异常",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
		
		AppPrizeUserRelModelExample appPrizeUserRelModelExample = new AppPrizeUserRelModelExample();
		Criteria criteria = appPrizeUserRelModelExample.createCriteria();
		if(appPrizeUserRelDO.getPartyid() != null){
			criteria = criteria.andPartyidEqualTo(appPrizeUserRelDO.getPartyid());
		}
		criteria.andActiveidEqualTo(appPrizeUserRelDO.getActiveid()).andDataStatusEqualTo(DataStatusEnums.Y.getCode());
		int totalCount = appPrizeUserRelModelMapper.countByExample(appPrizeUserRelModelExample);
		
		List<AppPrizeUserRelDO> appPrizeUserRelDOs = appPrizesActivityRuleDAO.getUserLuckWheelPrizes(appPrizeUserRelDO);
		List<AppPrizeUserRelDTO> appPrizeUserRelDTOs;
		try {
			appPrizeUserRelDTOs = DTOUtils.map(appPrizeUserRelDOs, AppPrizeUserRelDTO.class);
		} catch (Exception e) {
			logger.error("对象转换异常",e);
			throw new BusiException(ResponseEnum.SYSTEM_EXCEPTION);
		}
		StringBuilder stringBuilder = null;
		for (AppPrizeUserRelDTO appPrizeUserRelDTO2 : appPrizeUserRelDTOs) {
			stringBuilder = new StringBuilder();
			if(!UserType.nodeManager.name().equals(appPrizeUserRelDTO2.getUserType())){
				if(StringUtils.isNotEmpty(appPrizeUserRelDTO2.getNikerName())){
					stringBuilder.append(appPrizeUserRelDTO2.getNikerName());
				}else {
					stringBuilder.append(XLCommonUtils.maskMobile(appPrizeUserRelDTO2.getLoginName()));
				}
			}else{
				Response<Map<String, Object>> distrinctFullResult = nodeService.queryDistrictCodeFull(null, Long.valueOf(appPrizeUserRelDTO2.getPartyid()));
				if(distrinctFullResult.getCode() != com.xianglin.xlnodecore.common.service.facade.enums.FacadeEnums.OK.code
						|| distrinctFullResult.getResult() == null){
					logger.error("查询行政区域失败");
					if(StringUtils.isNotEmpty(appPrizeUserRelDTO2.getNikerName())){
						stringBuilder.append(appPrizeUserRelDTO2.getNikerName());
					}else {
						stringBuilder.append(XLCommonUtils.maskMobile(appPrizeUserRelDTO2.getLoginName()));
					}
					continue;
				}
				Map<String, Object> distrinctFullMap = distrinctFullResult.getResult();
				String nodeManagerName = distrinctFullMap.get("nodeManagerName") == null ? " " : distrinctFullMap.get("nodeManagerName").toString();
				String cityName = String.valueOf(distrinctFullMap.get("cityName"));
				String message = String.format("%s %s站长", cityName,nodeManagerName.substring(0, 1));
				stringBuilder.append(message);
			}
			stringBuilder.append("获得").append(appPrizeUserRelDTO2.getPname());
			appPrizeUserRelDTO2.setTipMsg(stringBuilder.toString());
		}
		CommonResp<List<AppPrizeUserRelDTO>> commonResp = new CommonResp<>();
		commonResp.setTotalCount(totalCount);
		commonResp.setCurPage(appPrizeUserRelDTO.getCurPage());
		commonResp.setResult(appPrizeUserRelDTOs);
		return commonResp;
	}

	@Override
	@Transactional(readOnly = true)
	public int getLastCount(AppPrizeUserRelDTO appPrizeUserRelDTO) throws BusiException {
		//最大参加次数
		int maxApply = SysConfigUtil.getInt(LuckWheelMakeCoreService.getLuckWheelMaxApply(), 2);
		AppPrizeUserRelModelExample appPrizeUserRelModelExample = new AppPrizeUserRelModelExample();
		appPrizeUserRelModelExample.createCriteria().andActiveidEqualTo(appPrizeUserRelDTO.getActiveid()).andPrizetimeEqualTo(new Date())
		.andPartyidEqualTo(appPrizeUserRelDTO.getPartyid()).andDataStatusEqualTo(DataStatusEnums.Y.getCode());
		//当天参加次数
		int hasApplyCount = appPrizeUserRelModelMapper.countByExample(appPrizeUserRelModelExample);
		
		int result = maxApply - hasApplyCount;
		
		if(result == 0){
			return result;
		}
		
		AppActiveShareExample appActiveShareExample = new AppActiveShareExample();
		appActiveShareExample.createCriteria().andActiveidEqualTo(appPrizeUserRelDTO.getActiveid()).andSharetimeEqualTo(new Date())
		.andPartyidEqualTo(appPrizeUserRelDTO.getPartyid()).andDataStatusEqualTo(DataStatusEnums.Y.getCode());
		int shareAccount = appActiveShareMapper.countByExample(appActiveShareExample);
		if(shareAccount <= 0){//扣除没有分享的1次
			result = result - 1;
		}
		
		return result;
	}

}
