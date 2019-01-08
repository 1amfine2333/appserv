/**
 * 
 */
package com.xianglin.appserv.common.service.facade;

import com.xianglin.appserv.common.service.facade.model.Response;

/**
 * 对话相关服务
 * 
 * @author pengpeng 2016年3月29日上午10:55:05
 */
public interface DialogService {

	/**
	 * 开始私密模式
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @return
	 */
	Response<Boolean> startPrivateMode(String figureId, String otherFigureId, int messageLifetime);

	/**
	 * 更新私密消息生存期
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @param messageLifetime
	 * @return
	 */
	Response<Boolean> updatePrivateMessageLifetime(String figureId, String otherFigureId, int messageLifetime);

	/**
	 * 结束私密模式
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @return
	 */
	Response<Boolean> endPrivateMode(String figureId, String otherFigureId);

}
