package com.xianglin.appserv.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xianglin.appserv.common.dal.daointerface.AppMenuModelMapper;
import com.xianglin.appserv.common.dal.dataobject.AppMenuModel;
import com.xianglin.appserv.common.dal.dataobject.AppMenuModelExample;
import com.xianglin.appserv.common.service.facade.model.AppMenuDTO;
import com.xianglin.appserv.common.service.facade.model.enums.DataStatusEnums;
import com.xianglin.appserv.common.service.facade.model.enums.ResponseEnum;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.util.DTOUtils;
import com.xianglin.appserv.core.service.AppMenuCoreService;

@Service
public class AppMenuCoreServiceImpl implements AppMenuCoreService {
	private final Logger logger = LoggerFactory.getLogger(AppMenuCoreServiceImpl.class);
	@Autowired
	private AppMenuModelMapper appMenuModelMapper;

	@Override
	public List<AppMenuDTO> getAppMenus(AppMenuDTO appMenuDTO) throws BusiException {
		AppMenuModelExample appMenuModelExample = new AppMenuModelExample();
		AppMenuModelExample.Criteria criteria = appMenuModelExample.createCriteria();
		criteria.andMstatusEqualTo(DataStatusEnums.Y.getCode());
		if(appMenuDTO.getBusitype() != null){
			criteria.andBusitypeEqualTo(appMenuDTO.getBusitype());
		}
		appMenuModelExample.setOrderByClause("MID");
		List<AppMenuModel> appMenuModels = appMenuModelMapper.selectByExample(appMenuModelExample);
		
		List<AppMenuDTO> appMenuDTOs = null;
		try {
			appMenuDTOs = DTOUtils.map(appMenuModels, AppMenuDTO.class);
		} catch (Exception e) {
			logger.error("查询菜单列表失败",e);
			throw new BusiException(ResponseEnum.BUSI_INVALD,"查询菜单列表失败");
		}
		
		return appMenuDTOs;
	}
	
}
