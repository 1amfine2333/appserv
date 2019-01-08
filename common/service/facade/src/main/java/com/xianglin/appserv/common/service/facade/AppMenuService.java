package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.AppMenuDTO;
import com.xianglin.appserv.common.service.facade.model.Response;

public interface AppMenuService {
	/**
	 * 获取App菜单资源列表
	 * 
	 * @author gengchaogang
	 * @dateTime 2016年12月9日 上午10:47:20
	 * @param userInfoDTO
	 * @return
	 */
	public Response<List<AppMenuDTO>> getAppMenus(AppMenuDTO appMenuDTO);

	// 根据菜单名查服务信息
	public List<AppMenuDTO> queryMenu(AppMenuDTO req);

	// 更新菜单
	public Boolean updateMenu(AppMenuDTO vo);
}
