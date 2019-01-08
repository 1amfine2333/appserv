/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.DeviceInfo;
import com.xianglin.appserv.common.service.facade.model.Response;


/**
 * 
 * 设备信息服务
 * 
 * @author cf 2016年8月15日上午10:50:43
 */
public interface DeviceInfoService {

	/**
	 * 设备激活
	 * 
	 * @param deviceInfo
	 *            设备信息
	 * @return 设备唯一标识
	 */
	Response<String> activateDevice(DeviceInfo deviceInfo);

	/**
	 * 更新iOSToken
	 * 
	 * @param iOSToken
	 * @return
	 */
	Response<Boolean> updateIOSToken(String iOSToken);


}
