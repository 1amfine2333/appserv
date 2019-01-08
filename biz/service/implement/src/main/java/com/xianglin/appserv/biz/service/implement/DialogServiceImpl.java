/**
 * 
 */
package com.xianglin.appserv.biz.service.implement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xianglin.appserv.biz.shared.util.ResponseUtils;
import com.xianglin.appserv.common.service.facade.DialogService;
import com.xianglin.appserv.common.service.facade.model.Response;
import com.xianglin.gateway.common.service.spi.model.enums.ResultEnum;

/**
 * 对话相关服务实现类
 * 
 * @author pengpeng 2016年3月29日上午11:05:08
 */
public class DialogServiceImpl implements DialogService {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(DialogServiceImpl.class);

	/** 默认私密消息最大生存期：30分钟 */
	private static final int MAX_MESSAGE_LIFETIME = 30 * 60;

	/**
	 * @see com.xianglin.appserv.common.service.facade.DialogService#startPrivateMode(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	//@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.DialogService.startPrivateMode", description = "开始私密模式")
	public Response<Boolean> startPrivateMode(String figureId, String otherFigureId, int messageLifetime) {
		logger.debug("startPrivateMode start,figureId:{},otherFigureId:{},messageLifetime:{}", figureId, otherFigureId,
				messageLifetime);
		if (StringUtils.isEmpty(figureId) || StringUtils.isEmpty(otherFigureId) || messageLifetime < 0
				|| messageLifetime > MAX_MESSAGE_LIFETIME) {
			return ResponseUtils.toResponse(ResultEnum.IllegalArgument);
		}
		long startTime = System.currentTimeMillis();
		Response<Boolean> response = ResponseUtils.successResponse();
		// response.setResult(locationInfoManager.newLocationInfo(locationInfo));
		long spendTime = System.currentTimeMillis() - startTime;
		logger.debug("startPrivateMode end,response:{},{}ms", response, spendTime);
		return response;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.DialogService#updatePrivateMessageLifetime(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	//@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.DialogService.updatePrivateMessageLifetime", description = "更新私密消息生存期")
	public Response<Boolean> updatePrivateMessageLifetime(String figureId, String otherFigureId, int messageLifetime) {
		logger.debug("updatePrivateMessageLifetime start,figureId:{},otherFigureId:{},messageLifetime:{}", figureId,
				otherFigureId, messageLifetime);
		if (StringUtils.isEmpty(figureId) || StringUtils.isEmpty(otherFigureId) || messageLifetime < 0
				|| messageLifetime > MAX_MESSAGE_LIFETIME) {
			return ResponseUtils.toResponse(ResultEnum.IllegalArgument);
		}
		long startTime = System.currentTimeMillis();
		Response<Boolean> response = ResponseUtils.successResponse();
		// response.setResult(locationInfoManager.newLocationInfo(locationInfo));
		long spendTime = System.currentTimeMillis() - startTime;
		logger.debug("updatePrivateMessageLifetime end,response:{},{}ms", response, spendTime);
		return response;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.DialogService#endPrivateMode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	//@ServiceMethod(alias = "com.xianglin.appserv.common.service.facade.DialogService.endPrivateMode", description = "结束私密模式")
	public Response<Boolean> endPrivateMode(String figureId, String otherFigureId) {
		logger.debug("endPrivateMode start,figureId:{},otherFigureId:{}", figureId, otherFigureId);
		if (StringUtils.isEmpty(figureId) || StringUtils.isEmpty(otherFigureId)) {
			return ResponseUtils.toResponse(ResultEnum.IllegalArgument);
		}
		long startTime = System.currentTimeMillis();
		Response<Boolean> response = ResponseUtils.successResponse();
		// response.setResult(locationInfoManager.newLocationInfo(locationInfo));
		long spendTime = System.currentTimeMillis() - startTime;
		logger.debug("endPrivateMode end,response:{},{}ms", response, spendTime);
		return response;
	}

}
