/**
 * 
 */
package com.xianglin.appserv.biz.shared;

/**
 * 对哈相关业务服务
 * 
 * @author pengpeng 2016年3月29日上午11:20:34
 */
public interface DialogManager {

	/**
	 * 开始私密模式
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @return
	 */
	boolean startPrivateMode(String figureId, String otherFigureId, int messageLifetime);

	/**
	 * 更新私密消息生存期
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @param messageLifetime
	 * @return
	 */
	boolean updatePrivateMessageLifetime(String figureId, String otherFigureId, int messageLifetime);

	/**
	 * 结束私密模式
	 * 
	 * @param figureId
	 * @param otherFigureId
	 * @return
	 */
	boolean endPrivateMode(String figureId, String otherFigureId);
}
