package com.xianglin.appserv.core.service;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.AppMenuDTO;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;

public interface AppMenuCoreService {
	/**
	 * 查询菜单列表
	 * @author gengchaogang
	 * @dateTime 2016年12月9日 上午10:57:44
	 * @param appMenuDTO
	 * @return
	 * @throws BusiException 
	 */
	public List<AppMenuDTO> getAppMenus(AppMenuDTO appMenuDTO) throws BusiException;
}
