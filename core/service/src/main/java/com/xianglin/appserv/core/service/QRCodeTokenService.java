/**
 * 
 */
package com.xianglin.appserv.core.service;

/**
 * 二维码token相关服务
 * 
 * @author pengpeng 2016年3月23日上午11:31:49
 */
public interface QRCodeTokenService {

	/**
	 * 生成用户token
	 * 
	 * @param figureId
	 * @return
	 */
	String getUserToken(String figureId);

	/**
	 * 生成群组token
	 * 
	 * @param groupId
	 * @return
	 */
	String getGroupToken(String groupId);

	/**
	 * 查询Token相关信息
	 * 
	 * @param token
	 * @return
	 */
	String getTokenInfo(String token);
}
