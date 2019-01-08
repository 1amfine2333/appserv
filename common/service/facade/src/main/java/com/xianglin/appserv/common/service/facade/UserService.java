package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.BindUserDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.UserInfoDTO;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseMenuDTO;
import com.xianglin.appserv.common.service.facade.model.vo.AppCommuseWordDTO;

public interface UserService {
	/**
	 * 图片上传
	 * @dateTime 2016年11月23日 下午3:58:51
	 * @param userInfoDTO
	 * @return
	 */
	public Response<String> uploadImg(UserInfoDTO userInfoDTO);
	/**
	 * 获取用户信息
	 * @dateTime 2016年11月23日 下午4:07:41
	 * @param userInfoDTO
	 * @return
	 */
	public Response<UserInfoDTO> getUserInfo(UserInfoDTO userInfoDTO);

	/**
	 * 根据partyId查询用户信息
	 * 简单的信息，头像，昵称 性别 个人介绍 粉丝和关注数等
	 * 和app底部我的有区别
	 * @param userInfoDTO
	 * @return
     */
	Response<UserInfoDTO> getUserByPartyId(UserInfoDTO userInfoDTO);
	/**
	 * 更新用户信息
	 * @dateTime 2016年11月23日 下午4:09:28
	 * @param userInfoDTO
	 * @return
	 */
	public Response<UserInfoDTO> updateUserInfo(UserInfoDTO userInfoDTO);
	
	/**
	 * 绑定新手机号码
	 * @dateTime 2016年12月6日 上午11:04:23
	 * @param bindUserDTO
	 * @return
	 */
	Response<Boolean> bindNewPhone(BindUserDTO bindUserDTO);
	
	/**
	 * 更新用户常用服务
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午12:39:27
	 * @param appCommuseMenuDTO
	 */
	public Response<AppCommuseMenuDTO> saveOrUpdateUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO);
	
	/**
	 * 更新用户常用关键字
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午12:39:39
	 * @param appCommuseWordDTO
	 */
	public Response<AppCommuseWordDTO> saveOrUpdateUserCommWord(AppCommuseWordDTO appCommuseWordDTO);
	
	/**
	 * 获取前100条个人常用服务
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseMenuDTO
	 */
	public Response<List<AppCommuseMenuDTO>> queryTopUserCommMenu(AppCommuseMenuDTO appCommuseMenuDTO);
	/**
	 * 获取前100条常用服务
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseMenuDTO
	 */
	public Response<List<AppCommuseMenuDTO>> queryTopCommMenu(AppCommuseMenuDTO appCommuseMenuDTO);

	/**
	 * 获取前100条个人常用关键词
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseWordDTO
	 */
	public Response<List<AppCommuseWordDTO>> queryTopUserCommWord(AppCommuseWordDTO appCommuseWordDTO);
	/**
	 * 获取前100条常用关键词
	 * @author gengchaogang
	 * @dateTime 2016年12月10日 下午1:08:02
	 * @param appCommuseWordDTO
	 */
	public Response<List<AppCommuseWordDTO>> queryTopCommWord(AppCommuseWordDTO appCommuseWordDTO);
	
}
