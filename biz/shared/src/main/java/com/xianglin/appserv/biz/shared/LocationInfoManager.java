/**
 * 
 */
package com.xianglin.appserv.biz.shared;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.LocationInfoDTO;
import com.xianglin.appserv.common.service.facade.model.UserFigureDTO;

/**
 * 地理位置信息管理器
 * 
 * @author pengpeng 2016年2月24日下午8:29:16
 */
public interface LocationInfoManager {

	/**
	 * 处理新的地理位置信息
	 * 
	 * @param locationInfo
	 * @param geohash
	 * @return
	 */
	boolean newLocationInfo(LocationInfoDTO locationInfo);

	/**
	 * 根据地理位置信息查找附近的人
	 * 
	 * @param locationInfo
	 * @return
	 */
	List<UserFigureDTO> findNearbyUsers(LocationInfoDTO locationInfo);

}
