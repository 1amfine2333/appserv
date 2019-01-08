/**
 * 
 */
package com.xianglin.appserv.biz.shared.impl;

import com.xianglin.appserv.common.service.facade.DialogService;
import com.xianglin.appserv.common.service.facade.model.Response;

/**
 * 对话相关业务服务实现类
 * 
 * @author pengpeng 2016年3月29日上午11:22:04
 */
public class DialogManagerImpl implements DialogService {

//	/** logger */
//	private static final Logger logger = LoggerFactory.getLogger(DialogManagerImpl.class);
//
//	private MessageSendClient messageSendClient;

	/**
	 * @see com.xianglin.appserv.common.service.facade.DialogService#startPrivateMode(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public Response<Boolean> startPrivateMode(String figureId, String otherFigureId, int messageLifetime) {
		return null;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.DialogService#updatePrivateMessageLifetime(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public Response<Boolean> updatePrivateMessageLifetime(String figureId, String otherFigureId, int messageLifetime) {
		// Auto-generated method stub
		return null;
	}

	/**
	 * @see com.xianglin.appserv.common.service.facade.DialogService#endPrivateMode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Response<Boolean> endPrivateMode(String figureId, String otherFigureId) {
		// Auto-generated method stub
		return null;
	}

}
