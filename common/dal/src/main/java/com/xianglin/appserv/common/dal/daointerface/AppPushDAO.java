package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.AppPush;

public interface AppPushDAO extends BaseDAO<AppPush>{

	/**
	 * 根据条件分页查询
	 * @param paras
	 * @return
	 */
	public List<AppPush> query(Map<String, Object> paras);
	
	/**
	 * 绑定用户到指定设备
	 * @param deviceId
	 * @param partyId
	 * @return
	 */
	public int bindDevice(@Param("deviceId") String deviceId,@Param("partyId")Long partyId);
	
	/**
	 * 解除用户与所有设备的绑定
	 * @param partyId
	 * @return
	 */
	public int unBindDevice(@Param("partyId")Long partyId);
	
	/**
	 * 根据设备号更新推送配置信息
	 * @param push
	 * @return
	 */
	public int updateByDeviceId(AppPush push);
	
	/**
	 * 查询第一版需要推送的用户
	 * 
	 * @return
	 */
	public List<AppPush> selectJpushDevice();

	
	/**
	 * 针对ios系统，当deviceId不同而iosToken相同时，
	 * @param deviceId
	 * @param pushToken
	 * @return
	 */
	public int updateBandIOS8(@Param("deviceId") String deviceId,@Param("pushToken")String pushToken);

	/**
	 * 批量删除过期的tokens
	 * 
	 * @param tokens
	 */
	public int deleteBatch(Set<String> tokens);

	/**根据设备号查询
	 * @param deviceId
	 * @return
	 */
	AppPush selectByDeviceId(@Param("deviceId") String deviceId);
}