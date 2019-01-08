/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.biz.shared.LocationInfoManager;
import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.LBSService;
import com.xianglin.appserv.common.service.facade.model.LocationInfoDTO;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.appserv.common.service.facade.model.UserFigureDTO;
import com.xianglin.gateway.common.service.spi.annotation.ServiceInterface;
import com.xianglin.gateway.common.service.spi.annotation.ServiceMethod;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 地理位置相关服务实现类
 * 
 * @author pengpeng 2016年2月24日下午3:57:10
 */
//@ServiceInterface
public class LBSServiceImpl implements LBSService {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(LBSServiceImpl.class);

	/** locationInfoManager */
	private LocationInfoManager locationInfoManager;

	/**
	 * @see com.xianglin.appserv.common.service.facade.LBSService#reportLocation(com.xianglin.appserv.common.service.facade.model.LocationInfoDTO)
	 */
	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.LBSService.reportLocation", description = "上报地理位置")
	public Response<Boolean> reportLocation(LocationInfoDTO locationInfo) {
		logger.debug("reportLocation start,locationInfo:{}", locationInfo);
		if (!checkParam(locationInfo)) {
			return ResponseUtils.toResponse(ResultEnum.IllegalArgument);
		}
		long startTime = System.currentTimeMillis();
		Response<Boolean> response = ResponseUtils.successResponse();
		response.setResult(locationInfoManager.newLocationInfo(locationInfo));
		long spendTime = System.currentTimeMillis() - startTime;
		logger.debug("reportLocation end,response:{},{}ms", response, spendTime);
		return response;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.LBSService#findNearbyUsers(com.xianglin.appserv.common.service.facade.model.LocationInfoDTO)
	 */
	@Override
	@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.LBSService.findNearbyUsers", description = "查找附近的人")
	public Response<List<UserFigureDTO>> findNearbyUsers(LocationInfoDTO locationInfo) {
		logger.debug("reportLocation start,locationInfo:{}", locationInfo);
		if (!checkParam(locationInfo)) {
			return ResponseUtils.toResponse(ResultEnum.IllegalArgument);
		}
		long startTime = System.currentTimeMillis();
		Response<List<UserFigureDTO>> response = ResponseUtils.successResponse();
		response.setResult(locationInfoManager.findNearbyUsers(locationInfo));
		long spendTime = System.currentTimeMillis() - startTime;
		logger.debug("reportLocation end,response:{},{}ms", response, spendTime);
		return response;
	}

	/**
	 * 检查参数
	 * 
	 * @param locationInfo
	 */
	private boolean checkParam(LocationInfoDTO locationInfo) {
		if (locationInfo == null) {
			logger.warn("reportLocation, illegal locationInfo:{}", locationInfo);
			return false;
		}
		locationInfo.setPosition(StringUtils.trim(locationInfo.getPosition()));
		if (StringUtils.length(locationInfo.getPosition()) > 256) {
			logger.warn("position is too long! locationIngo:{}", locationInfo);
			return false;
		}
		if (Math.abs(locationInfo.getLatitude()) > 90.0 || Math.abs(locationInfo.getLongitude()) > 180.0) {
			logger.warn("reportLocation, illegal locationInfo:{}", locationInfo);
			return false;
		}
		return true;
	}

	/**
	 * @param locationInfoManager
	 *            the locationInfoManager to set
	 */
	public void setLocationInfoManager(LocationInfoManager locationInfoManager) {
		this.locationInfoManager = locationInfoManager;
	}

}
