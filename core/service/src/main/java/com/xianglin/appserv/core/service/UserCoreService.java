package com.xianglin.appserv.core.service;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.UserInfoDTO;
import com.xianglin.appserv.common.service.facade.model.exception.BusiException;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseMenuDTO;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseWordDTO;

public interface UserCoreService {
	/**
	 * 更新用户信息
	 * @author gengchaogang
	 * @dateTime 2016年12月8日 下午3:13:55
	 * @param userInfoDTO
	 * @return
	 * @throws BusiException
	 */
	public void updateUserInfo(UserInfoDTO userInfoDTO) throws BusiException;
	/**
	 * 更新用户常用服务
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午12:39:27
	 * @param appCommuseMenuDTO
	 * @throws BusiException
	 */
	public void saveOrUpdateUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) throws BusiException;
	/**
	 * 获取前100条个人常用服务
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseMenuDTO
	 * @throws BusiException
	 */
	public List<AppCommuseMenuDTO> queryTopUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) throws BusiException;
	/**
	 * 获取前100条常用服务
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseMenuDTO
	 * @throws BusiException
	 */
	public List<AppCommuseMenuDTO> queryTopCommMenu(AppCommuseMenuDTO appCommuseMenuDTO) throws BusiException;
	/**
	 * 更新用户常用关键字
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午12:39:39
	 * @param appMenuDTO
	 * @throws BusiException
	 */
	public void saveOrUpdateUserCommWord(AppCommuseWordDTO appCommuseWordDTO) throws BusiException;
	/**
	 * 获取前100条个人常用关键词
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseMenuDTO
	 * @throws BusiException
	 */
	public List<AppCommuseWordDTO> queryTopUserCommWord(AppCommuseWordDTO appCommuseWordDTO) throws BusiException;
	/**
	 * 获取前100条常用关键词
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseMenuDTO
	 * @throws BusiException
	 */
	public List<AppCommuseWordDTO> queryTopCommWord(AppCommuseWordDTO appCommuseWordDTO) throws BusiException;
	
}
