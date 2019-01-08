/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import java.util.List;

import com.xianglin.appserv.common.service.facade.model.LocationInfoDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.UserFigureDTO;

/**
 * 地理位置相关服务
 * 
 * @author pengpeng 2016年2月18日下午2:03:20
 */
public interface LBSService {

	/**
	 * 上报地理位置
	 * 
	 * @param locationInfo
	 * @return
	 */
	Response<Boolean> reportLocation(LocationInfoDTO locationInfo);

	/**
	 * 查找附近的人
	 * 
	 * @param locationInfo
	 * @return
	 */
	Response<List<UserFigureDTO>> findNearbyUsers(LocationInfoDTO locationInfo);

}
