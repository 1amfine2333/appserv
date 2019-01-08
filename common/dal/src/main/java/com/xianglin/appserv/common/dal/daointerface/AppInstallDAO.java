package com.xianglin.appserv.common.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xianglin.appserv.common.dal.dataobject.AppInstall;
/**
 * 设备安装应用信息
 * 
 * 
 * @author zhangyong 2016年9月1日下午5:35:34
 */
public interface AppInstallDAO extends BaseDAO<AppInstall> {
	/**
	 * 
	 * 查询设备的应用
	 * 
	 * @param deviceId
	 * @return
	 */
	List<AppInstall> queryAppInstallByDeviceId(String deviceId);
	/**
	 * 删除应用
	 * 
	 * 
	 * @param deviceId
	 * @param appName
	 * @return
	 */
	int deleteAppInstall(@Param("deviceId")String deviceId,@Param("appName")String appName);
	
	/**
	 * 批量插入
	 * 
	 * 
	 * @param list
	 * @return
	 */
	int batchInsertAppInstall(List<AppInstall> list);
	
	/**
	 * 
	 * 
	 * 
	 * @param install
	 * @return
	 */
	int updateAppInstall(AppInstall install);
}